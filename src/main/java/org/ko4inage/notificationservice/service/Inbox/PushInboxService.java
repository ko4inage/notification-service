package org.ko4inage.notificationservice.service.Inbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.notificationservice.model.PushInbox;
import org.ko4inage.notificationservice.repo.PushRepository;
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
public class PushInboxService implements BaseInboxService<PushInbox> {

    private final PushRepository pushRepository;

    @Override
    @Transactional
    public Optional<PushInbox> saveEvent(
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
    @Transactional
    public boolean existsByKeyAndValue(String key, String value){
        return pushRepository.existsByKeyAndValue(key, value);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAttempt(UUID id) {
        pushRepository.incrementAttempt(id);
    }



    @Override
    @Transactional
    public void setProcessed(UUID id) {
        pushRepository.setProcessedTrue(id);
    }

    @Override
    @Transactional
    public List<PushInbox> findBatch(Pageable limit) {
        return pushRepository.findBatch(limit);
    }

}
