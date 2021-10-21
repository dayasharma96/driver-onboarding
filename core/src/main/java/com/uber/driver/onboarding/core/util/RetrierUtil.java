package com.uber.driver.onboarding.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

public class RetrierUtil {

    private static final String CONNECT_TIMED_OUT = "connect timed out";
    private static final String CONNECTION_EXCEPTION = "java.net.connectexception";

    public static <T, P> P retry(Supplier<T> function, int retryCount, Class<P> pClass, long sleepTimeInMillies) throws Exception {
        while (0 < retryCount) {
            try {
                return (P) function.get();
            } catch (Exception e) {
                Thread.sleep(sleepTimeInMillies);
                retryCount--;
                if (retryCount == 0) {
                    throw e;
                }
            }
        }
        return (P) function.get();
    }

    public static <T, P> P connectExceptionRetry(Supplier<T> function, int retryCount, Class<P> pClass, long sleepTimeInMillies) throws Exception {
        while (0 < retryCount) {
            try {
                return (P) function.get();
            } catch (Exception e) {
                if(!isConnectRetriable(e.getMessage().toLowerCase()) || retryCount == 1) {
                    throw e;
                }
                Thread.sleep(sleepTimeInMillies);
                retryCount--;
            }
        }
        return (P) function.get();
    }

    private static boolean isConnectRetriable(String exceptionMsg) {
        return StringUtils.isNotBlank(exceptionMsg) &&
                (exceptionMsg.contains(CONNECT_TIMED_OUT) || exceptionMsg.contains(CONNECTION_EXCEPTION));
    }

}
