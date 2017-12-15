package com.franroa.roottranslator.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franroa.roottranslator.core.Temporary;
import com.franroa.roottranslator.dto.WordRequest;
import com.franroa.roottranslator.jobs.SendTranslateWordToScrapperJob;
import org.javalite.activejdbc.LazyList;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static jodd.util.ThreadUtil.sleep;

@Path("/v1/translate")
public class TranslatorResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response store(WordRequest request) {
        LazyList<Temporary> temporalWords = Temporary.findAll();

        if (! temporalWords.isEmpty()) {
            new SendTranslateWordToScrapperJob().dispatch();
            return Response.ok().build();
        }

        return Response.ok().entity(new ObjectMapper().createObjectNode().put("error", "database is empty")).build();
    }
}
