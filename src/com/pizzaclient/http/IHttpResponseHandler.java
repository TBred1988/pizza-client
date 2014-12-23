package com.pizzaclient.http;

import org.apache.http.HttpResponse;

/**
 * Created by user on 2014.09.07..
 */
public interface IHttpResponseHandler {

    public void updateResultPage(HttpResponse response);
}
