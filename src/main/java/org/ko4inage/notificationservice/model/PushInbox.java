package org.ko4inage.notificationservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "push_inbox",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"key", "value"})
)
public class PushInbox {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String value;

    @Column(nullable = false)
    private boolean processed;

    @Column(nullable = false)
    @Builder.Default
    private int attempt = 1;
}
