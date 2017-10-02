package com.franroa.roottranslator.resources;


import com.franroa.roottranslator.FeatureTestEnvironment;
import com.franroa.roottranslator.core.AlreadyReadWord;
import com.franroa.roottranslator.core.Temporary;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.javalite.activejdbc.LazyList;
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
    public void import_all_words_from_an_uploaded_file_in_temporary_and_already_read_words_tables() throws FileNotFoundException {
        final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("src/test/resources/testfile"));
        final FormDataMultiPart multipart = new FormDataMultiPart();
        multipart.bodyPart(filePart);


        Response response = resources.client()
                .register(MultiPartFeature.class)
                .target("/import")
                .request()
                .post(Entity.entity(multipart, MULTIPART_FORM_DATA));


        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
        assertThat(Temporary.where("word = ?", "Hello")).isNotEmpty();
        assertThat(Temporary.where("word = ?", "World")).isNotEmpty();
        assertThat(AlreadyReadWord.where("word = ?", "Hello")).isNotEmpty();
        assertThat(AlreadyReadWord.where("word = ?", "World")).isNotEmpty();
        File f = new File("uploads/testfile");
        assertThat(f.exists()).isTrue();
        assertThat(f.isDirectory()).isFalse();
    }

    @Test
    public void import_only_not_already_read_words_from_an_uploaded_file_in_temporary_and_already_read_words_tables() throws FileNotFoundException {
        AlreadyReadWord alreadyReadWord = new AlreadyReadWord();
        alreadyReadWord.set("word", "Hello").saveIt();
        final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("src/test/resources/testfile"));
        final FormDataMultiPart multipart = new FormDataMultiPart();
        multipart.bodyPart(filePart);


        Response response = resources.client()
                .register(MultiPartFeature.class)
                .target("/import")
                .request()
                .post(Entity.entity(multipart, MULTIPART_FORM_DATA));


        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
        assertThat(Temporary.where("word = ?", "Hello")).isEmpty();
        assertThat(Temporary.where("word = ?", "World")).isNotEmpty();
        assertThat(AlreadyReadWord.where("word = ?", "Hello")).isNotEmpty();
        assertThat(AlreadyReadWord.where("word = ?", "World")).isNotEmpty();
        File f = new File("uploads/testfile");
        assertThat(f.exists()).isTrue();
        assertThat(f.isDirectory()).isFalse();
    }

//    @After
//    public void cleanup() throws Exception {
//        File f = new File("uploads/testfile");
//        f.delete();
//    }
}
