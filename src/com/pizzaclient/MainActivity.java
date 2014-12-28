package com.pizzaclient;

import android.content.SharedPreferences;
import android.widget.TextView;
import com.pizzaclient.http.IHttpResponseHandler;
import com.pizzaclient.parser.JsonParser;
import com.pizzaclient.session.SessionManager;
import com.pizzaclient.webservice.ServiceCallManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements OnClickListener, IHttpResponseHandler {

    private ServiceCallManager serviceCallManager;

    private static SessionManager sessionManager;

    private View onClickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonBrowser = (Button)findViewById(R.id.buttonLogin);
        buttonBrowser.setOnClickListener(this);
        this.serviceCallManager = new ServiceCallManager(this.getApplicationContext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonLogin){
            String userName = getUserName();
            String password = getPassword();
            serviceCallManager.loginUser(userName, password, this);
            this.onClickView = v;
		}
	}

    private String getPassword() {
        EditText passwordText = (EditText)findViewById(R.id.editTextPassword);
        return passwordText.getText().toString();
    }

    private String getUserName() {
        EditText userText = (EditText)findViewById(R.id.editTextUserName);
        return userText.getText().toString();
    }

    @Override
    public void updateResultPage(HttpResponse response) {
        if(response != null){
            TextView resultText = (TextView)findViewById(R.id.textLoginResult);
            JsonParser jsonParser = new JsonParser();
            JSONObject jsonObject = jsonParser.getJSONFromResponse(response);
            if(jsonObject !=  null){
                try {
                    resultText.setText(jsonObject.getString("message"));
                    int statusCode = jsonObject.getInt("statusCode");
                    if(statusCode == 200){
                        if(this.onClickView != null){
                            sessionManager = new SessionManager(this.onClickView.getContext());
                            sessionManager.login(getUserName(), getPassword());
                            Intent orderMainActivity = new Intent(this.onClickView.getContext(), OrderMain.class);
                            this.onClickView.getContext().startActivity(orderMainActivity);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static SessionManager getSessionManager() {
        return sessionManager;
    }

}
