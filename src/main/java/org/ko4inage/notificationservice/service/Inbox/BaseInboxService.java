package org.ko4inage.notificationservice.service.Inbox;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseInboxService<T> {

    Optional<T> saveEvent(
            String key,
            String message,
            String topic
    );

    boolean existsByKeyAndValue(String key, String value);

    void incrementAttempt(UUID id);

    void setProcessed(UUID id);

    List<T> findBatch(Pageable limit);

}
