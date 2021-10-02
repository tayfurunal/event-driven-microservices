package com.tayfurunal.productservice.configuration.mq;

import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import static com.tayfurunal.productservice.configuration.mq.RabbitMQMDC.MESSAGE_POST_PROCESSOR;
import static com.tayfurunal.productservice.configuration.mq.RabbitMQMDC.METHOD_INTERCEPTOR;

@Configuration
public class RabbitMQConfiguration {

    private final RabbitProperties rabbitProperties;

    @Value("${spring.application.name}")
    private String applicationName;

    public RabbitMQConfiguration(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setChannelTransacted(true);
        rabbitTemplate.setBeforePublishPostProcessors(MESSAGE_POST_PROCESSOR);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                                                     ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setAdviceChain(retryOperationsInterceptor(), METHOD_INTERCEPTOR);
        return factory;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(rabbitProperties.getHost());
        cachingConnectionFactory.setUsername(rabbitProperties.getUsername());
        cachingConnectionFactory.setPassword(rabbitProperties.getPassword());
        cachingConnectionFactory.setPort(rabbitProperties.getPort());
        cachingConnectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
        cachingConnectionFactory.setConnectionNameStrategy(connFactory -> applicationName);
        return cachingConnectionFactory;
    }

    @Bean
    public RetryOperationsInterceptor retryOperationsInterceptor() {
        RabbitProperties.ListenerRetry retryConfig = rabbitProperties.getListener().getSimple().getRetry();
        return RetryInterceptorBuilder
                .stateless()
                .maxAttempts(retryConfig.getMaxAttempts())
                .backOffOptions(
                        retryConfig.getInitialInterval().getSeconds(),
                        retryConfig.getMultiplier(),
                        retryConfig.getMaxInterval().getSeconds()
                )
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }
}
