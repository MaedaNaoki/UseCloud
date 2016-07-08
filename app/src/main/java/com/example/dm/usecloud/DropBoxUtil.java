package com.example.dm.usecloud;

import android.content.Context;
import android.content.SharedPreferences;

import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

public class DropBoxUtil {
    private static final String TOKEN = "t1oLwT3GkZ4AAAAAAAAFShA9S4MVq_JJc2QJ_KGt1ZrKBpjLkU3v-Kamp0HefHsP";
    private static final String PREF_NAME = "dropbox";
    public static final String APPKEY = "hkylq6f7kmbxtno";
    public static final String APPKEYSECRET = "ejwik68xc6xizee";
    private Context context;

    public DropBoxUtil(Context context) {
        this.context = context;
    }

    public void storeOauth2AccessToken(String secret){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, secret);
        editor.commit();
    }

    public AndroidAuthSession loadAndroidAuthSession() {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        String token = preferences.getString(TOKEN, null);
        if (token != null) {
            AppKeyPair appKeys = new AppKeyPair(APPKEY, APPKEYSECRET);
            return new AndroidAuthSession(appKeys,token);
        } else {

            return null;
        }
    }

    public boolean hasLoadAndroidAuthSession() {
        return loadAndroidAuthSession() != null;
    }

}