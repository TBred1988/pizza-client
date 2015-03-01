package com.pizzaclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.pizzaclient.http.IHttpResponseHandler;
import com.pizzaclient.parser.JsonParser;
import com.pizzaclient.webservice.ServiceCallManager;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tibor Kovacs on 2014.12.28..
 */
public class OrderMain extends Activity implements View.OnClickListener, IHttpResponseHandler {

    private ServiceCallManager serviceCallManager;

    private String[] orderItems;

    public static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main_layout);
        this.serviceCallManager = new ServiceCallManager(this.getApplicationContext());
        Button buttonLogout = (Button)findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);
        String userName = MainActivity.getSessionManager().getUsername();
        this.serviceCallManager.getItems(this);
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

    @Override
    public void updateResultPage(HttpResponse response) {
        if(response != null){
            JsonParser jsonParser = new JsonParser();
            JSONObject jsonObject = jsonParser.getJSONFromResponse(response);
            if(jsonObject !=  null){
                try {
                    updateByResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateByResponse(JSONObject jsonObject)throws JSONException{
        handleItemsResponse(jsonObject);
    }

    private void handleItemsResponse(JSONObject jsonObject) throws JSONException {
        JSONArray items = jsonObject.getJSONArray("items");
        ListView itemsList = (ListView)findViewById(R.id.items);
        if(items != null){
            orderItems = new String[items.length()];
            for(int i=0; i<items.length(); i++)
            {
                JSONObject actItem = items.getJSONObject(i);
                orderItems[i] = actItem.getString("shortName") + "," + actItem.getString("priceHu");
            }

            adapter = new CustomArrayAdapter(getApplicationContext(), R.layout.row, orderItems);
        }else{
            orderItems = new String[1];
            orderItems[0] = "items NOT FOUND";
            adapter = new CustomArrayAdapter(getApplicationContext(), R.layout.row, orderItems);
        }
        itemsList.setAdapter(adapter);
    }

    private class CustomArrayAdapter extends ArrayAdapter<String>{

        private Context context;

        public CustomArrayAdapter(Context context, int textViewResourceId,
                                  String[] objects){
            super(context, textViewResourceId, objects);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.textOfItems);
            TextView priceView = (TextView) rowView.findViewById(R.id.priceOfItems);
            String item = getItem(position);
            String[] values = item.split(",");
            String name = values[0];
            String price = "ár: " + values[1];
            textView.setText(name);
            priceView.setText(price);
            return rowView;
        }

    }

}
