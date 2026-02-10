package org.ko4inage.notificationservice.service.shedulers.handler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.notificationservice.model.AbstractInbox;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxHandler {

    @Transactional
    public void handle(AbstractInbox msg) {
        log.info("Обработано событие: Key: {}, Payload: {}, topic: {}", msg.getKey(), msg.getValue(), msg.getTopic());
    }

}
