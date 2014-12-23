package com.pizzaclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class GesturedActivity extends Activity implements OnClickListener{
	
	WebView webView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestured_layout);
        webView = (WebView)this.findViewById(R.id.webView);
        Button buttonBrowserGo = (Button)findViewById(R.id.buttonBrowserGo);
        buttonBrowserGo.setOnClickListener(this);
        gotoPage("http://www.origo.hu");
    }
	
	private void gotoPage(String url){

        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);

        webView.setWebViewClient(new Callback());  //HERE IS THE MAIN CHANGE
        webView.loadUrl(url);

    }

    private class Callback extends WebViewClient{  //HERE IS THE MAIN CHANGE. 

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
        
        

    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonBrowserGo){
			EditText editTextBrowserUrl =(EditText)findViewById(R.id.editTextBrowserUrl);
			gotoPage(editTextBrowserUrl.getText().toString());
		}
	}
}
