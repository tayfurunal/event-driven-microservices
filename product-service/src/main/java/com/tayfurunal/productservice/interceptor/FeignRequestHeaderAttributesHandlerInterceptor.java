package com.tayfurunal.productservice.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import static com.tayfurunal.productservice.configuration.MDCConstants.X_AGENT_NAME;
import static com.tayfurunal.productservice.configuration.MDCConstants.X_AGENT_USER;
import static com.tayfurunal.productservice.configuration.MDCConstants.X_CORRELATION_ID;

@Component
public class FeignRequestHeaderAttributesHandlerInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String correlationId = MDC.get(X_CORRELATION_ID);
        if (StringUtils.isNotBlank(correlationId)) {
            requestTemplate.header(X_CORRELATION_ID, correlationId);
        }

        String agentName = MDC.get(X_AGENT_NAME);
        if (StringUtils.isNotBlank(agentName)) {
            requestTemplate.header(X_AGENT_NAME, agentName);
        }

        String agentUser = MDC.get(X_AGENT_USER);
        if (StringUtils.isNotBlank(agentUser)) {
            requestTemplate.header(X_AGENT_USER, agentUser);
        }
    }
}