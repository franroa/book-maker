package com.franroa.roottranslator.resources;

import com.franroa.roottranslator.core.AlreadyReadWord;
import com.franroa.roottranslator.core.Temporary;
import com.franroa.roottranslator.dto.BookTextResponse;
import com.franroa.roottranslator.dto.TranslationRequest;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.javalite.activejdbc.Base;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

@Path("/v1/import")
@Produces(APPLICATION_JSON)
public class ImportResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportResource.class);

    @POST
    @Consumes(MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws Exception {
        String uploadedFileLocation = "uploads/" + fileDetail.getFileName();

        writeToFile(uploadedInputStream, uploadedFileLocation);

        EpubReader epubReader = new EpubReader();
        Book epub = epubReader.readEpub(new FileInputStream(uploadedFileLocation));

        String rawText = "";

        if (epub.getSpine() != null) {
            for (int i = 0; i < epub.getSpine().getSpineReferences().size(); i++) {
                Resource resource = epub.getSpine().getResource(i);

                Scanner s = new Scanner(resource.getInputStream()).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                Document doc = Jsoup.parse(result);
                Elements html = doc.getElementsByTag("html");
                rawText += " " + html.text();
            }

        }

        if (epub.getSpine().getTocResource() == null) {
            Scanner s2 = new Scanner(epub.getOpfResource().getInputStream()).useDelimiter("\\A");
            String metadata = s2.hasNext() ? s2.next() : "";
            Document docMetadata = Jsoup.parse(metadata);
            Elements aPackage = docMetadata.getElementsByAttribute("idref");

            Iterator<Element> metadataIterator = aPackage.iterator();

            while (metadataIterator.hasNext()) {
                String idref = metadataIterator.next().attr("idref");

                Scanner s = new Scanner(epub.getResources().getByIdOrHref("OEBPS/Text/" + idref).getInputStream()).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                Document doc = Jsoup.parse(result);
                Elements html = doc.getElementsByTag("html");
                rawText += " " + html.text();
            }
        }

        if (rawText == null) {
            throw new Exception("the epub could not be read");
        }

        HashSet wordsToSave = new HashSet();
        String[] words = rawText.toUpperCase().replaceAll("[^\\p{L}\\p{Nd}]+", " ").split("\\s");
        for (String word : words) {
            wordsToSave.add(word);
        }

        try {
            Base.openTransaction();
            Temporary.addWordsIfNotAlreadyRead(wordsToSave);
            AlreadyReadWord.addNewWords(wordsToSave);
            Base.commitTransaction();
        } catch(Exception e) {
            Base.rollbackTransaction();
        } finally {
            Base.close();
        }
//
//        Client client = createClient();
//        Response response = client.target("http://localhost:8000/api/v1/word/store")
//                .request()
//                .accept(MediaType.APPLICATION_JSON_TYPE)
//                .post(Entity.entity(new TranslationRequest(wordToTranslate, "de", "es"), APPLICATION_JSON), Response.class);


        return Response.ok().entity(new BookTextResponse(rawText, wordsToSave)).build();




//        List<Resource> resourceList = new ArrayList<>();
//        Scanner s2 = new Scanner(epub.getOpfResource().getInputStream()).useDelimiter("\\A");
//        String metadata = s2.hasNext() ? s2.next() : "";
//        Document docMetadata = Jsoup.parse(metadata);
//        Elements aPackage = docMetadata.getElementsByAttribute("idref");
//
//        Iterator<Element> metadataIterator = aPackage.iterator();
//
//        while (metadataIterator.hasNext()) {
//            String idref = metadataIterator.next().attr("idref");
//
//            resourceList.add(epub.getResources().getByIdOrHref("OEBPS/Text/" + idref));
//        }


//
//
//        String rawText = null;
//        Iterator<Resource> resourceIterator = resourceList.iterator();
////        Page page = new Page();
//
//        while (resourceIterator.hasNext()) {
//            Resource resource = (Resource) resourceIterator.next();
//
//            Scanner s = new Scanner(resource.getInputStream()).useDelimiter("\\A");
//            String result = s.hasNext() ? s.next() : "";
//
//            Document doc = Jsoup.parse(result);
//            Elements html = doc.getElementsByTag("html");
//
////            page.set("page", html.toString());
//            rawText += " " + html.text();
//        }
//
////        page.saveIt();
//


//
//
//
//        Iterator<Resource> resourceIteratorAdditionalElements = epub.getResources().getAll().iterator();
//
//        while (resourceIteratorAdditionalElements.hasNext()) {
//            Resource newResource = resourceIteratorAdditionalElements.next();
//
//            com.franroa.roottranslator.core.Resource resource = new com.franroa.roottranslator.core.Resource();
//
//            resource.set("input_stream", asString(newResource.getInputStream()));
//            resource.set("data", asString(newResource.getData()));
//            resource.set("input_encoding", asString(newResource.getInputEncoding()));
//            resource.set("href", asString(newResource.getHref()));
//            resource.set("media_type", asString(newResource.getMediaType()));
//            resource.set("title", asString(newResource.getTitle()));
//            resource.set("toc_href", asString(newResource.getTocHref()));
//            resource.set("id", asString(newResource.getId()));
//            resource.set("original_href", asString(newResource.getId()));
//            resource.set("reader", asString(newResource.getReader()));
//            resource.set("size", newResource.getSize());
//
//            resource.insert();
//        }
//





//
//        Iterator<Resource> resourceIteratorAdditionalElements = epub.getResources().getAll().iterator();
//        Image images = new Image();
//        Style styles = new Style();
//        com.franroa.roottranslator.core.Book book = new com.franroa.roottranslator.core.Book();
//
//        while (resourceIteratorAdditionalElements.hasNext()) {
//            Resource resource = resourceIteratorAdditionalElements.next();
//
//            MediaType mediaType = resource.getMediaType();
//
//            if (! mediaType.getName().equals("application/x-dtbncx+xml") && ! mediaType.getName().equals("application/xhtml+xml")) {
//                byte[] data = resource.getData();
//                File outputfile = new File(resource.getHref());
//                outputfile.getParentFile().mkdir();
//                outputfile.createNewFile();
//                String extension = FilenameUtils.getExtension(resource.getHref());
//
//                BufferedImage bufferedImage;
//
//                if (mediaType.getName().equals("application/x-dtbncx+xml")) {
//                    book.set("ncx", data);
//                } else if (mediaType.getName().equals("text/css")) {
//                    FileOutputStream fos = new FileOutputStream(resource.getHref());
//                    fos.write(data);
//                    fos.close();
//                    styles.set("style", data);
//
//                } else {
//                    bufferedImage = ImageIO.read(new ByteArrayInputStream(data));
//                    images.set("image", data);
//                    ImageIO.write(bufferedImage, extension, outputfile);
//                }
//            }
//        }
//
//        images.saveIt();
//        styles.saveIt();
//        book.saveIt();
//
//        BookProperty bookProperty = new BookProperty();
//        bookProperty.add(book);
//        bookProperty.add(images);
//        bookProperty.add(styles);
//        bookProperty.add(page);

    }

    private Client createClient() {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
                .credentials("test", "pass")
                .build();

        return ClientBuilder.newClient(new ClientConfig().register(feature))
                .property(ClientProperties.CONNECT_TIMEOUT, 5000)
                .property(ClientProperties.READ_TIMEOUT, 5000);
    }

    private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
        int read;
        final int BUFFER_LENGTH = 1024;
        final byte[] buffer = new byte[BUFFER_LENGTH];
        OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
        while ((read = uploadedInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
        out.close();
    }
}
