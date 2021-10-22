package com.uber.driver.onboarding.web.configuration;

import com.uber.driver.onboarding.api.handler.BgVerificationHandler;
import com.uber.driver.onboarding.api.handler.TrackingDeviceShipmentHandler;
import com.uber.driver.onboarding.api.service.IDriverDocumentService;
import com.uber.driver.onboarding.api.service.IDriverOnboarding;
import com.uber.driver.onboarding.api.service.IUserService;
import com.uber.driver.onboarding.api.service.impl.DriverDocService;
import com.uber.driver.onboarding.api.service.impl.DriverOnboardingService;
import com.uber.driver.onboarding.api.service.impl.UserService;
import com.uber.driver.onboarding.core.event.ConsumerFactory;
import com.uber.driver.onboarding.core.event.ProducerFactory;
import com.uber.driver.onboarding.core.event.handler.BaseHandler;
import com.uber.driver.onboarding.core.event.handler.IMessageHandler;
import com.uber.driver.onboarding.core.event.kafka.KafkaMsgConsumer;
import com.uber.driver.onboarding.core.event.kafka.KafkaMsgProducer;
import com.uber.driver.onboarding.core.event.kafka.config.IKafkaConsumerProperties;
import com.uber.driver.onboarding.core.event.kafka.config.IKafkaProducerProperties;
import com.uber.driver.onboarding.core.event.kafka.config.KafkaConsumerProps;
import com.uber.driver.onboarding.core.event.kafka.config.KafkaProducerProps;
import com.uber.driver.onboarding.core.event.kafka.serde.KafkaJsonDeserializer;
import com.uber.driver.onboarding.core.event.kafka.serde.KafkaJsonSerializer;
import com.uber.driver.onboarding.core.event.model.Message;
import com.uber.driver.onboarding.core.event.model.MessageType;
import com.uber.driver.onboarding.core.repository.dao.IDriverInfoDao;
import com.uber.driver.onboarding.core.repository.dao.IUserDao;
import com.uber.driver.onboarding.core.repository.dao.mysql.DriverInfoDao;
import com.uber.driver.onboarding.core.repository.dao.mysql.UserDao;
import com.uber.driver.onboarding.model.pojo.IProperties;
import com.uber.driver.onboarding.model.util.JsonUtil;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApiContext {

    @Autowired
    private IProperties properties;

    @Bean
    public IUserService getUserService(UserDao userDao, KafkaMsgProducer msgProducer) {
        return new UserService(userDao, msgProducer);
    }

    @Bean
    public IDriverDocumentService driverDocumentService(IUserDao userDao, IDriverInfoDao driverInfoDao) {
        return new DriverDocService(userDao, driverInfoDao);
    }

    @Bean
    public IDriverOnboarding driverOnboarding(UserDao userDao, DriverInfoDao driverInfoDao, KafkaMsgProducer msgProducer) {
        return new DriverOnboardingService(userDao, driverInfoDao, msgProducer);
    }

    @Bean
    public IMessageHandler baseMessageHandler(IUserService userService) {
        Map<MessageType, IMessageHandler> iMessageHandlerMap = new HashMap<>();
        iMessageHandlerMap.put(MessageType.DRIVER_BG_VERIFICATION, new BgVerificationHandler(userService));
        iMessageHandlerMap.put(MessageType.DRIVER_TRACKING_DEVICE_SHIPMENT, new TrackingDeviceShipmentHandler(userService));
        return new BaseHandler(iMessageHandlerMap);
    }

    @Bean(value = "eventConsumer")
    public KafkaMsgConsumer eventConsumer(IMessageHandler messageHandler) {
        String bootstrapUrl = properties.getPropertyValue("com.uber.driver.onboarding.event.consumer.brokers");
        String groupId = properties.getPropertyValue("com.uber.driver.onboarding.event.consumer.group.id");
        String topics = properties.getPropertyValue("com.uber.driver.onboarding.event.consumer.topics");
        final IKafkaConsumerProperties consumerProps = new KafkaConsumerProps(bootstrapUrl, groupId, Arrays.asList(topics.split(",")));
        final KafkaConsumer<String, Message> consumer = ConsumerFactory.buildConsumer(consumerProps, new StringDeserializer(), new KafkaJsonDeserializer<>(JsonUtil.getObjectMapper(), Message.class));
        return new KafkaMsgConsumer(consumer, messageHandler);
    }

    @Bean(value = "eventPublisher")
    public KafkaMsgProducer eventPublisher() {
        String brokers = properties.getPropertyValue("com.uber.driver.onboarding.event.publisher.brokers");
        String clientId = properties.getPropertyValue("com.uber.driver.onboarding.event.publisher.client.id");
        final IKafkaProducerProperties producerProperties = new KafkaProducerProps(brokers, clientId + "_" + System.currentTimeMillis());
        final KafkaJsonSerializer<Message> jsonSerializer = new KafkaJsonSerializer<>(JsonUtil.getObjectMapper());
        KafkaProducer<String, Message> producer = ProducerFactory.buildProducer(producerProperties, new StringSerializer(), jsonSerializer);
        return new KafkaMsgProducer(producer);
    }

}
