package com.hka.oms.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hka.oms.service.OrderCreationResult;

@Component
public class WmsPublisher {

    private static final Logger logger = LoggerFactory.getLogger(WmsPublisher.class);
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public WmsPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishOrderCreated(OrderCreationResult result) {
        String payload = serializeResult(result);
        rabbitTemplate.convertAndSend("orders.queue", payload);
        logger.info("Sent order-created payload for {}: {}", result.order().getOrderId(), payload);
    }

    private String serializeResult(OrderCreationResult result) {
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize order " + result.order().getOrderId(), e);
        }
    }
}
