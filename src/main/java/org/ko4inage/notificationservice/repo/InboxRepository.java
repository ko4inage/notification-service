package org.ko4inage.notificationservice.repo;

import org.ko4inage.notificationservice.model.AbstractInbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

@NoRepositoryBean
public interface InboxRepository<T extends AbstractInbox> extends JpaRepository<T, UUID> {

    @Query("""
    select i from #{#entityName} i
    where i.processed = false
    order by i.createdAt
    """)
    List<T> findBatch(Pageable limit);

    @Query("""
    select count(i) > 0 from #{#entityName} i
    where i.key = :key and i.value = :value
    """)
    boolean existsByKeyAndValue(
            @Param("key") String key,
            @Param("value") String value
    );

    @Modifying
    @Query("""
            update #{#entityName} n
            set n.attempt = n.attempt + 1
            where n.id = :id
    """)
    void incrementAttempt(@Param("id") UUID id);

    @Modifying
    @Query("""
            update #{#entityName} n
            set n.processed = true
            where n.id = :id
    """)
    void setProcessedTrue(@Param("id") UUID id);

}
