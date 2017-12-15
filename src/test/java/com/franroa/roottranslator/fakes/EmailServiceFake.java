package com.franroa.roottranslator.fakes;

import com.franroa.roottranslator.services.mail.EmailService;
import com.franroa.roottranslator.services.mail.EmailServiceException;
import com.franroa.roottranslator.services.mail.Mail;
import com.google.common.base.Joiner;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.function.Function;

public class EmailServiceFake implements EmailService {
    private ArrayList<Mail> mailsSent = new ArrayList<>();
    private Boolean shouldFail = false;

    @Override
    public void send(Mail mail) {
        if (shouldFail) {
            throw new EmailServiceException(String.format("Could not send email to {}", Joiner.on(',').join(mail.getRecipients())));
        }

        mailsSent.add(mail);
    }

    public void fakeFail() {
        shouldFail = true;
    }

    public <T extends Mail> void assertSent(Class<T> mailClass, Function<T, Boolean> compareFunction) {
        for (Mail mail : mailsSent) {
            if (mailClass.equals(mail.getClass()) && compareFunction.apply((T) mail)) {
                return;
            }
        }

        Assert.fail("Email was not sent");
    }

    public void assertSent(Class<? extends Mail> mailClass) {
        assertSent(mailClass, (mail) -> true);
    }

    public <T extends Mail> void assertNotSent(Class<T> mailClass) {
        for (Mail mail : mailsSent) {
            if (mailClass.equals(mail.getClass())) {
                Assert.fail("Email was sent, but shouldn't.");
            }
        }
    }
}
