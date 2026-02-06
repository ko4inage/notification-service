package org.ko4inage.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.ko4inage.notificationservice.dto.Message;
import org.ko4inage.notificationservice.model.SmsInbox;
import org.ko4inage.notificationservice.repo.SmsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SmsInboxService {

    private static final Logger log = LoggerFactory.getLogger(SmsInboxService.class);
    private final SmsRepository smsRepository;
    private final ObjectMapper objectMapper;

    public void create(
            String key,
            Message message,
            String topic
    ) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        SmsInbox msgInbox = new SmsInbox();
        msgInbox.setTopic(topic);
        msgInbox.setKey(key);
        msgInbox.setValue(json);
        msgInbox.setProcessed(false);

        smsRepository.save(msgInbox);
        log.info("Сохранено {}: {}", topic, message);
    }
}
