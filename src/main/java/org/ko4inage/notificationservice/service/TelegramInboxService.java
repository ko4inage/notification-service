package org.ko4inage.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.ko4inage.notificationservice.dto.Message;
import org.ko4inage.notificationservice.model.TelegramInbox;
import org.ko4inage.notificationservice.repo.TelegramRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class TelegramInboxService {

    private static final Logger log = LoggerFactory.getLogger(TelegramInboxService.class);
    private final TelegramRepository telegramRepository;
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

        TelegramInbox msgInbox = new TelegramInbox();
        msgInbox.setTopic(topic);
        msgInbox.setKey(key);
        msgInbox.setValue(json);
        msgInbox.setProcessed(false);

        telegramRepository.save(msgInbox);
        log.info("Сохранено {}: {}", topic, message);
    }
}
