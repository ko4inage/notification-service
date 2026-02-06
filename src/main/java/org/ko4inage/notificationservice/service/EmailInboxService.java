package org.ko4inage.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.ko4inage.notificationservice.dto.Message;
import org.ko4inage.notificationservice.model.EmailInbox;
import org.ko4inage.notificationservice.repo.EmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class EmailInboxService {

    private static final Logger log = LoggerFactory.getLogger(EmailInboxService.class);
    private final EmailRepository emailRepository;
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

        EmailInbox msgInbox = new EmailInbox();
        msgInbox.setTopic(topic);
        msgInbox.setKey(key);
        msgInbox.setValue(json);
        msgInbox.setProcessed(false);

        emailRepository.save(msgInbox);
        log.info("Сохранено {}: {}", topic, message);
    }
}
