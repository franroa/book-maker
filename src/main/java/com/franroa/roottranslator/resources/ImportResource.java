package com.franroa.roottranslator.resources;

import com.franroa.roottranslator.core.AlreadyReadWord;
import com.franroa.roottranslator.core.Temporary;
import com.franroa.roottranslator.core.Word;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static javax.ws.rs.core.MediaType.*;

@Path("/import")
@Produces(APPLICATION_JSON)
public class ImportResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportResource.class);

    @POST
    @Consumes(MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {

        Set<String> uniqueWords = getUniqueWordsOfFile(fileDetail);

        Temporary.addWordsIfNotAlreadyRead(uniqueWords);
        AlreadyReadWord.addNewWords(uniqueWords);

//        try (BufferedReader br = new BufferedReader(new FileReader(uploadedFileLocation))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                Scanner scanner = new Scanner(line);
//                while (scanner.hasNextLine()) {
//                    String nextToken = scanner.next();
//                }
//            }
//        }

        return Response.ok().build();
    }

    private Set<String> getUniqueWordsOfFile(FormDataContentDisposition fileDetail) {
        String uploadedFileLocation = "uploads/" + fileDetail.getFileName();

        Set<String> uniqueWords = new HashSet<String>();
        Scanner scanner = null;

        try {
            scanner = new Scanner(new File(uploadedFileLocation));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (scanner.hasNextLine()) {
            String[] words = scanner.next().split("[\\W]+");
            for (String word : words) {
                uniqueWords.add(word);
            }
        }

        return uniqueWords;
    }
}
