package org.ko4inage.notificationservice.service;

import org.ko4inage.notificationservice.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageKafkaConsumer {
    public static final Logger log = LoggerFactory.getLogger(MessageKafkaConsumer.class);

    @KafkaListener(
            topics = "sms-events"
    )
    public void consumeMessage(Message message){
        log.info("Получено сообщение: {}", message);
    }
}
