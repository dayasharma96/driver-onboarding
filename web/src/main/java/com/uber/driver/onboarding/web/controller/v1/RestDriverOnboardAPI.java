package com.uber.driver.onboarding.web.controller.v1;

import com.uber.driver.onboarding.api.service.IDriverDocumentService;
import com.uber.driver.onboarding.api.service.IDriverOnboarding;
import com.uber.driver.onboarding.model.enums.DocumentType;
import com.uber.driver.onboarding.model.response.Response;
import com.uber.driver.onboarding.web.controller.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/driver")
public class RestDriverOnboardAPI extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(RestDriverOnboardAPI.class);

    @Autowired
    private IDriverDocumentService driverDocumentService;

    @Autowired
    private IDriverOnboarding driverOnboarding;

    @PostMapping(value = "/uploadDocument", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> login(@RequestParam("file") MultipartFile file, @RequestParam("type") DocumentType type) {
        String userId = getLoggedInUserIdWithError();
        driverDocumentService.saveDocument(userId, type, file);
        return new Response.Builder<>().withBody(null).withStatus(HttpStatus.OK.value()).build();
    }

    @GetMapping(value = "/initVerification", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> sendForVerification() {
        String userId = getLoggedInUserIdWithError();
        driverOnboarding.initDriverVerification(userId);
        return new Response.Builder<>().withBody(null).withStatus(HttpStatus.OK.value()).build();
    }

}
