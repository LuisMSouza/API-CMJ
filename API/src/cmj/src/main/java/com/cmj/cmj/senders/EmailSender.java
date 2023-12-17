package com.cmj.cmj.senders;

import com.cmj.cmj.model.Appointment;
import com.cmj.cmj.model.User;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.cmj.cmj.utils.MailBuilder.aMail;
import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public class EmailSender {
    private static final String fromEmail = "clinimeninojesus@gmail.com";
    private static final String fromName = "Clínica Menino Jesus";

    @Value("${SENDGRID_API_KEY}")
    private String apiKey;

    public ResponseEntity<String> sendEmail(Appointment appointment, String subject, String content) {
        try {
            return sendEmail(appointment.getAppoinClient().getUsuarEmail(), subject, content);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    public ResponseEntity<String> sendEmail(String email, String subject, String content) {
        SendGrid sendGrid = new SendGrid(apiKey);
        Mail mail = aMail()
                .from(fromEmail, fromName)
                .to(email)
                .subject("Clinica Menino Jesus - " + subject)
                .content(content)
                .build();
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println(response.getBody());
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (IOException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    public ResponseEntity<String> sendUpdatePasswordEmail(User user, String redirectUrl) {
        return sendEmail(user.getUsuarEmail(),
                "Solicitação de Alteração de Senha",
                buildUpdatePasswordContent(user, redirectUrl));
    }

    public ResponseEntity<String> sendCreatedAppointmentEmail(Appointment appointment) {
        return sendEmail(appointment,
                "Pedido de Consulta",
                buildAppointmentContent(appointment,
                        "Seu pedido de consulta na Clínica Menino Jesus foi feito."));
    }

    public ResponseEntity<String> sendConfirmedAppointmentEmail(Appointment appointment) {
        return sendEmail(appointment,
                "Confirmação de Consulta",
                buildAppointmentContent(appointment, "Seu pedido de consulta na Clínica Menino Jesus foi confirmado."));
    }

    public ResponseEntity<String> sendUpdatedAppointmentEmail(Appointment oldAppointment, Appointment newAppointment) {
        return sendEmail(oldAppointment,
                "Alteração de Consulta",
                buildUpdateAppointmentContent(oldAppointment, newAppointment));
    }

    public ResponseEntity<String> sendDeleteAppointmentEmail(Appointment appointment) {
        return sendEmail(appointment,
                "Cancelamento de Consulta",
                buildAppointmentContent(appointment, "Sua consulta foi cancelada na Clínica Menino Jesus."));
    }

    public String buildUpdateAppointmentContent(Appointment oldAppointment, Appointment newAppointment) {
        User user = oldAppointment.getAppoinClient();
        return String.format("""
                        Olá, %s
                        Sua consulta foi alterada na Clínica Menino Jesus.
                                                
                        Consulta Atual:
                        Data: %s
                        Horário: %s
                        Médico: %s
                        Tipo de Pagamento: %s
                                                
                        Consulta Antiga:
                        Data: %s
                        Horário: %s
                        Médico: %s
                        Tipo de Pagamento: %s

                        Agradecemos sua confiança e esperamos vê-lo(a) em breve.
                        Atenciosamente,
                                Clinica Menino Jesus.
                        """,
                user.getUsuarName(),
                newAppointment.getAppoinDate().format(ofPattern("dd/MM/yyyy")),
                newAppointment.getAppoinDate().format(ofPattern("hh:ss")),
                newAppointment.getAppoinDoctor().getUsuarName(),
                newAppointment.getAppoinPaymentType(),
                oldAppointment.getAppoinDate().format(ofPattern("dd/MM/yyyy")),
                oldAppointment.getAppoinDate().format(ofPattern("hh:ss")),
                oldAppointment.getAppoinDoctor().getUsuarName(),
                oldAppointment.getAppoinPaymentType()
        );

    }

    private String buildAppointmentContent(Appointment appointment, String subject) {
        User user = appointment.getAppoinClient();
        return String.format("""
                        Olá, %s
                        %s
                                                
                        Data: %s
                        Horário: %s
                        Médico: %s
                        Tipo de Pagamento: %s
                                                
                        Agradecemos sua confiança e esperamos vê-lo(a) em breve.
                        Atenciosamente,
                                Clinica Menino Jesus
                        """,
                user.getUsuarName(),
                subject,
                appointment.getAppoinDate().format(ofPattern("dd/MM/yyyy")),
                appointment.getAppoinDate().format(ofPattern("hh:ss")),
                appointment.getAppoinDoctor().getUsuarName(),
                appointment.getAppoinPaymentType()
        );
    }

    private String buildUpdatePasswordContent(User user, String redirectUrl) {
        return String.format("""
                        Olá %s,
                        Recebemos uma solicitação de alteração de senha para sua conta.
                        Se você não solicitou esta alteração, pode ignorar este e-mail.

                        Se você solicitou esta alteração, clique no link abaixo para criar uma nova senha:
                        %s

                        Este link expirará em 24 horas.
                                
                        Atenciosamente,
                                Clinica Menino Jesus
                        """,
                user.getUsuarName(), redirectUrl
        );
    }


}
