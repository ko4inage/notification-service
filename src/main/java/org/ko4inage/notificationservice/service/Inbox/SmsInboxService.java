package org.ko4inage.notificationservice.service.Inbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.notificationservice.model.SmsInbox;
import org.ko4inage.notificationservice.repo.SmsRepository;
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
public class SmsInboxService implements BaseInboxService<SmsInbox> {

    private final SmsRepository smsRepository;

    @Override
    @Transactional
    public Optional<SmsInbox> saveEvent(
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
    @Transactional
    public boolean existsByKeyAndValue(String key, String value){
        return smsRepository.existsByKeyAndValue(key, value);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAttempt(UUID id) {
        smsRepository.incrementAttempt(id);
    }

    @Override
    @Transactional
    public void setProcessed(UUID id) {
        smsRepository.setProcessedTrue(id);
    }

    @Override
    @Transactional
    public List<SmsInbox> findBatch(Pageable limit) {
        return smsRepository.findBatch(limit);
    }

}
