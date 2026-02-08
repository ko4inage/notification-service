package org.ko4inage.notificationservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.notificationservice.model.PushInbox;
import org.ko4inage.notificationservice.model.TelegramInbox;
import org.ko4inage.notificationservice.repo.TelegramRepository;
import org.ko4inage.notificationservice.service.BaseNotificationService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class TelegramInboxService implements BaseNotificationService<TelegramInbox> {

    private final TelegramRepository telegramRepository;

    @Override
    @Transactional
    public Optional<TelegramInbox> create(
            String key,
            String message,
            String topic
    ) {
        TelegramInbox msgInbox = new TelegramInbox();
        msgInbox.setTopic(topic);
        msgInbox.setKey(key);
        msgInbox.setValue(message);
        msgInbox.setProcessed(false);

        try {
            TelegramInbox saved = telegramRepository.save(msgInbox);
            log.info("Сохранено {}: {}", topic, message);
            return Optional.of(saved);
        } catch (DataIntegrityViolationException e) {
            log.info("Сообщение было сохранено ранее {}: {}", topic, message);
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByKeyAndValue(String key, String value){
        return telegramRepository.existsByKeyAndValue(key, value);
    }
}
