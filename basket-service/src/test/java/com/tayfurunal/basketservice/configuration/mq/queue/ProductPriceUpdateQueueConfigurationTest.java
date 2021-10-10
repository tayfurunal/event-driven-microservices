package com.tayfurunal.basketservice.configuration.mq.queue;

import com.tayfurunal.basketservice.configuration.mq.RabbitMqConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ProductPriceUpdateQueueConfigurationTest {

    @InjectMocks
    private ProductPriceUpdateQueueConfiguration productPriceUpdateQueueConfiguration;

    @Test
    public void it_should_get_queue() {
        // When
        final Queue queue = productPriceUpdateQueueConfiguration.productPriceUpdateQueue();

        // Then
        assertThat(queue.getName()).isEqualTo("basket.service.product.price.update");
        assertThat(queue.isDurable()).isTrue();
        assertThat(queue.getArguments()).containsEntry(RabbitMqConstants.X_DEAD_LETTER_EXCHANGE, "");
        assertThat(queue.getArguments()).containsEntry(RabbitMqConstants.X_DEAD_LETTER_ROUTING_KEY,
                                                       "basket.service.product.price.update.dead-letter");
    }

    @Test
    public void it_should_get_exchange() {
        // When
        final DirectExchange exchange = productPriceUpdateQueueConfiguration.productPriceUpdateExchange();

        // Then
        assertThat(exchange.getName()).isEqualTo("basket.service.product.price.update");
    }

    @Test
    public void it_should_get_queue_dead_letter() {
        // When
        final Queue deadLetterQueue = productPriceUpdateQueueConfiguration.productPriceUpdateQueueDeadLetter();

        // Then
        assertThat(deadLetterQueue.getName()).isEqualTo("basket.service.product.price.update.dead-letter");
        assertThat(deadLetterQueue.isDurable()).isTrue();
    }

    @Test
    public void it_should_get_binding_exchange_to_queue() {
        // When
        final Binding binding = productPriceUpdateQueueConfiguration.productPriceUpdateQueueBinding(
                productPriceUpdateQueueConfiguration.productPriceUpdateQueue(),
                productPriceUpdateQueueConfiguration.productPriceUpdateExchange());

        //Then
        assertThat(binding.getExchange()).isEqualTo("basket.service.product.price.update");
        assertThat(binding.getDestination()).isEqualTo("basket.service.product.price.update");
    }


}