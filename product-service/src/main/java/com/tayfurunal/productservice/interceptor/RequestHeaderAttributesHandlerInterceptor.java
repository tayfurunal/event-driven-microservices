package com.tayfurunal.productservice.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.tayfurunal.productservice.configuration.MdcConstants.X_AGENT_NAME;
import static com.tayfurunal.productservice.configuration.MdcConstants.X_AGENT_USER;
import static com.tayfurunal.productservice.configuration.MdcConstants.X_CORRELATION_ID;

public class RequestHeaderAttributesHandlerInterceptor implements HandlerInterceptor {

    private static final String SYSTEM_USER = "1";
    private final String applicationName;

    public RequestHeaderAttributesHandlerInterceptor(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String correlationId = request.getHeader(X_CORRELATION_ID);
        String agentName = request.getHeader(X_AGENT_NAME);
        String agentUser = request.getHeader(X_AGENT_USER);

        if (StringUtils.isBlank(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }
        if (StringUtils.isBlank(agentName)) {
            agentName = applicationName;
        }
        if (StringUtils.isBlank(agentUser)) {
            agentUser = SYSTEM_USER;
        }

        MDC.put(X_CORRELATION_ID, correlationId);
        MDC.put(X_AGENT_NAME, agentName);
        MDC.put(X_AGENT_USER, agentUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.remove(X_CORRELATION_ID);
        MDC.remove(X_AGENT_NAME);
        MDC.remove(X_AGENT_USER);
    }
}
