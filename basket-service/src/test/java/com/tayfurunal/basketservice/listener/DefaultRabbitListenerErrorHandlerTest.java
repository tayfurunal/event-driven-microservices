package com.tayfurunal.basketservice.listener;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class DefaultRabbitListenerErrorHandlerTest {

    @InjectMocks
    private DefaultRabbitListenerErrorHandler defaultRabbitListenerErrorHandler;

    @Test
    public void it_should_log_and_rethrow_rabbitmq_listener_exception() {
        //given
        Message amqpMessage = mock(Message.class);
        org.springframework.messaging.Message<?> message = mock(org.springframework.messaging.Message.class);
        ListenerExecutionFailedException exception = new ListenerExecutionFailedException("listenerException",
                                                                                          new RuntimeException("exception"),
                                                                                          amqpMessage);

        //when
        ListenerExecutionFailedException thrown = (ListenerExecutionFailedException) Assertions.catchThrowable(
                () -> defaultRabbitListenerErrorHandler.handleError(amqpMessage, message, exception)
        );

        //then
        assertThat(thrown).isEqualTo(exception);
    }


}