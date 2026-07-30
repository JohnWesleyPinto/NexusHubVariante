package br.ufpb.dsc.nexushub.model.people.service;

import br.ufpb.dsc.nexushub.model.people.domain.Human;
import br.ufpb.dsc.nexushub.model.people.domain.Notification;
import br.ufpb.dsc.nexushub.model.people.repository.HumanRepository;
import br.ufpb.dsc.nexushub.model.people.repository.NotificationRepository;
import br.ufpb.dsc.nexushub.model.shared.service.EmailService;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final HumanRepository humanRepository;
    private final EmailService emailService;

    public NotificationService(NotificationRepository notificationRepository, HumanRepository humanRepository, EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.humanRepository = humanRepository;
        this.emailService = emailService;
    }

    /**
     * Função utilitária centralizada de disparo modular de notificações.
     */
    @Transactional
    public Notification createNotification(UUID userId, String title, String message, String type, String link, boolean sendEmail) {
        Human receiver = humanRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário destinatário não encontrado para notificação."));

        Notification notification = new Notification(receiver, title, message, type, link, sendEmail);
        Notification saved = notificationRepository.save(notification);

        if (sendEmail) {
            triggerFutureEmailWorker(saved);
        }

        return saved;
    }

    public Notification createNotification(UUID userId, String title, String message, String type, String link) {
        return createNotification(userId, title, message, type, link, false);
    }

    @Transactional(readOnly = true)
    public List<Notification> listByUserId(UUID userId) {
        return notificationRepository.findAllByReceiverIdOrderByCreatedTimeDesc(userId);
    }

    @Transactional(readOnly = true)
    public long countUnread(UUID userId) {
        return notificationRepository.countByReceiverIdAndReadFalse(userId);
    }

    @Transactional
    public Notification markAsRead(UUID notificationId, UUID userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notificação não encontrada."));

        if (!notification.getReceiver().getId().equals(userId)) {
            throw new IllegalStateException("Permissão negada para alterar esta notificação.");
        }

        notification.markAsRead();
        return notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        notificationRepository.markAllAsReadByReceiverId(userId);
    }

    /**
     * Trabalhador modular assíncrono de envio de e-mails para notificações.
     */
    private void triggerFutureEmailWorker(Notification notification) {
        if (notification.getReceiver() != null && notification.getReceiver().getEmail() != null) {
            String to = notification.getReceiver().getEmail();
            String title = notification.getTitle();
            String msg = notification.getMessage();

            String html = """
                <!DOCTYPE html>
                <html>
                <head>
                  <style>
                    body { font-family: 'Plus Jakarta Sans', Arial, sans-serif; background-color: #f1f5f9; color: #0b1d3a; margin: 0; padding: 20px; }
                    .container { max-width: 540px; margin: 0 auto; background: #ffffff; border-radius: 16px; padding: 32px; box-shadow: 0 4px 16px rgba(11,29,58,0.08); border: 1px solid #e2e8f0; }
                    .header { text-align: center; padding-bottom: 16px; border-bottom: 1px solid #e2e8f0; }
                    .logo { font-size: 22px; font-weight: 800; color: #0b1d3a; }
                    .logo span { color: #3b6ef4; }
                    .content { padding: 20px 0; font-size: 14px; line-height: 1.6; }
                    .notif-box { background: #f8fafc; border-left: 4px solid #3b6ef4; padding: 16px; border-radius: 8px; margin: 16px 0; }
                    .notif-title { font-weight: 800; font-size: 15px; color: #0b1d3a; margin-bottom: 6px; }
                    .footer { text-align: center; font-size: 12px; color: #64748b; margin-top: 20px; border-top: 1px solid #e2e8f0; padding-top: 16px; }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <div class="header">
                      <div class="logo">NEXUS<span>HUB</span></div>
                    </div>
                    <div class="content">
                      <p>Olá, <strong>%s</strong>!</p>
                      <p>Você tem uma nova notificação em sua conta no NexusHub:</p>
                      <div class="notif-box">
                        <div class="notif-title">%s</div>
                        <div>%s</div>
                      </div>
                    </div>
                    <div class="footer">
                      <p>NexusHub — Plataforma Acadêmica UFPB</p>
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(notification.getReceiver().getName(), title, msg);

            emailService.sendEmail(to, "🔔 NexusHub: " + title, html);
            notification.markEmailSent();
            notificationRepository.save(notification);
        }
    }
}
