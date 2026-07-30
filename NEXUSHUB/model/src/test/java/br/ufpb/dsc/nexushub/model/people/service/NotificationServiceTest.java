package br.ufpb.dsc.nexushub.model.people.service;

import br.ufpb.dsc.nexushub.model.people.domain.Human;
import br.ufpb.dsc.nexushub.model.people.domain.Notification;
import br.ufpb.dsc.nexushub.model.people.repository.HumanRepository;
import br.ufpb.dsc.nexushub.model.people.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.ufpb.dsc.nexushub.model.shared.service.EmailService;

class NotificationServiceTest {

    private NotificationRepository notificationRepository;
    private HumanRepository humanRepository;
    private EmailService emailService;
    private NotificationService notificationService;

    private UUID userId;
    private Human dummyHuman;

    @BeforeEach
    void setUp() {
        notificationRepository = mock(NotificationRepository.class);
        humanRepository = mock(HumanRepository.class);
        emailService = mock(EmailService.class);
        notificationService = new NotificationService(notificationRepository, humanRepository, emailService);

        userId = UUID.randomUUID();
        dummyHuman = mock(Human.class);
        when(dummyHuman.getId()).thenReturn(userId);
        when(humanRepository.findById(userId)).thenReturn(Optional.of(dummyHuman));
    }

    @Test
    void testCreateNotification_Success() {
        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> i.getArgument(0));

        Notification n = notificationService.createNotification(
                userId,
                "Nova Candidatura Recebida!",
                "João candidatou-se",
                "OPPORTUNITY_APPLICATION",
                "/oportunidades",
                true
        );

        assertNotNull(n);
        assertEquals("Nova Candidatura Recebida!", n.getTitle());
        assertEquals("João candidatou-se", n.getMessage());
        assertEquals("OPPORTUNITY_APPLICATION", n.getType());
        assertEquals("/oportunidades", n.getLink());
        assertTrue(n.isSendEmail());
        assertFalse(n.isRead());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testCountUnread() {
        when(notificationRepository.countByReceiverIdAndReadFalse(userId)).thenReturn(3L);
        long count = notificationService.countUnread(userId);
        assertEquals(3L, count);
    }

    @Test
    void testMarkAsRead() {
        Notification dummyNotification = new Notification(dummyHuman, "Titulo", "Msg", "SYSTEM_NOTICE", null, false);
        UUID notifId = UUID.randomUUID();

        when(notificationRepository.findById(notifId)).thenReturn(Optional.of(dummyNotification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> i.getArgument(0));

        Notification updated = notificationService.markAsRead(notifId, userId);
        assertTrue(updated.isRead());
    }

    @Test
    void testMarkAllAsRead() {
        notificationService.markAllAsRead(userId);
        verify(notificationRepository, times(1)).markAllAsReadByReceiverId(userId);
    }
}
