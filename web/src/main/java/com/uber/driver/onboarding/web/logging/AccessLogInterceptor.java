package com.uber.driver.onboarding.web.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Interceptor for logging request access log.
 */
@Component
public class AccessLogInterceptor extends HandlerInterceptorAdapter {

	private final ThreadLocal<Long> startTime = new ThreadLocal<>();
	private static final Logger ACCESS_LOG = LoggerFactory.getLogger("access.log");
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		startTime.set(System.currentTimeMillis());
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse response, Object handler,
                                Exception ex) {
		String requestHost = httpServletRequest.getHeader("X-Request-Host");
		String requestRealIP = httpServletRequest.getHeader("X-Real-IP");
		if (requestHost == null)
			requestHost = httpServletRequest.getRemoteAddr();
		String remoteUser = httpServletRequest.getRemoteUser();

		if (remoteUser == null)
			remoteUser = "-";
		Date date = new Date();
		String method = httpServletRequest.getMethod();
		String uri = httpServletRequest.getRequestURI();
		String protocol = httpServletRequest.getProtocol();

		String referer = httpServletRequest.getHeader("Referer");
		if (referer == null)
			referer = "-";

		String userAgent = httpServletRequest.getHeader("User-agent");
		if (userAgent == null)
			userAgent = "-";

		String callingHost = httpServletRequest.getHeader("Host");
		if (callingHost == null)
			userAgent = "-";
		setRequestId(httpServletRequest);
		StringBuilder record = new StringBuilder("")
                .append(requestRealIP != null ? requestRealIP: requestHost).append(" ")
                .append("- ")
                .append(remoteUser).append(" ")
                .append("[").append(dateFormat.format(date)).append("] ")
                .append("\"").append(method).append(" ").append(uri).append(" ").append(protocol).append("\" ")
                .append(response.getStatus()).append(" ")
                .append("- ")
                .append("\"").append(referer).append("\" ")
                .append("\"").append(userAgent).append("\" ")
                .append("\"").append(callingHost).append("\" ")
                .append(System.currentTimeMillis() - startTime.get());
		ACCESS_LOG.info(record.toString());
	}
	
	private void setRequestId(HttpServletRequest httpServletRequest) {
		String requestId = httpServletRequest.getHeader("X-Request-ID");
		if (requestId == null)
			requestId = UUID.randomUUID().toString();
		requestId = requestId.replaceAll("\\s", "-");
		MDC.put("request-id", requestId);
	}
}
