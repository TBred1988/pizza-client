package com.pizzaclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ServiceActivity extends Activity implements OnClickListener{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_layout);
        Button buttonServiceGo = (Button)findViewById(R.id.buttonServiceGo);
        buttonServiceGo.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonServiceGo){
			sendRequestToService();
		}	
	}
	
	private void sendRequestToService(){
		
	}
	
}
