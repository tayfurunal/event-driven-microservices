package com.tayfurunal.basketservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultRabbitListenerErrorHandler implements RabbitListenerErrorHandler {


    @Override
    public Object handleError(Message message,
                              org.springframework.messaging.Message<?> message1,
                              ListenerExecutionFailedException e) throws Exception {
        log.error("Error occurred on queue listening. Message: " + message, e);
        throw e;
    }
}
