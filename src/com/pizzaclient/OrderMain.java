package com.pizzaclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.pizzaclient.R;

/**
 * Created by Tibor Kovacs on 2014.12.28..
 */
public class OrderMain extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main_layout);
        Button buttonLogout = (Button)findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);
        String userName = MainActivity.getSessionManager().getUsername();
        fillFields(userName);
    }

    private void fillFields( String userName ) {
        TextView loginText = (TextView)findViewById(R.id.loginNameText);
        String userNameText = getResources().getString(R.string.hu_logged_in_label);
        loginText.setText(userNameText + ":" + userName);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonLogout){
            MainActivity.getSessionManager().clear();
            Intent mainActivity = new Intent(v.getContext(), MainActivity.class);
            v.getContext().startActivity(mainActivity);
        }
    }

}
