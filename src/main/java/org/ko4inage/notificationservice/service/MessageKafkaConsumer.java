package org.ko4inage.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.notificationservice.dto.Message;
import org.ko4inage.notificationservice.service.impl.EmailInboxService;
import org.ko4inage.notificationservice.service.impl.PushInboxService;
import org.ko4inage.notificationservice.service.impl.SmsInboxService;
import org.ko4inage.notificationservice.service.impl.TelegramInboxService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class MessageKafkaConsumer {
    private final SmsInboxService smsInboxService;
    private final EmailInboxService emailInboxService;
    private final PushInboxService pushInboxService;
    private final TelegramInboxService telegramInboxService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "sms-events", groupId = "notification-service-v2")
    public void consumeSMS(
            @Payload Message message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        handle(message, key, topic, smsInboxService, ack);
    }

    @KafkaListener(topics = "email-events")
    public void consumeEMAIL(
            @Payload Message message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        handle(message, key, topic, emailInboxService, ack);
    }

    @KafkaListener(topics = "push-events")
    public void consumePUSH(
            @Payload Message message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        handle(message, key, topic, pushInboxService, ack);
    }

    @KafkaListener(topics = "telegram-events")
    public void consumeTG(
            @Payload Message message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        handle(message, key, topic, telegramInboxService, ack);
    }

    private String convertToJson(Message message){
        String json;
        try {
            json = objectMapper.writeValueAsString(message);
        } catch (
                JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    private <T> void handle(
            Message message,
            String key,
            String topic,
            @NonNull BaseNotificationService<T> service,
            Acknowledgment ack
    ) {
        String value = convertToJson(message);

        if (service.existsByKeyAndValue(key, value)) {
            ack.acknowledge();
            return;
        }

        service.create(key, value, topic).ifPresent(saved -> ack.acknowledge());

    }
}
