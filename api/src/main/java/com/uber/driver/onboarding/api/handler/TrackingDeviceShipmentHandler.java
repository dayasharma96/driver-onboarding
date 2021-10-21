package com.uber.driver.onboarding.api.handler;

import com.uber.driver.onboarding.api.service.IUserService;
import com.uber.driver.onboarding.core.event.model.Message;

public class TrackingDeviceShipmentHandler extends AbstractMsgHandler {

    private final IUserService userService;

    public TrackingDeviceShipmentHandler(IUserService userService) {
        this.userService = userService;
    }


    @Override
    protected void processMessage(Message message) {

    }

    @Override
    public void validateMessage(Message message) {

    }
}
