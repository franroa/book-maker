package com.franroa.roottranslator.services.requester;

import javax.ws.rs.core.Response;

public interface RequesterService {
    Response post(String url, Object body);
    Response get(String url);
    RequesterServiceImpl register(Class<?> var1);
}
