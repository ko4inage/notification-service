package org.ko4inage.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.notificationservice.dto.Message;
import org.ko4inage.notificationservice.service.Inbox.BaseInboxService;
import org.ko4inage.notificationservice.service.Inbox.EmailInboxService;
import org.ko4inage.notificationservice.service.Inbox.PushInboxService;
import org.ko4inage.notificationservice.service.Inbox.SmsInboxService;
import org.ko4inage.notificationservice.service.Inbox.TelegramInboxService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class MessageKafkaConsumer {

    private final SmsInboxService smsInboxService;
    private final EmailInboxService emailInboxService;
    private final PushInboxService pushInboxService;
    private final TelegramInboxService telegramInboxService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "sms-events")
    public void consumeSMS(
            @Payload Message message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        handleEvent(message, key, topic, smsInboxService, ack);
    }

    @KafkaListener(topics = "email-events")
    public void consumeEMAIL(
            @Payload Message message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        handleEvent(message, key, topic, emailInboxService, ack);
    }

    @KafkaListener(topics = "push-events")
    public void consumePUSH(
            @Payload Message message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        handleEvent(message, key, topic, pushInboxService, ack);
    }

    @KafkaListener(topics = "telegram-events")
    public void consumeTG(
            @Payload Message message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack
    ) {
        handleEvent(message, key, topic, telegramInboxService, ack);
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

    private <T> void handleEvent(
            Message message,
            String key,
            String topic,
            @NonNull BaseInboxService<T> service,
            Acknowledgment ack
    ) {
        String value = convertToJson(message);

        if (service.existsByKeyAndValue(key, value)) {
            ack.acknowledge();
            return;
        }

        service.saveEvent(key, value, topic).ifPresent(saved -> ack.acknowledge());
    }
}
