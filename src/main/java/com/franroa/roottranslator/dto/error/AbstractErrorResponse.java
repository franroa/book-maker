package com.franroa.roottranslator.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractErrorResponse {
    @JsonProperty("error_code")
    public String errorCode = "";

    @JsonProperty("error_message")
    public String errorMessage = "";

    @JsonProperty("affected_fields")
    public ArrayList<String> affectedFields;

    //TODO correlation id
//    @JsonProperty("correlation_id")
//    public String correlationId = CorrelationIdRequestFilter.getCorrelationId();
}
