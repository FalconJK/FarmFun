package com.example.user.farm.SharePreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by example on 16/9/2.
 */

public class History {

    private static final String SHARED_PREF_History = "history";

    public static void addata(Context context, String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_History, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String history = sharedPreferences.getString("history", new JSONArray().toString());

        JsonArray jsonarray = new JsonParser().parse(history).getAsJsonArray();
        JsonElement jsonobject = new JsonParser().parse(string).getAsJsonObject();

        if (history.contains(string)) {
            jsonarray.remove(jsonobject);
        }

        jsonarray.add(jsonobject);
        editor.putString("history", jsonarray.toString());
        editor.commit();
    }

    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_History, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("history", String.valueOf(new JSONArray()));
        editor.commit();
    }

    public static JSONArray getdata(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_History, Context.MODE_PRIVATE);
        String get = sharedPreferences.getString("history", String.valueOf(new JSONArray()));
        try {
            JSONArray data = new JSONArray(get);
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
