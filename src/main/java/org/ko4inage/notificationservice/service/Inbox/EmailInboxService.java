package org.ko4inage.notificationservice.service.Inbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.notificationservice.model.EmailInbox;
import org.ko4inage.notificationservice.repo.EmailRepository;
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
public class EmailInboxService implements BaseInboxService<EmailInbox> {

    //private InboxRepository<EmailInbox> emailRepository;
    private final EmailRepository emailRepository;

    @Override
    @Transactional
    public Optional<EmailInbox> saveEvent(
            String key,
            String message,
            String topic
    ) {
        EmailInbox msgInbox = new EmailInbox();
        msgInbox.setTopic(topic);
        msgInbox.setKey(key);
        msgInbox.setValue(message);
        msgInbox.setProcessed(false);

        try {
            EmailInbox saved = emailRepository.save(msgInbox);
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
        return emailRepository.existsByKeyAndValue(key, value);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAttempt(UUID id) {
        emailRepository.incrementAttempt(id);
    }

    @Override
    @Transactional
    public void setProcessed(UUID id) {
        emailRepository.setProcessedTrue(id);
    }

    @Override
    @Transactional
    public List<EmailInbox> findBatch(Pageable limit) {
        return emailRepository.findBatch(limit);
    }

}
