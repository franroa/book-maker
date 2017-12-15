package com.franroa.roottranslator.services.mail.driver;


import com.franroa.roottranslator.services.mail.Mail;
import com.franroa.roottranslator.services.mail.config.FromConfiguration;

public interface MailDriver {
    void send(FromConfiguration from, Mail mail);
}
