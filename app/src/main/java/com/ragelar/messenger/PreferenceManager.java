package com.ragelar.messenger;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    SharedPreferences preferences;

    // context
    Context _context;

    // editor

    SharedPreferences.Editor editor;

    // mode
    int PRIVATE_MODE = 0;

    // filename
    public static final String PREFERENCES_NAME = "config";

    // keys
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_PRIVATE_KEY = "privateKey";
    public static final String KEY_SESSION = "session";
    public static final String KEY_SHARED_KEY = "sharedKey";
    public static final String KEY_NICKNAME = "nickName";

    public PreferenceManager(Context context){
        this._context = context;
        preferences = _context.getSharedPreferences(PREFERENCES_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void setUserName(String userName){
        editor.putString(KEY_USERNAME, userName);
        editor.commit();
    }

    public void setNickName(String nickName){
        editor.putString(KEY_NICKNAME, nickName);
        editor.commit();
    }

    public void setSession(String session){
        editor.putString(KEY_SESSION, session);
        editor.commit();
    }

    public void setSharedKey(String sharedKey){
        editor.putString(KEY_SHARED_KEY, sharedKey);
        editor.commit();
    }

    public void logIn(String privateKey, String session){
        editor.putString(KEY_PRIVATE_KEY, privateKey);
        editor.putString(KEY_SESSION, session);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserName(){
        return preferences.getString(KEY_USERNAME, "undefined");
    }

    public String getKeyPrivateKey(){
        return preferences.getString(KEY_PRIVATE_KEY, "undefined");
    }

    public String getSession(){
        return preferences.getString(KEY_SESSION, "undefined");
    }

    public String getSharedKey(){
        return preferences.getString(KEY_SHARED_KEY, "undefined");
    }

    public String getNickName() { return preferences.getString(KEY_NICKNAME, "undefined");}

    public void clearSession(){
        editor.clear();
        editor.commit();
    }


}
