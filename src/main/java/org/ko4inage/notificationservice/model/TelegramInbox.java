package org.ko4inage.notificationservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "telegram_inbox",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"key", "value"})
)
public class TelegramInbox extends AbstractInbox {
}
