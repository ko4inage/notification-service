package org.ko4inage.notificationservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.notificationservice.model.SmsInbox;
import org.ko4inage.notificationservice.repo.SmsRepository;
import org.ko4inage.notificationservice.service.BaseNotificationService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class SmsInboxService implements BaseNotificationService<SmsInbox> {

    private final SmsRepository smsRepository;

    @Override
    @Transactional
    public Optional<SmsInbox> create(
            String key,
            String message,
            String topic
    ) {
        SmsInbox msgInbox = new SmsInbox();
        msgInbox.setTopic(topic);
        msgInbox.setKey(key);
        msgInbox.setValue(message);
        msgInbox.setProcessed(false);

        try {
            SmsInbox saved = smsRepository.save(msgInbox);
            log.info("Сохранено {}: {}", topic, message);
            return Optional.of(saved);
        } catch (DataIntegrityViolationException e) {
            log.info("Сообщение было сохранено ранее {}: {}", topic, message);
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByKeyAndValue(String key, String value){
        return smsRepository.existsByKeyAndValue(key, value);
    }
}
