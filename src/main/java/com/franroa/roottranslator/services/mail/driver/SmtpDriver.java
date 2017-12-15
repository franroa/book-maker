package com.franroa.roottranslator.services.mail.driver;

import com.franroa.roottranslator.services.mail.EmailServiceException;
import com.franroa.roottranslator.services.mail.Mail;
import com.franroa.roottranslator.services.mail.config.FromConfiguration;
import com.franroa.roottranslator.services.mail.config.SmtpConfiguration;
import com.google.common.base.Joiner;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class SmtpDriver implements MailDriver{
    private SmtpConfiguration config;

    public SmtpDriver(SmtpConfiguration config) {
        this.config = config;
    }

    @Override
    public void send(FromConfiguration from, Mail mail) {
        try {
            Message message = createMessage();

            message.setFrom(new InternetAddress(from.address, from.name));

            for (String to : mail.getRecipients()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            }

            message.setSubject(mail.getSubject());
            message.setContent(createContent(mail.getBody()));

            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailServiceException(String.format("Could not send email to {}", Joiner.on(',').join(mail.getRecipients())), e);
        }
    }

    private Message createMessage() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", config.host);
        if (config.port != null) {
            properties.setProperty("mail.smtp.port", config.port);
        }

        Session session = Session.getInstance(properties);

        return new MimeMessage(session);
    }

    private Multipart createContent(String body) {
        try {
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            Multipart content = new MimeMultipart();
            content.addBodyPart(messageBodyPart);

            return content;
        } catch (MessagingException e) {
            throw new RuntimeException(String.format("Could not create content for email with body: %s", body), e);
        }
    }
}
