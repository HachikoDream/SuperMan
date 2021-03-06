package com.dreamspace.superman.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.dreamspace.superman.model.Catalog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * SharedPreferences工具类
 *
 * @author wangdd
 * @date 2015-2-1
 */
public class PreferenceUtils {

    public static class Key {
        public static final String ACCOUNT = "ACCOUNT";
        public static final String AVATAR = "AVATAR";
        public static final String REALNAME="REALNAME";
        public static final String SEX="SEX";
        public static final String CLASSIFY = "CLASSIFY";
        public static final String ACCESS="access_token";
        public static final String PHONE="PHONE";
        public static final String MAS_ID="MAS_ID";
        public static final String QINIU_SOURCE="QINIU_CODE";
        public static final String MAST_STATE="MAST_STATE";
        public static final String UID="UID";
        public static final String AVATER_AVAILABLE="avater_ava";
        public static final String AVATER_CACHE_PATH="avater_cache_path";
        public static final String SM_INFO_READ="sm_info_read";

    }

    public static final String DEFAULT_STRING = "";
    public static final int DEFAULT_INT = 0;
    public static final boolean DEFAULT_BOOLEAN = false;
    public static final long DEFAULT_LONG = 0;
    public static final float DEFAULT_FLOAT = 0.0f;


    public static String getString(Context context, String key) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getString(key, DEFAULT_STRING);
    }
    public static String getString(Context context, String key,String defaultvalue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getString(key, defaultvalue);
    }

    public static void putString(Context context, final String key,
                                 final String value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putString(key, value).commit();
    }

    public static boolean getBoolean(Context context, final String key) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getBoolean(key, DEFAULT_BOOLEAN);
    }

    public static boolean hasKey(Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(
                key);
    }

    public static void putBoolean(Context context, final String key,
                                  final boolean value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key, value).commit();
    }

    public static void putInt(Context context, final String key,
                              final int value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, final String key) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getInt(key, DEFAULT_INT);
    }

    public static void putFloat(Context context, final String key,
                                final float value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putFloat(key, value).commit();
    }

    public static float getFloat(Context context, final String key) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getFloat(key, DEFAULT_FLOAT);
    }

    public static void putLong(Context context, final String key,
                               final long value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, final String key) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getLong(key, DEFAULT_LONG);
    }

    public static void clearPreference(Context context,
                                       final SharedPreferences p) {
        final Editor editor = p.edit();
        editor.clear();
        editor.commit();
    }

    public static List<Catalog> getClassifyItems(Context context) {
        String content = getDefaultSp(context).getString(Key.CLASSIFY, "EMPTY");
        Gson gson = new Gson();
        return gson.fromJson(content, new TypeToken<List<Catalog>>() {
        }.getType());
    }

    public static void writeClassifyIntoSp(Context context, List<Catalog> items) {
        Gson gson = new Gson();
        Catalog catalog=new Catalog();
        catalog.setId(-1);
        catalog.setIcon("TEST");
        catalog.setName("精选");
        items.add(0, catalog);
        String content = gson.toJson(items);
        putString(context, Key.CLASSIFY, content);
    }
    public static void writeClassifyIntoSpForFirst(Context context) {
        Gson gson = new Gson();
        Catalog catalog=new Catalog();
        catalog.setId(-1);
        catalog.setIcon("TEST");
        catalog.setName("精选");
        List<Catalog> mCata=new ArrayList<>();
        mCata.add(catalog);
        String content = gson.toJson(mCata);
        putString(context, Key.CLASSIFY, content);
    }

    public static SharedPreferences getDefaultSp(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
