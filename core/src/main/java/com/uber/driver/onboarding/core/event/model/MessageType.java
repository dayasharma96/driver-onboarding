package com.uber.driver.onboarding.core.event.model;

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
}
