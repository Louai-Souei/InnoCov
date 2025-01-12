package covoiturage.project.InnoCov.service.serviceInterface;

import covoiturage.project.InnoCov.entity.Route;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String prenom, String nom) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String subject = "Validation de l'adresse e-mail";
            String htmlContent = """
              <html>
                  <head>
                    <style>
                      body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        margin: 0;
                        padding: 0;
                      }
                      .container {
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                        background-color: #fff;
                        border-radius: 10px;
                        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                      }
                      .header {
                        text-align: center;
                        margin-bottom: 20px;
                      }
                      .button {
                        display: inline-block;
                        padding: 10px 20px;
                        background-color: #007bff;
                        color: white;
                        text-decoration: none;
                        border-radius: 5px;
                      }
                      .footer {
                        margin-top: 20px;
                        text-align: center;
                      }
                    </style>
                  </head>
                  <body>
                    <div class="container">
                      <div class="header">
                        <h2>Bienvenue chez Nous !</h2>
                      </div>
                      <p>Bonjour %s %s,</p>
                      <p>Merci de vous être inscrit. Veuillez cliquer sur le bouton ci-dessous pour valider votre adresse e-mail :</p>
                      <div style="text-align: center;">
                        <a href="http://localhost:4200/email-verified" class="button">Valider votre e-mail</a>
                      </div>
                      <p>Le lien de validation est valide pour trois jours. Si le lien expire, veuillez créer un nouveau compte et valider votre e-mail avec le nouveau lien qui vous sera envoyé par e-mail.</p>
                      <div class="footer">
                        <p>Cordialement,<br>L'Équipe Technique</p>
                      </div>
                    </div>
                  </body>
              </html>
            """.formatted(prenom, nom);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom("votre.email@gmail.com");

            mailSender.send(mimeMessage);
            System.out.println("Email envoyé à " + toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'e-mail", e);
        }
    }

    public void sendRejectReservationEmail(String toEmail, String prenom, String nom, Route route) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String subject = "Annulation de réservation";
            String htmlContent = """
              <html>
                  <head>
                    <style>
                      body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        margin: 0;
                        padding: 0;
                      }
                      .container {
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                        background-color: #fff;
                        border-radius: 10px;
                        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                      }
                      .header {
                        text-align: center;
                        margin-bottom: 20px;
                      }
                      .footer {
                        margin-top: 20px;
                        text-align: center;
                      }
                      .details {
                        margin: 20px 0;
                        padding: 10px;
                        background-color: #f8d7da;
                        color: #721c24;
                        border: 1px solid #f5c6cb;
                        border-radius: 5px;
                      }
                    </style>
                  </head>
                  <body>
                    <div class="container">
                      <div class="header">
                        <h2>Annulation de votre réservation</h2>
                      </div>
                      <p>Bonjour %s %s,</p>
                      <p>Nous sommes désolés de vous informer que votre réservation pour le trajet suivant a été annulée :</p>
                      <div class="details">
                        <p><strong>Départ :</strong> %s</p>
                        <p><strong>Arrivée :</strong> %s</p>
                        <p><strong>Date :</strong> %s</p>
                      </div>
                      <p>Nous nous excusons pour la gêne occasionnée. N'hésitez pas à consulter d'autres trajets disponibles sur notre plateforme.</p>
                      <div class="footer">
                        <p>Cordialement,<br>L'Équipe Technique</p>
                      </div>
                    </div>
                  </body>
              </html>
            """.formatted(prenom, nom, route.getDeparture(), route.getArrival(), route.getDepartureDate());

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom("votre.email@gmail.com");

            mailSender.send(mimeMessage);
            System.out.println("Email d'annulation envoyé à " + toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'e-mail d'annulation", e);
        }
    }

    public void sendAcceptReservationEmail(String toEmail, String prenom, String nom, Route route) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String subject = "Confirmation de réservation";
            String htmlContent = """
          <html>
              <head>
                <style>
                  body {
                    font-family: Arial, sans-serif;
                    background-color: #f4f4f4;
                    margin: 0;
                    padding: 0;
                  }
                  .container {
                    max-width: 600px;
                    margin: 0 auto;
                    padding: 20px;
                    background-color: #fff;
                    border-radius: 10px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                  }
                  .header {
                    text-align: center;
                    margin-bottom: 20px;
                  }
                  .footer {
                    margin-top: 20px;
                    text-align: center;
                  }
                  .details {
                    margin: 20px 0;
                    padding: 10px;
                    background-color: #d4edda;
                    color: #155724;
                    border: 1px solid #c3e6cb;
                    border-radius: 5px;
                  }
                </style>
              </head>
              <body>
                <div class="container">
                  <div class="header">
                    <h2>Réservation Confirmée !</h2>
                  </div>
                  <p>Bonjour %s %s,</p>
                  <p>Nous avons le plaisir de vous informer que votre réservation pour le trajet suivant a été acceptée :</p>
                  <div class="details">
                    <p><strong>Départ :</strong> %s</p>
                    <p><strong>Arrivée :</strong> %s</p>
                    <p><strong>Date :</strong> %s</p>
                  </div>
                  <p>Nous sommes ravis de vous compter parmi nos passagers. N'hésitez pas à nous contacter si vous avez des questions ou besoin d'assistance.</p>
                  <div class="footer">
                    <p>Cordialement,<br>L'Équipe Technique</p>
                  </div>
                </div>
              </body>
          </html>
        """.formatted(prenom, nom, route.getDeparture(), route.getArrival(), route.getDepartureDate());

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom("votre.email@gmail.com");

            mailSender.send(mimeMessage);
            System.out.println("Email de confirmation envoyé à " + toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'e-mail de confirmation", e);
        }
    }

}
