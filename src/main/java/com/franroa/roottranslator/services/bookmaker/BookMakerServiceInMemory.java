package com.franroa.roottranslator.services.bookmaker;

public class BookMakerServiceInMemory implements BookMakerService{
    private static BookMakerServiceInMemory singleton = null;

    protected BookMakerServiceInMemory() {}

    public static BookMakerServiceInMemory getInstance() {
        if(singleton == null) {
            singleton = new BookMakerServiceInMemory();
        }

        return singleton;
    }

    @Override
    public int sendBook(String uploadFileLocation) throws BookMakerException {
        return 200;
    }
}
