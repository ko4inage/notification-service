package org.ko4inage.notificationservice.service;

import lombok.AllArgsConstructor;
import org.ko4inage.notificationservice.dto.Message;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class MessageKafkaConsumer {
    private final SmsInboxService smsInboxService;
    private final EmailInboxService emailInboxService;
    private final PushInboxService pushInboxService;
    private final TelegramInboxService telegramInboxService;

    @Transactional
    @KafkaListener(topics = "sms-events")
    public void consumeSMS(
            @Payload Message message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        smsInboxService.create(key, message, topic);
        ack.acknowledge();
    }

    @Transactional
    @KafkaListener(topics = "email-events")
    public void consumeEMAIL(
            @Payload Message message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        emailInboxService.create(key, message, topic);
        ack.acknowledge();
    }

    @Transactional
    @KafkaListener(topics = "push-events")
    public void consumePUSH(
            @Payload Message message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        pushInboxService.create(key, message, topic);
        ack.acknowledge();
    }

    @Transactional
    @KafkaListener(topics = "telegram-events")
    public void consumeTG(
            @Payload Message message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        telegramInboxService.create(key, message, topic);
        ack.acknowledge();
    }
}
