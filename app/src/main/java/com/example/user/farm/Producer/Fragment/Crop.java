package com.example.user.farm.Producer.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.android.volley.toolbox.Volley;
import com.example.user.farm.DetailActivity.CommodityActivity;
import com.example.user.farm.Funtional.ListImgSet;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2018/3/16.
 */

public class Crop extends Fragment {

    private RequestQueue queue;
    private Context context;
    private RecyclerView recyclerView;
    private String StoreID = null;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.produce_cropfarm, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        context = getActivity();
        StoreID = AccountData.getbelong_StoreID(context);
        queue = Volley.newRequestQueue(context);
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

    private void setRecyclerView(JSONArray jsonArray) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
        recyclerView.setAdapter(new RecyclerViewAdapter(jsonArray));
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private JSONArray jsonArray;

        public RecyclerViewAdapter(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_grid_commity, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final View view = holder.mView;
            // 註冊項目點擊事件
            try {
                final JSONObject jsonObject = jsonArray.getJSONObject(position);
                final String CropID = jsonObject.getString("CropID");

                ListImgSet.set(queue, holder.img, jsonObject.getString("Photo"));
                holder.title.setText(jsonObject.getString("CropName"));
                holder.price.setText(String.format("%s元/%s",
                        jsonObject.getString("Price"),
                        jsonObject.getString("P_unit")
                ));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, CommodityActivity.class);
                        intent.putExtra("CropID", CropID);
                        context.startActivity(intent);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            // 設定為固定7個項目
            return jsonArray.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public ImageView img;
            public TextView title;
            public TextView price;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                img = (ImageView) view.findViewById(R.id.grid_crop_photo);
                title = (TextView) view.findViewById(R.id.grid_crop_title);
                price = (TextView) view.findViewById(R.id.grid_crop_price);
            }
        }


    }


}
