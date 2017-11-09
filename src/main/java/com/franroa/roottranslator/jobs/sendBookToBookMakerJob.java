package com.franroa.roottranslator.jobs;

import com.franroa.roottranslator.container.Container;
import com.franroa.roottranslator.queue.Job;
import com.franroa.roottranslator.services.requester.RequesterService;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.client.Entity;
import java.io.File;

import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

public class sendBookToBookMakerJob extends Job {
    @Override
    protected void handle() {
        final FileDataBodyPart filePart = new FileDataBodyPart("file", new File(data.get("uploadedFileLocation").toString()));
        final FormDataMultiPart multipart = new FormDataMultiPart();
        multipart.bodyPart(filePart);

        Container.resolve(RequesterService.class)
                .register(MultiPartFeature.class)
                .post("http://localhost:8283/v1/books", Entity.entity(multipart, MULTIPART_FORM_DATA));
    }

    public sendBookToBookMakerJob setUploadFileLocation(String toAddress) {
        return (sendBookToBookMakerJob) set("uploadedFileLocation", toAddress);
    }
}
