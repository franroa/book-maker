package com.franroa.roottranslator.services.mail;


import com.franroa.roottranslator.services.mail.config.EmailServiceConfiguration;
import com.franroa.roottranslator.services.mail.config.SupportedMailDrivers;
import com.franroa.roottranslator.services.mail.driver.MailDriver;
import com.franroa.roottranslator.services.mail.driver.NullDriver;
import com.franroa.roottranslator.services.mail.driver.SmtpDriver;

public class EmailServiceImpl implements EmailService {
    private EmailServiceConfiguration configuration;
    private MailDriver driver;

    public EmailServiceImpl(EmailServiceConfiguration configuration) {
        this.configuration = configuration;
        this.driver = createDriver();
    }

    @Override
    public void send(Mail mail) {
        this.driver.send(configuration.from, mail);
    }

    private MailDriver createDriver() {
        if (configuration.driver.equals(SupportedMailDrivers.NULL)) {
            return new NullDriver();
        }

        if (configuration.driver.equals(SupportedMailDrivers.SMTP)) {
            return new SmtpDriver(configuration.smtp);
        }

        throw new RuntimeException(String.format("The configured driver '%s' is not supported!", configuration.driver));
    }
}
