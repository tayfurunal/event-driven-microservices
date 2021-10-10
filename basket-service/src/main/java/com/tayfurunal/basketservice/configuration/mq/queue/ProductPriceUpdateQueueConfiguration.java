package com.tayfurunal.basketservice.configuration.mq.queue;

import com.tayfurunal.basketservice.configuration.mq.RabbitMqConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ProductPriceUpdateQueueConfiguration {

    public static final String PRODUCT_PRICE_UPDATE_QUEUE = "basket.service.product.price.update";
    public static final String PRODUCT_PRICE_UPDATE_EXCHANGE = "basket.service.product.price.update";
    private static final String PRODUCT_PRICE_UPDATE_QUEUE_DEAD_LETTER = "basket.service.product.price.update.dead-letter";

    private final RabbitAdmin rabbitAdmin;

    @Bean
    public Queue productPriceUpdateQueue() {
        final Queue queue = QueueBuilder.durable(PRODUCT_PRICE_UPDATE_QUEUE)
                .withArgument(RabbitMqConstants.X_DEAD_LETTER_EXCHANGE, StringUtils.EMPTY)
                .withArgument(RabbitMqConstants.X_DEAD_LETTER_ROUTING_KEY, PRODUCT_PRICE_UPDATE_QUEUE_DEAD_LETTER)
                .build();
        queue.setAdminsThatShouldDeclare(rabbitAdmin);
        return queue;
    }

    @Bean
    public DirectExchange productPriceUpdateExchange() {
        return new DirectExchange(PRODUCT_PRICE_UPDATE_EXCHANGE);
    }

    @Bean
    public Queue productPriceUpdateQueueDeadLetter() {
        return QueueBuilder.durable(PRODUCT_PRICE_UPDATE_QUEUE_DEAD_LETTER).build();
    }

    @Bean
    public Binding productPriceUpdateQueueBinding(Queue productPriceUpdateQueue, DirectExchange productPriceUpdateExchange) {
        return BindingBuilder.bind(productPriceUpdateQueue).to(productPriceUpdateExchange).with("");
    }
}
