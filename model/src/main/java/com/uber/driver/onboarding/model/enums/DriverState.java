package com.uber.driver.onboarding.model.enums;

public enum DriverState {

    SIGNED_UP {
        @Override
        public DriverState getNextState() {
            return DOCUMENT_COLLECTED;
        }
    },
    DOCUMENT_COLLECTED {
        @Override
        public DriverState getNextState() {
            return BACKGROUND_VERIFICATION_IN_PROGRESS;
        }
    },
    BACKGROUND_VERIFICATION_IN_PROGRESS {
        @Override
        public DriverState getNextState() {
            return BACKGROUND_VERIFIED;
        }
    },
    BACKGROUND_VERIFIED {
        @Override
        public DriverState getNextState() {
            return DEVICE_SHIPPED;
        }
    },
    DEVICE_SHIPPED {
        @Override
        public DriverState getNextState() {
            return DEVICE_RECEIVED;
        }
    },
    DEVICE_RECEIVED {
        @Override
        public DriverState getNextState() {
            return ACTIVE;
        }
    },
    ACTIVE {
        @Override
        public DriverState getNextState() {
            return DEVICE_RECOLLECT;
        }
    },
    DEVICE_RECOLLECT {
        @Override
        public DriverState getNextState() {
            return INACTIVE;
        }
    },
    INACTIVE {
        @Override
        public DriverState getNextState() {
            return DOCUMENT_COLLECTED;
        }
    };

    public abstract DriverState getNextState();

}
