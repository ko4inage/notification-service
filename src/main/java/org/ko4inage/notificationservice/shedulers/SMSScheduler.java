package org.ko4inage.notificationservice.shedulers;

import lombok.RequiredArgsConstructor;
import org.ko4inage.notificationservice.config.InboxProperties;
import org.ko4inage.notificationservice.model.SmsInbox;
import org.ko4inage.notificationservice.repo.SmsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SMSScheduler {

    private static final Logger log = LoggerFactory.getLogger(EmailScheduler.class);
    private final SmsRepository smsRepository;
    private final InboxProperties properties;

    @Scheduled(fixedDelayString = "#{@inboxProperties.delayMs}")
    @Transactional
    public void processPendingMessages() {
        Pageable limit = PageRequest.of(0, properties.getBatchSize());
        List<SmsInbox> messages = smsRepository.findBatch(limit);

        if(messages.isEmpty()){
            return;
        }

        for(SmsInbox msg : messages){
            try{
                log.info("Обработано событие: Key: {}, Payload: {}, topic: {}", msg.getKey(), msg.getValue(), msg.getTopic());
                msg.setProcessed(true);
            } catch (RuntimeException e) {
                msg.setAttempt(msg.getAttempt() + 1);
            }
        }
    }

}
