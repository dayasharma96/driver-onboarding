package com.uber.driver.onboarding.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class GenUtil {

    public static String getIPDetail() {
        try {
            final DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (Exception ex) {
            return org.apache.commons.lang3.StringUtils.EMPTY;
        }
    }

    public static String asHex(byte[] buf) {
        StringBuilder strbuf = new StringBuilder(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }

    public static Cookie getCookie(Cookie[] cookies, String cookieName) {
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static boolean getBooleanParameter(HttpServletRequest request, String name, boolean defaultValue) {
        boolean value = defaultValue;
        if (request != null && name != null) {
            String param = request.getParameter(name);
            if (param != null) {
                value = "1".equals(param) || "true".equalsIgnoreCase(param);
            }
        }

        return value;
    }

    /**
     * Overloading method which takes the response code.
     *
     * @param responseCode Response code
     * @return True If it is a 200 or 201 response
     */

    public static boolean hasSuccessCode(int responseCode) {
        return ((responseCode == 201) || (responseCode == 200));
    }

    public static String getRemoteAddr(HttpServletRequest request) {
        // If Akamai Header present use that
        String remoteIpAddress = request.getHeader("true-client-ip");

        if (remoteIpAddress == null) {

            String remoteAddresses = request.getHeader("ns-remote-addr");
            if (remoteAddresses != null) {
                String[] remoteIpAddresses = remoteAddresses.split(",");
                remoteIpAddress = remoteIpAddresses[0];
            }

            String forwardedIpAddress = request.getHeader("x-forwarded-for");
            int pos = 0;
            if (forwardedIpAddress != null) {
                pos = forwardedIpAddress.indexOf(',');
            }
            if (pos > 0) {
                forwardedIpAddress = forwardedIpAddress.substring(0, pos);
            }
            if (remoteIpAddress == null || pos > 0) {
                // If a comma is present in forwardedIpAddress that is given
                // preference since it
                // would be forwarded by a proxy server
                remoteIpAddress = forwardedIpAddress;
            }
            if (remoteIpAddress == null) {
                remoteIpAddress = request.getRemoteAddr();
            }
        }

        if (remoteIpAddress != null) {
            remoteIpAddress = remoteIpAddress.trim();
        }

        return remoteIpAddress;
    }

}
