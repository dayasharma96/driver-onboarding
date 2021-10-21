package com.uber.driver.onboarding.core.event.kafka;

import com.uber.driver.onboarding.core.event.handler.IMessageHandler;
import com.uber.driver.onboarding.core.event.model.Message;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author drs
 * <p>
 * Base class for all Kafa consumers.
 */
public class KafkaMsgConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaMsgConsumer.class);

    private final KafkaConsumer<String, Message> consumer;
    private final IMessageHandler messageHandler;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    private final BlockingExecutor blockingExecutor = new BlockingExecutor(50, new ThreadPoolExecutor(
            10, 20, 180L,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>()));

    public KafkaMsgConsumer(KafkaConsumer<String, Message> consumer, IMessageHandler messageHandler) {
        this.consumer = consumer;
        this.messageHandler = messageHandler;
        run();
    }

    public void run() {
        final Thread thread = new Thread(this::startConsumer);
        thread.setDaemon(true);
        thread.setName(consumer.getClass().getName() + "-" + thread.getName());
        thread.start();
    }

    private void startConsumer() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        try {
            while (!closed.get()) {

                ConsumerRecords<String, Message> records = consumer.poll(300);
                records.forEach(record -> {
                    blockingExecutor.execute(() -> {
                        try {
                            messageHandler.handleMessage(record.value());
                        }
                        catch (Exception ex) {
                            log.error("Exception while processing message handler.", ex);
                        }
                    });
                });

                if (!records.isEmpty()) {
                    consumer.commitAsync((offsets, e) -> {
                        if (e != null) {
                            log.error("Commit failed for offsets " + offsets);
                        }
                    });
                }
            }
        } catch (WakeupException e) {
            if (!closed.get()) {
                log.warn("Error in consumer as : ", e);
                throw e;
            }
        } catch (Exception e) {
            log.warn("Error in consumer as ", e);
        } finally {
            log.info("Closing customer.");
            consumer.close();
        }
    }

    private static class BlockingExecutor implements Executor {

        final Semaphore semaphore;
        final ExecutorService delegate;

        private BlockingExecutor(final int concurrentTasksLimit, final ExecutorService delegate) {
            semaphore = new Semaphore(concurrentTasksLimit);
            this.delegate = delegate;
        }

        @Override
        public void execute(final Runnable command) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            final Runnable wrapped = () -> {
                try {
                    command.run();
                } finally {
                    semaphore.release();
                }
            };
            delegate.execute(wrapped);
        }
    }

    public void shutdown() {
        blockingExecutor.delegate.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!blockingExecutor.delegate.awaitTermination(300, TimeUnit.SECONDS)) {
                blockingExecutor.delegate.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!blockingExecutor.delegate.awaitTermination(300, TimeUnit.SECONDS)) {
                    log.warn("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            blockingExecutor.delegate.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        log.info("Inside shutdown for consumer.");
        closed.set(true);
        consumer.wakeup();
    }

}
