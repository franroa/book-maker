package com.franroa.roottranslator.resources;

import com.franroa.roottranslator.core.Temporary;
import com.franroa.roottranslator.dto.WordRequest;
import org.javalite.activejdbc.LazyList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Path("/translate")
public class TranslatorResource {
    @POST
    public Response store(WordRequest request) {
        try {
            LazyList<Temporary> temporarlWords = Temporary.findAll();
            if (!temporarlWords.isEmpty()) {
                String wordToTranslate = temporarlWords.get(0).get("word").toString();

                URLConnection connection = new URL("http://de.pons.com/%C3%BCbersetzung?q=" + wordToTranslate + "&l=dees&in=&lf=es").openConnection();
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                InputStream response = connection.getInputStream();

                return Response.ok().entity(response).build();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.serverError().build();
    }
}
