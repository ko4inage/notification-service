package org.ko4inage.notificationservice.service;

import java.util.Optional;

public interface BaseNotificationService<T> {

    public Optional<T> create(
            String key,
            String message,
            String topic
    );

    public boolean existsByKeyAndValue(String key, String value);

}
