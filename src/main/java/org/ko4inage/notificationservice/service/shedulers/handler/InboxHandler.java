package org.ko4inage.notificationservice.service.shedulers.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.notificationservice.model.AbstractInbox;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxHandler {

    public void handle(AbstractInbox msg) throws InterruptedException {
        //условная ошибка обработки события
        boolean mistake = false;
        if (mistake) {
            Thread.sleep(3000);
            throw new RuntimeException();
        } else {
            log.info("Обработано событие: Key: {}, Payload: {}, topic: {}", msg.getKey(), msg.getValue(), msg.getTopic());
        }
    }
}
