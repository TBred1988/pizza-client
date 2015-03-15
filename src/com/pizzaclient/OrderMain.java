package com.pizzaclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    public static ArrayAdapter<String> itemsAdapter;

    public static ArrayAdapter<String> bucketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main_layout);
        this.serviceCallManager = new ServiceCallManager(this.getApplicationContext());
        MainActivity.getSessionManager().initBucket();
        Button buttonLogout = (Button)findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);
        Button buttonBucket = getBucketButton();
        buttonBucket.setOnClickListener(this);
        String userName = MainActivity.getSessionManager().getUsername();
        Log.i("OrderMain", "Retrieving items to order activity");
        this.serviceCallManager.getItems(this);
        fillFields(userName);
    }

    private Button getBucketButton() {
        return (Button)findViewById(R.id.buttonBucket);
    }

    private void fillFields( String userName ) {
        Log.i("OrderMain","Filling username text with value: " + userName);
        TextView loginText = (TextView)findViewById(R.id.loginNameText);
        String userNameText = getResources().getString(R.string.hu_logged_in_label);
        loginText.setText(userNameText + ":" + userName);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonLogout){
            Log.i("OrderMain","Logging out");
            MainActivity.getSessionManager().clear();
            Intent mainActivity = new Intent(v.getContext(), MainActivity.class);
            v.getContext().startActivity(mainActivity);
        }else if(v.getId() == R.id.buttonItem){
            Log.i("OrderMain", "Try retrieving item for bucket addition");
            TextView textView = (TextView)((ViewGroup)v.getParent()).findViewById(R.id.textOfItems);
            addItemToBucket(textView.getText().toString());
            v.setEnabled(Boolean.FALSE);
        }else if(v.getId() == R.id.buttonBucket){
            Log.i("OrderMain", "Bucket button clicked");
            if(isBucketText(v)){
                Log.i("OrderMain", "Show bucket content");
                showBucketViewAndContents(v);
            }else {
                Log.i("OrderMain", "Show items content");
                showItemsViewAndContents(v);
            }
        }
    }

    private void showItemsViewAndContents(View button) {
        Log.i("OrderMain", "Back to items");
        getItemsView().setVisibility(View.VISIBLE);
        getBucketButton().setVisibility(View.VISIBLE);
        getBucketView().setVisibility(View.INVISIBLE);
        ((Button)button).setText(button.getResources().getString(R.string.hu_button_bucket_label));
    }

    private boolean isBucketText(View button) {
        return ((Button)button).getText().equals(button.getResources().getString(R.string.hu_button_bucket_label));
    }

    private void showBucketViewAndContents(View button) {
        Log.i("OrderMain", "Opening bucket");
        ListView itemsList = getItemsView();
        ListView bucketItemsList = getBucketView();
        itemsList.setVisibility(View.INVISIBLE);
        String[] bucketItems = MainActivity.getSessionManager().bucketItems();
        Log.i("OrderMain", "Bucket items: " + bucketItems);
        if(bucketItems != null){
            bucketAdapter = new BucketArrayAdapter(getApplicationContext(), R.layout.row, bucketItems, this);
            bucketItemsList.setAdapter(bucketAdapter);
            bucketItemsList.setVisibility(View.VISIBLE);
            Log.i("OrderMain", "Bucket showing is visible");
        }
        ((Button)button).setText(button.getResources().getString(R.string.hu_button_back_from_bucket_label));
    }

    private ListView getBucketView() {
        return (ListView)findViewById(R.id.bucketItems);
    }

    private ListView getItemsView() {
        return (ListView)findViewById(R.id.items);
    }

    private void addItemToBucket(String itemName) {
        Log.i("OrderMain", "Adding item to bucket: " + itemName);
        MainActivity.getSessionManager().addItemToBucket(itemName);
    }

    @Override
    public void updateResultPage(HttpResponse response) {
        Log.i("OrderMain","Response from request: " + response);
        if(response != null){
            Log.i("OrderMain", "Updating result page");
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
        ListView itemsList = getItemsView();
        if(items != null){
            orderItems = new String[items.length()];
            Log.i("OrderMain","Items JSON length: " + items.length());
            for(int i=0; i<items.length(); i++)
            {
                JSONObject actItem = items.getJSONObject(i);
                orderItems[i] = actItem.getString("shortName") + "," + actItem.getString("priceHu");
            }
            itemsAdapter = new CustomArrayAdapter(getApplicationContext(), R.layout.row, orderItems, this);
        }else{
            Log.i("OrderMain","Items NULL");
            orderItems = new String[1];
            orderItems[0] = "items NOT FOUND";
            itemsAdapter = new CustomArrayAdapter(getApplicationContext(), R.layout.row, orderItems, this);
        }
        itemsList.setAdapter(itemsAdapter);
    }

    private class CustomArrayAdapter extends ArrayAdapter<String>{

        protected Context context;

        protected View.OnClickListener onClickListener;

        public CustomArrayAdapter(Context context, int textViewResourceId,
                                  String[] objects, View.OnClickListener onClickListener){
            super(context, textViewResourceId, objects);
            Log.i("OrderMain","CustomArrayAdapter objects length: " + objects.length);
            this.context = context;
            this.onClickListener = onClickListener;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.textOfItems);
            TextView priceView = (TextView) rowView.findViewById(R.id.priceOfItems);
            Button orderButton = (Button) rowView.findViewById(R.id.buttonItem);
            orderButton.setOnClickListener(onClickListener);
            Log.i("OrderMain", "Selecting item in position: " + position );
            String item = getItem(position);
            Log.i("OrderMain", "Actual item in position: " + position + " item: " + item);
            String[] values = item.split(",");
            String name = values[0];
            String price = "ár: " + values[1];
            textView.setText(name);
            priceView.setText(price);
            return rowView;
        }

    }

    private class BucketArrayAdapter extends CustomArrayAdapter{

        public BucketArrayAdapter(Context context, int textViewResourceId,
                                  String[] objects, View.OnClickListener onClickListener){
            super(context, textViewResourceId, objects, onClickListener);
            Log.i("OrderMain","BucketArrayAdapter objects length: " + objects.length);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.textOfItems);
            //TextView priceView = (TextView) rowView.findViewById(R.id.priceOfItems);
            Button orderButton = (Button) rowView.findViewById(R.id.buttonItem);
            orderButton.setOnClickListener(onClickListener);
            orderButton.setText("Törlés");
            Log.i("OrderMain", "Selecting item in position: " + position );
            String item = getItem(position);
            Log.i("OrderMain", "Actual item in position: " + position + " item: " + item);
            textView.setText(item);

            return rowView;
        }

    }

}
