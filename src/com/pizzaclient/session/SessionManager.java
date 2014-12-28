package com.pizzaclient.session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tibi on 2014.12.28..
 */
public class SessionManager {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

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

}
