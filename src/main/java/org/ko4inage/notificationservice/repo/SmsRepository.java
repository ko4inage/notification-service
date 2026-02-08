package org.ko4inage.notificationservice.repo;

import org.ko4inage.notificationservice.model.SmsInbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SmsRepository extends JpaRepository<SmsInbox, UUID> {

    @Query("""
    select i from SmsInbox i
    where i.processed = false
    order by i.createdAt
    """)
    List<SmsInbox> findBatch(Pageable limit);

    boolean existsByKeyAndValue(String key, String value);

}
