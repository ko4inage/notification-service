package org.ko4inage.notificationservice.service.shedulers;

import lombok.RequiredArgsConstructor;
import org.ko4inage.notificationservice.config.InboxProperties;
import org.ko4inage.notificationservice.model.SmsInbox;
import org.ko4inage.notificationservice.service.Inbox.SmsInboxService;
import org.ko4inage.notificationservice.service.shedulers.handler.InboxHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SMSScheduler {

    private final SmsInboxService smsInboxService;
    private final InboxProperties properties;
    private final InboxHandler inboxHandler;

    @Scheduled(fixedDelayString = "#{@inboxProperties.delayMs}")
    public void processPendingMessages() {
        Pageable limit = PageRequest.of(0, properties.getBatchSize());
        List<SmsInbox> messages = smsInboxService.findBatch(limit);

        for(SmsInbox msg : messages){
            try {
                inboxHandler.handle(msg);
                smsInboxService.setProcessed(msg.getId());
            } catch (RuntimeException e) {
                smsInboxService.incrementAttempt(msg.getId());
            }
        }
    }

}
