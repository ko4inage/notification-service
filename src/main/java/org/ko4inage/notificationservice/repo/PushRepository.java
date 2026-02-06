package org.ko4inage.notificationservice.repo;

import org.ko4inage.notificationservice.model.PushInbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PushRepository extends JpaRepository<PushInbox, UUID> {
}
