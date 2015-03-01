package com.pizzaclient.webservice;

import java.io.IOException;

import com.pizzaclient.http.IHttpResponseHandler;
import com.pizzaclient.http.RequestCommand;
import org.apache.http.HttpResponse;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.pizzaclient.R;

public class ServiceCallManager {
	
	Context c;
	
	private String nameSpace;

	private String loginMethodName;
	
	private String loginUrl;
	
	private String loginSoapAction;

	public ServiceCallManager(Context context){
		this.c = context;
		this.nameSpace = c.getString(R.string.namespace_url);
		this.loginMethodName = c.getString(R.string.login_method_name);
		this.loginUrl = c.getString(R.string.login_url);
		this.loginSoapAction = c.getString(R.string.login_action);
	}
	
	public void loginUser(String userName, String password, IHttpResponseHandler responseHandler){
		String url = "http://ec2-54-194-198-209.eu-west-1.compute.amazonaws.com:8080/services/rest/user-service/users/login/" + userName + "/" + password;
        RequestCommand requestCommand = new RequestCommand(responseHandler);
        requestCommand.send(url);
	}

    public void getItems( IHttpResponseHandler responseHandler ){
        String url = "http://ec2-54-194-198-209.eu-west-1.compute.amazonaws.com:8080/services/rest/items-service/items/list-all";
        RequestCommand requestCommand = new RequestCommand(responseHandler);
        requestCommand.send(url);
    }

}
