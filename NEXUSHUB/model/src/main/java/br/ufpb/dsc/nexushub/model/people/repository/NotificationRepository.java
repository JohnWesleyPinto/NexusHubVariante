package br.ufpb.dsc.nexushub.model.people.repository;

import br.ufpb.dsc.nexushub.model.people.domain.Notification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByReceiverIdAndReadOrderByCreatedTimeDesc(UUID receiverId, boolean read);
    List<Notification> findAllByReceiverIdOrderByCreatedTimeDesc(UUID receiverId);
    long countByReceiverIdAndReadFalse(UUID receiverId);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.receiver.id = :receiverId AND n.read = false")
    void markAllAsReadByReceiverId(@Param("receiverId") UUID receiverId);
}
