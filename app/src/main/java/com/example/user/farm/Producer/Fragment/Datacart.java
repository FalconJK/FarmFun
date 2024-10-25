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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.Producer.Adapter.DatacartAdapter;
import com.example.user.farm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2018/3/16.
 */

public class Datacart extends Fragment {
    private RequestQueue queue;
    private Context context;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.produce_datacart, container, false);
        setupComponent(view);
        return view;
    }

    private void setupComponent(View view) {
        context=getActivity();
        queue= Volley.newRequestQueue(context);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new DatacartAdapter(recyclerView.getContext(), R.layout.item_linear_datacart));
    }


}
