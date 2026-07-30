package br.ufpb.dsc.nexushub.model.people.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usr_notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idnotification")
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "idreceiver", nullable = false)
    private Human receiver;

    @Column(name = "dstitle")
    private String title;

    @Column(name = "dsmessage", nullable = false)
    private String message;

    @Column(name = "dstype")
    private String type = "SYSTEM_NOTICE";

    @Column(name = "dslink")
    private String link;

    @Column(name = "flread", nullable = false)
    private boolean read = false;

    @Column(name = "flsendemail", nullable = false)
    private boolean sendEmail = false;

    @Column(name = "flemailsent", nullable = false)
    private boolean emailSent = false;

    @Column(name = "tscreated", nullable = false, updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();

    public Notification(Human receiver, String title, String message, String type, String link, boolean sendEmail) {
        this.receiver = receiver;
        this.title = title != null && !title.isBlank() ? title.trim() : "Notificação NexusHub";
        this.message = message;
        this.type = type != null && !type.isBlank() ? type.trim() : "SYSTEM_NOTICE";
        this.link = link;
        this.read = false;
        this.sendEmail = sendEmail;
        this.emailSent = false;
        this.createdTime = LocalDateTime.now();
    }

    public Notification(Human receiver, String message) {
        this(receiver, "Notificação", message, "SYSTEM_NOTICE", null, false);
    }

    public void markAsRead() {
        this.read = true;
    }

    public void markEmailSent() {
        this.emailSent = true;
    }
}
