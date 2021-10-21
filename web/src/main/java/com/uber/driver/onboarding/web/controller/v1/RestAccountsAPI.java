package com.uber.driver.onboarding.web.controller.v1;

import com.uber.driver.onboarding.api.service.IUserService;
import com.uber.driver.onboarding.core.repository.entity.User;
import com.uber.driver.onboarding.model.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/account")
public class RestAccountsAPI {

    private static final Logger logger = LoggerFactory.getLogger(RestAccountsAPI.class);

    @Autowired
    private IUserService userService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<User> signup(@RequestBody String taskRequest) {
//        logger.debug("Received request for user signup : {}", taskRequest);
//        Task task = taskService.upsert(taskRequest);
//        return new Response.Builder<Task>().withBody(task).withStatus(HttpStatus.SC_CREATED).build();
        return null;
    }

}