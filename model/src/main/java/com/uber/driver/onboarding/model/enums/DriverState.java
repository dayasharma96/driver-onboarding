package com.uber.driver.onboarding.model.enums;

public enum DriverState {

    SIGNED_UP {
        @Override
        public DriverState getNextState() {
            return DOCUMENT_SENT;
        }
    },
    DOCUMENT_SENT {
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
            return DEVICE_RECOLLECTED;
        }
    },
    DEVICE_RECOLLECTED {
        @Override
        public DriverState getNextState() {
            return null;
        }
    };

    public abstract DriverState getNextState();

}
