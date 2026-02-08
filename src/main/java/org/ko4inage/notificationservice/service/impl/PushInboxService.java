package org.ko4inage.notificationservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.notificationservice.model.EmailInbox;
import org.ko4inage.notificationservice.model.PushInbox;
import org.ko4inage.notificationservice.repo.PushRepository;
import org.ko4inage.notificationservice.service.BaseNotificationService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class PushInboxService implements BaseNotificationService<PushInbox> {

    private final PushRepository pushRepository;

    @Override
    @Transactional
    public Optional<PushInbox> create(
            String key,
            String message,
            String topic
    ) {
        PushInbox msgInbox = new PushInbox();
        msgInbox.setTopic(topic);
        msgInbox.setKey(key);
        msgInbox.setValue(message);
        msgInbox.setProcessed(false);

        try {
            PushInbox saved = pushRepository.save(msgInbox);
            log.info("Сохранено {}: {}", topic, message);
            return Optional.of(saved);
        } catch (DataIntegrityViolationException e) {
            log.info("Сообщение было сохранено ранее {}: {}", topic, message);
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByKeyAndValue(String key, String value){
        return pushRepository.existsByKeyAndValue(key, value);
    }
}
