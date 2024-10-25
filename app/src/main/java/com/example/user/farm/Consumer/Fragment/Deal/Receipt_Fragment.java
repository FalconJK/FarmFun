package com.example.user.farm.Consumer.Fragment.Deal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.Consumer.Adapter.OrderOutAdapter;
import com.example.user.farm.Funtional.JsonRequest;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by user on 2018/1/24.
 */
//待收貨
public class Receipt_Fragment extends Fragment {
    private Context context;
    private RequestQueue queue;
    private RecyclerView recyclerView;
    private String ID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal_receipt, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.receipt);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        context = getActivity();
        queue = Volley.newRequestQueue(context);
        ID = AccountData.getID(context);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Read_Customer_Receive_Order?CustomerID=" + ID;
        JsonRequest jsonRequest = new JsonRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                try {
                    JsonArray data = response.get("result").getAsJsonArray();
                    setRecyclerView(data);
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonRequest);
    }

    public void setRecyclerView(JsonArray data) {
        recyclerView.setVisibility(View.GONE);
        recyclerView.setAdapter(new OrderOutAdapter(context,R.layout.item_linear_deal, data,queue,true));
        recyclerView.setVisibility(View.VISIBLE);
    }

}
