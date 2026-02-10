package org.ko4inage.notificationservice.service.shedulers;

import lombok.RequiredArgsConstructor;
import org.ko4inage.notificationservice.config.InboxProperties;
import org.ko4inage.notificationservice.model.EmailInbox;
import org.ko4inage.notificationservice.service.Inbox.EmailInboxService;
import org.ko4inage.notificationservice.service.shedulers.handler.InboxHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailScheduler {

    private final EmailInboxService emailInboxService;
    private final InboxProperties properties;
    private final InboxHandler inboxHandler;

    @Scheduled(fixedDelayString = "#{@inboxProperties.delayMs}")
    public void processPendingMessages() {
        Pageable limit = PageRequest.of(0, properties.getBatchSize());
        List<EmailInbox> messages = emailInboxService.findBatch(limit);

        for(EmailInbox msg : messages){
            try {
                inboxHandler.handle(msg);
                emailInboxService.setProcessed(msg.getId());
            } catch (RuntimeException e) {
                emailInboxService.incrementAttempt(msg.getId());
            }
        }
    }
}
