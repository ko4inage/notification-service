package org.ko4inage.notificationservice.repo;

import org.ko4inage.notificationservice.model.EmailInbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EmailRepository extends JpaRepository<EmailInbox, UUID> {

    @Query("""
    select i from EmailInbox i
    where i.processed = false
    order by i.createdAt
    """)
    List<EmailInbox> findBatch(Pageable limit);

    boolean existsByKeyAndValue(String key, String value);

}
