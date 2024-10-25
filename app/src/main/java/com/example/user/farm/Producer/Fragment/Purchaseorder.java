package com.example.user.farm.Producer.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.Funtional.JsonRequest;
import com.example.user.farm.Producer.Adapter.PurchaseAdapter;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by user on 2018/3/16.
 */

public class Purchaseorder extends Fragment {

    private RequestQueue queue;
    private Context context;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.produce_purchaseorder, container, false);
        setupComponent(view);
        getOrderData(AccountData.getbelong_StoreID(context));

        return view;
    }

    private void setupComponent(View view) {
        context = getContext();
        queue = Volley.newRequestQueue(context);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    private void getOrderData(String storeid) {
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Read_Store_Receive_Order?StoreID=" + storeid;
        Log.d("order", storeid);
        JsonRequest request = new JsonRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                try {
                    JsonArray jsonArray = response.get("result").getAsJsonArray();
                    Log.d("order", response.toString());
                    setData(jsonArray);
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    private void setData(JsonArray jsonArray) {
        recyclerView.setAdapter(new PurchaseAdapter(recyclerView.getContext(), R.layout.item_linear_purchaseorder, jsonArray, queue));
    }

}
