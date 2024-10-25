package com.example.user.farm.Consumer.Fragment.MainFragment.Fragment02FragmentList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestListener;
import com.example.user.farm.Consumer.Adapter.StoreAdapter;
import com.example.user.farm.Funtional.ListImgSet;
import com.example.user.farm.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Solinari on 2016/12/31.
 */

public class FragmentList_Two extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Context context;
    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_list__two, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        // 設定 RecylerView 使用的 Adapter 物件
        context = recyclerView.getContext();
        return view;
    }

    @Override
    public void onStart() {
        queue = Volley.newRequestQueue(context);
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Read_All_Store";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(new StoreAdapter(recyclerView.getContext(), R.layout.item_linear_store, response.getJSONArray("result"),queue));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
        super.onStart();
    }
}
