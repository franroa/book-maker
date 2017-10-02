package com.franroa.roottranslator.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.franroa.roottranslator.core.Temporary;
import com.franroa.roottranslator.dto.TranslationRequest;
import com.franroa.roottranslator.dto.WordRequest;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.javalite.activejdbc.LazyList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/v1/translate")
public class TranslatorResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response store(WordRequest request) {

        LazyList<Temporary> temporalWords = Temporary.findAll();

        if (! temporalWords.isEmpty()) {

            for(Temporary temporalWord: temporalWords) {
                String wordToTranslate = temporalWord.get("word").toString();

                Client client = createClient();
                Response response = client.target("http://localhost:8000/api/v1/word/store")
                        .request()
                        .accept(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(new TranslationRequest(wordToTranslate, "de", "es"), APPLICATION_JSON), Response.class);


                String translatedWord = response.readEntity(JsonNode.class).get("word").asText();
                response.close();

                if (! wordToTranslate.equals(translatedWord)) {
                    throw new RuntimeException("wordToTranslate and translatedWord should be the same");
                }

                Temporary.findFirst("word = ?", translatedWord).delete();
            }

            return Response.ok().build();
        }


        return Response.serverError().build();
    }

    private Client createClient() {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
                .credentials("test", "pass")
                .build();

        return ClientBuilder.newClient(new ClientConfig().register(feature))
                .property(ClientProperties.CONNECT_TIMEOUT, 5000)
                .property(ClientProperties.READ_TIMEOUT, 5000);
    }
}
