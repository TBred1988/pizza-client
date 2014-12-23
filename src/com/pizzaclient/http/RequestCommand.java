package com.pizzaclient.http;

import org.apache.http.HttpResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by user on 2014.08.27..
 */
public class RequestCommand implements IRequestCommand{

    private Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    NetworkTask networkTask;

    private IHttpResponseHandler responseHandler;

    public RequestCommand(IHttpResponseHandler responseHandler){
        this.networkTask = new NetworkTask(this);
        this.responseHandler=responseHandler;
        LOGGER.setLevel(Level.WARNING);
    }

    public void send(String url){
        this.networkTask.execute(url);
    }

    @Override
    public void handleHttpResponse(HttpResponse response) {
        if(response != null){
            this.responseHandler.updateResultPage(response);
        }
    }
}

