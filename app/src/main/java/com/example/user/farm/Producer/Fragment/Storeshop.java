package com.example.user.farm.Producer.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.DetailActivity.CommodityActivity;
import com.example.user.farm.Funtional.GetImageSet;
import com.example.user.farm.Producer.Adapter.StoreshopAdapter;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2018/3/16.
 */

public class Storeshop extends Fragment {

    private Context context;
    private RequestQueue queue;
    private ImageView imageView;
    private TextView name, phone, count, address, begintime, endtime;
    private String StoreID;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.produce_storeshop, container, false);
        setupComponent(view);
        return view;
    }

    private void setupComponent(View view) {
        context = getActivity();
        queue = Volley.newRequestQueue(context);
        StoreID = AccountData.getbelong_StoreID(context);
        imageView = (ImageView) view.findViewById(R.id.storedetail_photo);
        name = (TextView) view.findViewById(R.id.storedetail_name);
        phone = (TextView) view.findViewById(R.id.storedetail_phone);
        count = (TextView) view.findViewById(R.id.storedetail_count);
        address = (TextView) view.findViewById(R.id.storedetail_address);
        begintime = (TextView) view.findViewById(R.id.storedetail_begintime);
        endtime = (TextView) view.findViewById(R.id.storedetail_endtime);
    }

    public void onStart() {
        super.onStart();
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Read_Store?StoreID=" + StoreID;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                setData(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    private void setData(JsonObject jsonObject) {
        GetImageSet.gisl(imageView, jsonObject.get("Logo").getAsString());
        name.append(jsonObject.get("StoreName").getAsString());
        phone.append(jsonObject.get("Phone").getAsString());
        count.append(jsonObject.get("CropCount").getAsString());
        address.append(jsonObject.get("Address").getAsString());
        begintime.append(jsonObject.get("WorkTimeStart").getAsString());
        endtime.append(jsonObject.get("WorkTimeEnd").getAsString());
    }


}