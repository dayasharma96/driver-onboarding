package com.uber.driver.onboarding.web.controller.v1;

import com.uber.driver.onboarding.api.service.IUserService;
import com.uber.driver.onboarding.core.repository.entity.User;
import com.uber.driver.onboarding.model.request.UserLoginRequest;
import com.uber.driver.onboarding.model.response.Response;
import com.uber.driver.onboarding.web.controller.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/account")
public class RestAccountsAPI extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(RestAccountsAPI.class);

    @Autowired
    private IUserService userService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<User> signup(@RequestBody UserLoginRequest signupRequest) {
        logger.debug("Received request for user signup : {}", signupRequest);
        User user = userService.signup(signupRequest.getUserType(), signupRequest.getEmail(), signupRequest.getPassword());
        return new Response.Builder<User>().withBody(user).withStatus(HttpStatus.CREATED.value()).build();
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<User> login(@RequestBody UserLoginRequest loginRequest) {
        logger.debug("Received request for user login : {}", loginRequest);
        User user = userService.login(loginRequest.getUserType(), loginRequest.getEmail(), loginRequest.getPassword());
        return new Response.Builder<User>().withBody(user).withStatus(HttpStatus.OK.value()).build();
    }
}