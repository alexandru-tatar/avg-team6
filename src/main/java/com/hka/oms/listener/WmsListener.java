package com.hka.oms.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class WmsListener {

    private static final Logger logger = LoggerFactory.getLogger(WmsListener.class);

    @RabbitListener(queues = "status.queue")
    public void receiveStatus(String statusMessage) {
        logger.info("Received WMS status update: {}", statusMessage);
    }
}
