package com.franroa.roottranslator.services.bookmaker;

public interface BookMakerService {
    public abstract int sendBook(String uploadFileLocation) throws BookMakerException;
}
