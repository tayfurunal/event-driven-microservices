package com.tayfurunal.basketservice.configuration.mq;

import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;

import java.util.UUID;

import static com.tayfurunal.basketservice.configuration.MdcConstants.UNKNOWN_APPLICATION;
import static com.tayfurunal.basketservice.configuration.MdcConstants.X_AGENT_NAME;
import static com.tayfurunal.basketservice.configuration.MdcConstants.X_AGENT_USER;
import static com.tayfurunal.basketservice.configuration.MdcConstants.X_CORRELATION_ID;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface RabbitMqMdc {

    String SYSTEM_USER_ID = "-1";

    MessagePostProcessor MESSAGE_POST_PROCESSOR = message -> {
        message.getMessageProperties().getHeaders().put(X_CORRELATION_ID, retrieveCorrelationFromMDC());
        message.getMessageProperties().getHeaders().put(X_AGENT_NAME, retrieveAgentNameFromMDC());
        message.getMessageProperties().getHeaders().put(X_AGENT_USER, retrieveAgentUserFromMDC());
        return message;
    };

    MethodInterceptor METHOD_INTERCEPTOR = invocation -> {
        Message message = (Message) invocation.getArguments()[1];
        MDC.put(X_CORRELATION_ID, retrieveCorrelationFromMessage(message));
        MDC.put(X_AGENT_NAME, retrieveAgentNameFromMessage(message));
        MDC.put(X_AGENT_USER, retrieveAgentUserFromMessage(message));
        return invocation.proceed();
    };

    static String retrieveCorrelationFromMessage(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        if (isNull(messageProperties)) {
            return UUID.randomUUID().toString();
        }
        String correlationId = (String) messageProperties.getHeaders().getOrDefault(X_CORRELATION_ID, EMPTY);
        if (isBlank(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }
        return correlationId;
    }

    static String retrieveAgentNameFromMessage(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        if (isNull(messageProperties)) {
            return UNKNOWN_APPLICATION;
        }
        String agentName = (String) messageProperties.getHeaders().get(X_AGENT_NAME);
        if (isBlank(agentName)) {
            String queueName = messageProperties.getConsumerQueue();
            return UNKNOWN_APPLICATION + (isNotBlank(queueName) ? "[" + queueName + "]" : EMPTY);
        }
        return agentName;
    }

    static String retrieveAgentUserFromMessage(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        if (isNull(messageProperties)) {
            return SYSTEM_USER_ID;
        }
        String agentUser = (String) messageProperties.getHeaders().get(X_AGENT_USER);
        if (isBlank(agentUser)) {
            return SYSTEM_USER_ID;
        }
        return agentUser;
    }

    static String retrieveCorrelationFromMDC() {
        String correlationId = MDC.get(X_CORRELATION_ID);
        if (isBlank(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }
        return correlationId;
    }

    static String retrieveAgentNameFromMDC() {
        String agentName = MDC.get(X_AGENT_NAME);
        if (isBlank(agentName)) {
            agentName = UNKNOWN_APPLICATION;
        }
        return agentName;
    }

    static String retrieveAgentUserFromMDC() {
        String agentUser = MDC.get(X_AGENT_USER);
        if (isBlank(agentUser)) {
            agentUser = SYSTEM_USER_ID;
        }
        return agentUser;
    }
}
