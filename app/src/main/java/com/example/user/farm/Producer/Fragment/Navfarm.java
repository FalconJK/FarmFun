package com.example.user.farm.Producer.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.Producer.Activity.AddFarmActivity;
import com.example.user.farm.Producer.Adapter.NavfarmAdapter;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * Created by user on 2018/3/16.
 */

public class Navfarm extends Fragment {
    private Context context;
    private RequestQueue queue;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.produce_navfarm, container, false);

        context = getActivity();
        queue = Volley.newRequestQueue(context);

        fab=(FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddFarmActivity.class));
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Read_Store_All_Farm?StoreID=" + AccountData.getbelong_StoreID(context);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonArray jsonArray = new JsonParser().parse(response)
                        .getAsJsonObject().get("Farm").getAsJsonArray();
                recyclerView.setAdapter(new NavfarmAdapter(recyclerView.getContext(), R.layout.item_linear_farm, jsonArray));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
}
