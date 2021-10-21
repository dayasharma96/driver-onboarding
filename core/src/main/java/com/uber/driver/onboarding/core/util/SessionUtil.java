package com.uber.driver.onboarding.core.util;

import com.uber.driver.onboarding.core.repository.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;

import java.util.Arrays;

import static com.uber.driver.onboarding.core.util.SecurityUtil.*;

public class SessionUtil {

    public static void login(User user) {
        Cookie cookie = SecurityUtil.generateAuthenticationCookie(user.getUserType(), user.getId());
        ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ra != null && ra.getResponse() != null) {
            ra.getResponse().addCookie(cookie);
        }
    }

    public static String getLoggedInUserId() {
        ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(ra != null && ra.getRequest().getCookies() != null) {
            return Arrays.stream(ra.getRequest().getCookies()).filter(cookie -> cookie.getName().equalsIgnoreCase(AUTH_COOKIE_NAME)).map(SecurityUtil::decryptAuthCookie).filter(StringUtils::isNotBlank).findFirst().map(decryptedCookie -> decryptedCookie.split(PIPE_REGEX)[1].trim()).orElse(null);
        }
        return null;
    }

}
