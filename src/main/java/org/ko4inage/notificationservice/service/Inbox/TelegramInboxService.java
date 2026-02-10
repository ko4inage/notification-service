package org.ko4inage.notificationservice.service.Inbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.notificationservice.model.TelegramInbox;
import org.ko4inage.notificationservice.repo.TelegramRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class TelegramInboxService implements BaseInboxService<TelegramInbox> {

    private final TelegramRepository telegramRepository;

    @Override
    @Transactional
    public Optional<TelegramInbox> saveEvent(
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
    @Transactional
    public boolean existsByKeyAndValue(String key, String value){
        return telegramRepository.existsByKeyAndValue(key, value);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAttempt(UUID id) {
        telegramRepository.incrementAttempt(id);
    }

    @Override
    @Transactional
    public void setProcessed(UUID id) {
        telegramRepository.setProcessedTrue(id);
    }

    @Override
    @Transactional
    public List<TelegramInbox> findBatch(Pageable limit) {
        return telegramRepository.findBatch(limit);
    }

}
