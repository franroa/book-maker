package com.franroa.roottranslator.services.mail.driver;


import com.franroa.roottranslator.services.mail.Mail;
import com.franroa.roottranslator.services.mail.config.FromConfiguration;

public class NullDriver implements MailDriver {
    @Override
    public void send(FromConfiguration from, Mail mail) {

    }
}
