package br.ufpb.dsc.nexushub.controller;

import br.ufpb.dsc.nexushub.model.identity.domain.User;
import br.ufpb.dsc.nexushub.model.identity.service.IdentityService;
import br.ufpb.dsc.nexushub.model.people.domain.Notification;
import br.ufpb.dsc.nexushub.model.people.service.NotificationService;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationRestController {

    private final NotificationService notificationService;
    private final IdentityService identityService;

    public NotificationRestController(NotificationService notificationService, IdentityService identityService) {
        this.notificationService = notificationService;
        this.identityService = identityService;
    }

    @GetMapping
    public ResponseEntity<NotificationListResponse> getNotifications(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        User currentUser = identityService.findByEmail(principal.getName());
        UUID humanId = currentUser.getHuman().getId();

        List<NotificationResponseDto> list = notificationService.listByUserId(humanId)
                .stream()
                .map(NotificationResponseDto::from)
                .toList();

        long unreadCount = notificationService.countUnread(humanId);

        return ResponseEntity.ok(new NotificationListResponse(list, unreadCount));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable UUID id, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        User currentUser = identityService.findByEmail(principal.getName());
        try {
            Notification updated = notificationService.markAsRead(id, currentUser.getHuman().getId());
            return ResponseEntity.ok(NotificationResponseDto.from(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(new ErrorDto(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(new ErrorDto(e.getMessage()));
        }
    }

    @PatchMapping("/read-all")
    public ResponseEntity<?> markAllAsRead(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        User currentUser = identityService.findByEmail(principal.getName());
        notificationService.markAllAsRead(currentUser.getHuman().getId());
        return ResponseEntity.ok().build();
    }

    public record NotificationResponseDto(
        UUID id,
        String title,
        String message,
        String type,
        String link,
        boolean isRead,
        boolean sendEmail,
        boolean emailSent,
        LocalDateTime createdAt
    ) {
        public static NotificationResponseDto from(Notification n) {
            return new NotificationResponseDto(
                n.getId(),
                n.getTitle(),
                n.getMessage(),
                n.getType(),
                n.getLink(),
                n.isRead(),
                n.isSendEmail(),
                n.isEmailSent(),
                n.getCreatedTime()
            );
        }
    }

    public record NotificationListResponse(
        List<NotificationResponseDto> notifications,
        long unreadCount
    ) {}

    public record ErrorDto(String message) {}
}
