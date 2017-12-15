package com.franroa.roottranslator.services.bookmaker;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.core.Response;
import java.io.File;

import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

public class BookMakerServiceImpl implements BookMakerService {
    private HttpClient client;

    public BookMakerServiceImpl(BookMakerServiceConfiguration configuration) {
        client = new HttpClient(configuration.host);
    }

    @Override
    public int sendBook(String uploadFileLocation) throws BookMakerException {
        Response response = null;

        try {
            final FileDataBodyPart filePart = new FileDataBodyPart("file", new File(uploadFileLocation));
            final FormDataMultiPart multipart = new FormDataMultiPart();
            multipart.bodyPart(filePart);

            response =  client
                    .register(MultiPartFeature.class)
                    .post("v1/books", multipart)
                    .mediaType(MULTIPART_FORM_DATA)
                    .expects(200);

            return response.getStatus();
        } catch(Exception e) {
            throw new BookMakerException("something went wrong!");
        } finally {
            client.closeResponse(response);
        }
    }
}
