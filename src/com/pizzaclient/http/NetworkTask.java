package com.pizzaclient.http;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

public class NetworkTask extends AsyncTask<String, Void, HttpResponse> {

    private final IRequestCommand parentCommand;

    public NetworkTask(IRequestCommand parentCommand){
        this.parentCommand = parentCommand;
    }

    @Override
    protected HttpResponse doInBackground(String... params) {
        String link = params[0];
        HttpGet request = new HttpGet(link);
        AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        try {
            return client.execute(request);
        } catch (IOException e) {
            return null;
        } finally {
            client.close();
        }
    }

    @Override
    protected void onPostExecute(HttpResponse result) {
        parentCommand.handleHttpResponse(result);
    }
}