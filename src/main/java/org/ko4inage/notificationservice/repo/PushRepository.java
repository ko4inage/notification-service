package org.ko4inage.notificationservice.repo;

import org.ko4inage.notificationservice.model.PushInbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PushRepository extends JpaRepository<PushInbox, UUID> {

    @Query("""
    select i from PushInbox i
    where i.processed = false
    order by i.createdAt
    """)
    List<PushInbox> findBatch(Pageable limit);

    boolean existsByKeyAndValue(String key, String value);

}
