package com.franroa.roottranslator.services.mail;

import java.util.ArrayList;

public interface Mail {
    ArrayList<String> getRecipients();
    String getSubject();
    String getBody();
}
