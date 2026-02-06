package org.ko4inage.notificationservice.repo;

import org.ko4inage.notificationservice.model.TelegramInbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TelegramRepository extends JpaRepository<TelegramInbox, UUID> {
}
