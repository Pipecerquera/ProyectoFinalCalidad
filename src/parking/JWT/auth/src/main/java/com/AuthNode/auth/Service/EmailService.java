package com.AuthNode.auth.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Envía el correo de verificación al usuario recién registrado.
     * El link lleva al endpoint del backend que activa la cuenta.
     */
    public void sendVerificationEmail(String toEmail, String fullname, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("✅ Confirma tu cuenta - Parqueadero");

            String verificationLink = baseUrl + "/api/auth/verify?token=" + token;

            String htmlContent = """
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;
                                background: #f9f9f9; padding: 30px; border-radius: 10px;">
                        <div style="background: #1a73e8; padding: 20px; border-radius: 8px 8px 0 0; text-align: center;">
                            <h1 style="color: white; margin: 0;">🚗 Parqueadero</h1>
                        </div>
                        <div style="background: white; padding: 30px; border-radius: 0 0 8px 8px;">
                            <h2 style="color: #333;">¡Hola, %s!</h2>
                            <p style="color: #555; font-size: 16px;">
                                Gracias por registrarte. Para activar tu cuenta haz clic en el botón:
                            </p>
                            <div style="text-align: center; margin: 30px 0;">
                                <a href="%s"
                                   style="background: #1a73e8; color: white; padding: 14px 32px;
                                          border-radius: 6px; text-decoration: none; font-size: 16px;
                                          font-weight: bold;">
                                    Verificar mi cuenta
                                </a>
                            </div>
                            <p style="color: #888; font-size: 13px;">
                                Si no creaste esta cuenta, ignora este mensaje.<br>
                                Este enlace expira en <strong>24 horas</strong>.
                            </p>
                            <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                            <p style="color: #aaa; font-size: 12px; text-align: center;">
                                Sistema de Parqueadero — Correo automático, no responder.
                            </p>
                        </div>
                    </div>
                    """.formatted(fullname, verificationLink);

            helper.setText(htmlContent, true); // true = es HTML
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo de verificación: " + e.getMessage());
        }
    }
}