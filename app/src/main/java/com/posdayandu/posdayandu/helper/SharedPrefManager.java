package com.posdayandu.posdayandu.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefManager {
    public static final String id = "ID";
    public static final String NAME = "name";
    public static final String FCM = "fcm";
    public static final String SALDO = "saldo";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(id, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static String getFCM(Context context) {
        return getSharedPreference(context).getString(FCM, "");
    }

    public static void setFCM(Context context, String fcm) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(FCM, fcm);
        editor.apply();
    }

    public static void setSaldo(Context context, String saldo) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(SALDO, saldo);
        editor.apply();
    }

    public static String getSALDO(Context context) {
        return getSharedPreference(context).getString(SALDO, "");
    }

    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setid(Context context, String idku) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(id, idku);
        editor.apply();
    }

    public static String getId(Context context) {
        return getSharedPreference(context).getString(id, "errors");
    }

    public void saveSPString(String keySP, String value) {
        editor.putString(keySP, value);
        editor.commit();
    }

    public void saveSPBoolean(String keySP, Boolean value) {
        editor.putBoolean(keySP, value);
        editor.commit();
    }

    public String getname() {
        return sharedPreferences.getString(NAME, "");
    }
}