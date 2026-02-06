package org.ko4inage.notificationservice.repo;

import org.ko4inage.notificationservice.model.EmailInbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailRepository extends JpaRepository<EmailInbox, UUID> {
}
