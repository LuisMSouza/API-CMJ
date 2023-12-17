package com.cmj.cmj.senders;

import com.cmj.cmj.model.Appointment;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public class SMSSender {
    private static final PhoneNumber fromPhoneNumber = new PhoneNumber("+14159697625");
    @Value("${TWILIO_ACCOUNT_SID}")
    private String ACCOUNT_SID;

    @Value("${TWILIO_AUTH_TOKEN}")
    private String AUTH_TOKEN;
    @Value("${MESSAGE_SERVICE_SID}")
    private String MESSAGE_SERVICE_SID;


    public ResponseEntity<String> send(String toPhoneNumber, String content) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                            new PhoneNumber(toPhoneNumber), fromPhoneNumber, content)
                    .setMessagingServiceSid(MESSAGE_SERVICE_SID)
                    .create();
            return ResponseEntity.ok(message.getStatus().toString());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }

    public ResponseEntity<String> send(String toPhoneNumber, String content, ZonedDateTime sendAt) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                            new PhoneNumber(toPhoneNumber), fromPhoneNumber, content)
                    .setMessagingServiceSid(MESSAGE_SERVICE_SID)
                    .setScheduleType(Message.ScheduleType.FIXED)
                    .setSendAt(sendAt)
                    .create();
            return ResponseEntity.ok(message.getStatus().toString());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    public ResponseEntity<String> sendConfirmedAppointmentMessage(Appointment appointment) {
        return send("+55" + appointment.getAppoinClient().getUsuarCellphone(), buildConfirmedMessage(appointment));
    }

    public ResponseEntity<String> sendAppointmentNotification(Appointment appointment, int minutesBefore) {
        return send("+55" + appointment.getAppoinClient().getUsuarCellphone(),
                buildNotificationMessage(appointment), ZonedDateTime.of(appointment.getAppoinDate().minusMinutes(minutesBefore), ZoneId.systemDefault()));
    }

    private static String buildConfirmedMessage(Appointment appointment) {
        return String.format("""
                        Olá, %s
                        Sua consulta com %s está confirmada para %s às %s.
                        Lembre-se de trazer seus documentos médicos.
                        Atenciosamente,
                        Clínica Menino Jesus
                        """,
                appointment.getAppoinClient().getUsuarName(),
                appointment.getAppoinDoctor().getUsuarName(),
                appointment.getAppoinDate().format(ofPattern("dd/MM/yyyy")),
                appointment.getAppoinDate().format(ofPattern("hh:mm")));
    }

    private static String buildNotificationMessage(Appointment appointment) {
        return String.format("""
                        ATENÇÃO!
                        Olá, %s
                        Sua consulta com %s no dia de hoje %s será daqui 1 (uma) hora, às %s.
                        Lembre-se de trazer seus documentos médicos.
                        Atenciosamente,
                        Clínica Menino Jesus
                        """,
                appointment.getAppoinClient().getUsuarName(),
                appointment.getAppoinDoctor().getUsuarName(),
                appointment.getAppoinDate().format(ofPattern("dd/MM/yyyy")),
                appointment.getAppoinDate().format(ofPattern("hh:mm")));
    }
}
