package com.example.user.farm.Consumer.Fragment.MainFragment.Fragment03FragmentList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.Consumer.Actitvity.InsertStoreActivity;
import com.example.user.farm.Consumer.Adapter.CommodityAdapter;
import com.example.user.farm.Producer.Activity.ProducerMainActivity;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Solinari on 2016/12/31.
 */

public class FragmentList_Two extends Fragment implements View.OnClickListener {
    private RequestQueue queue;
    private LinearLayout seller;
    private RecyclerView recyclerView;
    private String StoreID = null;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_list3__two, container, false);
        context = getContext();
        StoreID = AccountData.getbelong_StoreID(context);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView3_2);
        seller = (LinearLayout) view.findViewById(R.id.seller);
        queue = Volley.newRequestQueue(context);
        seller.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Read_Store_All_Crop?StoreID=" + StoreID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray result = response.getJSONArray("Crop");
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
        queue.add(jsonObjectRequest);
    }

    public void setRecyclerView(JSONArray jsonArray) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new CommodityAdapter(recyclerView.getContext(), R.layout.item_linear_commity, jsonArray, queue));
        recyclerView.setVisibility(View.VISIBLE);//資料呈現
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.seller:
                if (AccountData.isseller(getContext()))
                    startActivity(new Intent(getActivity(), ProducerMainActivity.class));
                else
                    beseller();
                break;
        }
    }

    private void beseller() {
        new AlertDialog.Builder(context)
                .setTitle("賣家註冊")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("是否新建店家及農場，成為賣家?")
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), InsertStoreActivity.class));
                    }
                })
                .setNegativeButton("不用了，謝謝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
