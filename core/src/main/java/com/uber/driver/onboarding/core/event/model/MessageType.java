package com.uber.driver.onboarding.core.event.model;

import com.uber.driver.onboarding.model.enums.DriverState;

public enum MessageType {

    DRIVER_BG_VERIFICATION("driverBgVerification", "Message to trigger background verificaion."),
    DRIVER_TRACKING_DEVICE_SHIPMENT("driverTrackingDeviceShipment", "Message to trigger tracking device shipment.");

    private final String eventTopic;
    private final String description;

    MessageType(String eventTopic, String description) {
        this.eventTopic = eventTopic;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getEventTopic() {
        return eventTopic;
    }

    public static MessageType lookUp(DriverState state) {
        if(state.getNextState().equals(DriverState.BACKGROUND_VERIFIED)) {
            return DRIVER_BG_VERIFICATION;
        }
        else if(state.getNextState().equals(DriverState.DEVICE_SHIPPED)) {
            return DRIVER_TRACKING_DEVICE_SHIPMENT;
        }
        return null;
    }

}
