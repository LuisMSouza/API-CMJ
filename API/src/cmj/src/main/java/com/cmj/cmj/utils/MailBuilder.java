package com.cmj.cmj.utils;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MailBuilder {

    private static Mail mail;
    private static Personalization personalization;

    public static MailBuilder aMail() {
        return new MailBuilder();
    }

    private MailBuilder() {
        mail = new Mail();
        personalization = new Personalization();
        mail.addPersonalization(personalization);
    }

    public MailBuilder from(String fromEmail, String fromName) {
        mail.setFrom(new Email(fromEmail, fromName));
        return this;
    }

    public MailBuilder subject(String subject) {
        mail.setSubject(subject);
        return this;
    }

    public MailBuilder to(String toEmail) {
        personalization.addTo(new Email(toEmail));
        return this;
    }

    public MailBuilder sendAt(LocalDateTime dateTime) {
        personalization.setSendAt(dateTime.toEpochSecond(ZoneOffset.UTC));
        return this;
    }

    public MailBuilder content(String content) {
        mail.addContent(new Content("text/plain", content));
        return this;
    }

    public Mail build() {
        return mail;
    }

}
