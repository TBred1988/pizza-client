package com.pizzaclient.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Tibi on 2014.12.28..
 */
public class SessionManager {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String BUCKET = "bucket";


    private int SHARED_PREF_PRIVATE_MODE = 0;
    private static final String SHARED_PREF_PREFER_NAME = "UserPreferences";

    private Context context;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_PREFER_NAME, SHARED_PREF_PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void login(String userName, String password){
        editor.putString(USERNAME, userName);
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public String getUsername(){
        return sharedPreferences.getString(USERNAME, null);
    }

    public void clear(){
        sharedPreferences.edit().clear();
    }

    public void addItemToBucket(String name){
        Log.i("SessionManager", "Adding item to bucket: " + name);
        String bucket = sharedPreferences.getString(BUCKET, null);
        if( bucket == null ){
            Log.i("SessionManager", "Bucket items does not exists, adding to bucket: " + name);
            editor.putString(BUCKET, name);
            editor.commit();
        }else{
            if ( !bucket.contains( name ) ) {
                bucket = bucket + "," + name;
                Log.i("SessionManager", "Bucket modified value: " + bucket);
                editor.remove(BUCKET);
                editor.commit();
                Log.i("SessionManager", "Bucket cleared");
                editor.putString(BUCKET, bucket);
                editor.commit();
                Log.i("SessionManager", "Bucket updated with value: " + bucket);
            } else {
                Log.i("SessionManager", "Bucket already contains item: " + name);
            }
        }
    }

    public String[] bucketItems(){
        Log.i("SessionManager", "Get bucket items");
        String bucketValue = sharedPreferences.getString(BUCKET, null);
        Log.i("SessionManager", "Bucket value: " + bucketValue);
        String[] bucketItems = null;
        if(bucketValue != null){
            bucketItems = bucketValue.split(",");
        }

        return bucketItems;
    }

    public void initBucket() {
        Log.i("SessionManager", "Initializing bucket store");
        String bucketValue = sharedPreferences.getString(BUCKET, null);
        if(bucketValue != null){
            editor.remove(BUCKET);
            editor.commit();
            Log.i("SessionManager", "Bucket store cleared");
        }
    }
}
