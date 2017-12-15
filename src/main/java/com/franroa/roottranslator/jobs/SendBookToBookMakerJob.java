package com.franroa.roottranslator.jobs;

import com.franroa.roottranslator.container.Container;
import com.franroa.roottranslator.queue.Job;
import com.franroa.roottranslator.services.bookmaker.BookMakerService;

public class SendBookToBookMakerJob extends Job {
    @Override
    protected void handle() {
        try {
            Container.resolve(BookMakerService.class).sendBook(data.get("uploadedFileLocation").toString());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public SendBookToBookMakerJob setUploadFileLocation(String toAddress) {
        return (SendBookToBookMakerJob) set("uploadedFileLocation", toAddress);
    }
}
