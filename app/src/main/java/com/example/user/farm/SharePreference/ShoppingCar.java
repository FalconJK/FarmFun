package com.example.user.farm.SharePreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.user.farm.Funtional.Item;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by example on 16/9/2.
 */


public class ShoppingCar {

    private static final String SHARED_PREF_Shopping = "Shopping";

    public static void add(Context context, String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_Shopping, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String Shopping = sharedPreferences.getString("Shopping", new JSONArray().toString());

        JsonArray jsonarray = new JsonParser().parse(Shopping).getAsJsonArray();
        JsonObject jsonobject = new JsonParser().parse(string).getAsJsonObject();


        String CropID = String.format("\"CropID\":\"%s\"", jsonobject.get("CropID").getAsString());
        if (!Shopping.contains(CropID)) {
            jsonarray.add(jsonobject);
        }


        editor.putString("Shopping", jsonarray.toString());
        editor.commit();
    }

    public static Boolean contain(Context context, String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_Shopping, Context.MODE_PRIVATE);
        String Shopping = sharedPreferences.getString("Shopping", new JSONArray().toString());

        String CropID = String.format("\"CropID\":\"%s\"", string);
        if (Shopping.contains(CropID)) {
            return true;
        } else
            return false;
    }

    public static void remove(Context context, String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_Shopping, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String Shopping = sharedPreferences.getString("Shopping", new JSONArray().toString());

        JsonArray jsonarray = new JsonParser().parse(Shopping).getAsJsonArray();
        String target = new JsonParser().parse(string).getAsJsonObject().get("CropID").getAsString();

        for (int i = 0; i < jsonarray.size(); i++) {
            JsonObject jsonObject = jsonarray.get(i).getAsJsonObject();
            String object = jsonObject.get("CropID").getAsString();
            if (object.equals(target)) {
                jsonarray.remove(jsonObject);
            }
        }

        editor.putString("Shopping", jsonarray.toString());
        editor.commit();
    }

    public static void update(Context context, String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_Shopping, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Shopping", String.valueOf(new JsonParser().parse(string).toString()));
        editor.commit();
    }


    public static String getdata(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_Shopping, Context.MODE_PRIVATE);
        String get = sharedPreferences.getString("Shopping", String.valueOf(new JSONArray()));
        return get;
    }

}


//[
//  {
//      "CropID": "CropID",
//      "Count": "Count",
//      "Photo": "Photo",
//      "CropName": "CropName",
//      "Price": "Price"
//  }
//]
