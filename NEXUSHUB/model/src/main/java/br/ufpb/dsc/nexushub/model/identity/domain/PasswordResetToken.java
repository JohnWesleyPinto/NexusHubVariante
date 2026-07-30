package br.ufpb.dsc.nexushub.model.identity.domain;

import br.ufpb.dsc.nexushub.model.shared.domain.AuditableEntity;
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
@Table(name = "usr_password_reset_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idtoken")
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "iduser", nullable = false)
    private User user;

    @Column(name = "dstoken", nullable = false, unique = true)
    private String token;

    @Column(name = "tsexpiration", nullable = false)
    private LocalDateTime expirationTime;

    @Column(name = "flused", nullable = false)
    private boolean used = false;

    @Column(name = "tscreated", nullable = false, updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();

    public PasswordResetToken(User user, String token, int expirationMinutes) {
        this.user = user;
        this.token = token;
        this.expirationTime = LocalDateTime.now().plusMinutes(expirationMinutes);
        this.used = false;
        this.createdTime = LocalDateTime.now();
    }

    public boolean isValid() {
        return !used && LocalDateTime.now().isBefore(expirationTime);
    }

    public void markUsed() {
        this.used = true;
    }
}
