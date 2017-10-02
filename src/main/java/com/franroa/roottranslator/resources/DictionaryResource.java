package com.franroa.roottranslator.resources;

import com.franroa.roottranslator.dto.TranslationRequest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

@Path("/dictionary")
public class DictionaryResource {
    @POST
    public Response store(TranslationRequest request) {
        return Response.ok().entity(Entity.json(request.getWord())).build();
    }
}
