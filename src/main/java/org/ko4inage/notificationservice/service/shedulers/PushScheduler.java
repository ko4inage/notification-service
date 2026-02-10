package org.ko4inage.notificationservice.service.shedulers;

import lombok.RequiredArgsConstructor;
import org.ko4inage.notificationservice.config.InboxProperties;
import org.ko4inage.notificationservice.model.PushInbox;
import org.ko4inage.notificationservice.service.Inbox.PushInboxService;
import org.ko4inage.notificationservice.service.shedulers.handler.InboxHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PushScheduler {

    private final PushInboxService pushInboxService;
    private final InboxProperties properties;
    private final InboxHandler inboxHandler;

    @Scheduled(fixedDelayString = "#{@inboxProperties.delayMs}")
    public void processPendingMessages() {
        Pageable limit = PageRequest.of(0, properties.getBatchSize());
        List<PushInbox> messages = pushInboxService.findBatch(limit);

        for(PushInbox msg : messages){
            try {
                inboxHandler.handle(msg);
                pushInboxService.setProcessed(msg.getId());
            } catch (RuntimeException | InterruptedException e) {
                pushInboxService.incrementAttempt(msg.getId());
            }
        }
    }

}
