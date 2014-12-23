package com.pizzaclient.http;

import org.apache.http.HttpResponse;

/**
 * Created by user on 2014.09.07..
 */
public interface IRequestCommand {

    public void handleHttpResponse(HttpResponse response);

}
