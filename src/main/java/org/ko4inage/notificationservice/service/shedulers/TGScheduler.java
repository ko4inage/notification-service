package org.ko4inage.notificationservice.service.shedulers;

import lombok.RequiredArgsConstructor;
import org.ko4inage.notificationservice.config.InboxProperties;
import org.ko4inage.notificationservice.model.TelegramInbox;
import org.ko4inage.notificationservice.service.Inbox.TelegramInboxService;
import org.ko4inage.notificationservice.service.shedulers.handler.InboxHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TGScheduler {

    private final TelegramInboxService telegramInboxService;
    private final InboxProperties properties;
    private final InboxHandler inboxHandler;

    @Scheduled(fixedDelayString = "#{@inboxProperties.delayMs}")
    public void processPendingMessages() {
        Pageable limit = PageRequest.of(0, properties.getBatchSize());
        List<TelegramInbox> messages = telegramInboxService.findBatch(limit);

        for(TelegramInbox msg : messages){
            try {
                inboxHandler.handle(msg);
                telegramInboxService.setProcessed(msg.getId());
            } catch (RuntimeException | InterruptedException e) {
                telegramInboxService.incrementAttempt(msg.getId());
            }
        }
    }

}
