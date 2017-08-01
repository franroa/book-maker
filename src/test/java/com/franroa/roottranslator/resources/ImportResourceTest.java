package com.franroa.roottranslator.resources;


import com.franroa.roottranslator.FeatureTestEnvironment;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileNotFoundException;

import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static org.assertj.core.api.Assertions.assertThat;

public class ImportResourceTest extends FeatureTestEnvironment {
    @Test
    public void it_uploads_a_file_and_saves_each_word_in_the_database() throws FileNotFoundException {
        final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("src/test/resources/testfile"));
        final FormDataMultiPart multipart = new FormDataMultiPart();
        multipart.bodyPart(filePart);


        Response response = resources.client()
                .register(MultiPartFeature.class)
                .target("/import")
                .request()
                .post(Entity.entity(multipart, MULTIPART_FORM_DATA));


        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
        File f = new File("uploads/testfile");
        assertThat(f.exists()).overridingErrorMessage("file does not exists").isTrue();
        assertThat(f.isDirectory()).overridingErrorMessage("file is a folder").isFalse();
    }

//    @After
//    public void cleanup() throws Exception {
//        File f = new File("uploads/testfile");
////        f.delete();
//    }
}
