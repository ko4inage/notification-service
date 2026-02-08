package org.ko4inage.notificationservice.repo;

import org.ko4inage.notificationservice.model.TelegramInbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TelegramRepository extends JpaRepository<TelegramInbox, UUID> {

    @Query("""
    select i from TelegramInbox i
    where i.processed = false
    order by i.createdAt
    """)
    List<TelegramInbox> findBatch(Pageable limit);

    boolean existsByKeyAndValue(String key, String value);

}
