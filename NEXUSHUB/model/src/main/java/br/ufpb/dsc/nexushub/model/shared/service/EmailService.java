package br.ufpb.dsc.nexushub.model.shared.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@nexushub.ufpb.br}")
    private String fromEmail;

    public EmailService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String bodyHtml) {
        if (to == null || to.isBlank()) {
            log.warn("[EMAIL_SERVICE] Destinatário nulo ou vazio. Cancelando envio.");
            return;
        }

        log.info("[EMAIL_DISPATCH] Enviando e-mail para: {} | Assunto: {}", to, subject);

        if (mailSender == null) {
            log.warn("[EMAIL_SERVICE] JavaMailSender não configurado. Exibindo e-mail no log:\nTO: {}\nSUBJECT: {}\nBODY:\n{}", to, subject, bodyHtml);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(bodyHtml, true);

            mailSender.send(message);
            log.info("[EMAIL_SERVICE] E-mail enviado com sucesso para {}", to);
        } catch (MessagingException e) {
            log.error("[EMAIL_SERVICE] Erro ao enviar e-mail para {}: {}", to, e.getMessage(), e);
        } catch (Exception e) {
            log.error("[EMAIL_SERVICE] Falha inesperada no servidor SMTP ao enviar e-mail para {}: {}", to, e.getMessage(), e);
        }
    }

    public void sendPasswordResetEmail(String toEmail, String userName, String resetToken) {
        String resetLink = "http://localhost:4200/esqueci-senha?token=" + resetToken;
        String subject = "🔑 NexusHub - Solicitação de Redefinição de Senha";
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
              <style>
                body { font-family: 'Plus Jakarta Sans', Arial, sans-serif; background-color: #f1f5f9; color: #0b1d3a; margin: 0; padding: 20px; }
                .container { max-width: 540px; margin: 0 auto; background: #ffffff; border-radius: 16px; padding: 32px; box-shadow: 0 4px 16px rgba(11,29,58,0.08); border: 1px solid #e2e8f0; }
                .header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #e2e8f0; }
                .logo { font-size: 24px; font-weight: 800; color: #0b1d3a; }
                .logo span { color: #3b6ef4; }
                .content { padding: 24px 0; font-size: 15px; line-height: 1.6; }
                .btn-reset { display: inline-block; background-color: #3b6ef4; color: #ffffff !important; padding: 14px 28px; font-weight: 800; font-size: 14px; text-decoration: none; border-radius: 30px; margin: 20px 0; box-shadow: 0 4px 12px rgba(59,110,244,0.25); }
                .token-box { background: #f8fafc; border: 1px solid #cbd5e1; padding: 12px; border-radius: 8px; font-family: monospace; font-size: 13px; color: #475569; word-break: break-all; margin-top: 12px; }
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
                  <p>Recebemos uma solicitação para redefinir a senha da sua conta no <strong>NexusHub</strong>.</p>
                  <p>Clique no botão abaixo para cadastrar sua nova senha com segurança:</p>
                  <div style="text-align: center;">
                    <a href="%s" class="btn-reset">Redefinir Minha Senha</a>
                  </div>
                  <p>Este link possui um token único e é válido por <strong>30 minutos</strong>.</p>
                  <p>Se você não fez essa solicitação, pode ignorar este e-mail com segurança.</p>
                  <div class="token-box">Ou acesse direto: %s</div>
                </div>
                <div class="footer">
                  <p>NexusHub — Plataforma Acadêmica UFPB</p>
                  <p>Este é um e-mail automático. Por favor, não responda.</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(userName, resetLink, resetLink);

        sendEmail(toEmail, subject, htmlContent);
    }
}
