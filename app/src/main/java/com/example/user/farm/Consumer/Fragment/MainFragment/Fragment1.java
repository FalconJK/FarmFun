package com.example.user.farm.Consumer.Fragment.MainFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.Consumer.Adapter.MainCommdityAdapter;
import com.example.user.farm.Funtional.Imageitem;
import com.example.user.farm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tomoya on 4/5/17.
 */

public class Fragment1 extends Fragment {
    private Context context = getActivity();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
//        imgs.clear();for (int i=1;i<num+1;i++)  get(String.format("%d",i),i);
        requestQueue = Volley.newRequestQueue(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 設定畫面配置元件

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return false;
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Read_All_Crop";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray result = response.getJSONArray("result");
                    setRecyclerView(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void setRecyclerView(JSONArray jsonArray) {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new MainCommdityAdapter(getActivity(), R.layout.item_grid_commity, jsonArray, requestQueue));
        progressBar.setVisibility(View.GONE);//Loading圖隱藏
        recyclerView.setVisibility(View.VISIBLE);//資料呈現
    }


}