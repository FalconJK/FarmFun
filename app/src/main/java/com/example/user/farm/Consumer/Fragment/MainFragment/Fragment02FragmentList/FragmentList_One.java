package com.example.user.farm.Consumer.Fragment.MainFragment.Fragment02FragmentList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.DetailActivity.CommodityActivity;

import com.example.user.farm.Funtional.JsonRequest;
import com.example.user.farm.Funtional.ListImgSet;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.History;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by Solinari on 2016/12/31.
 */

public class FragmentList_One extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private Context context;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SegmentedGroup segmentedGroup;


    private JsonArray all;
    private JsonArray rice;
    private JsonArray vegetable;
    private JsonArray fruit;
    private JsonArray flower;
    private JsonArray others;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_list__one, container, false);
        setupComponent(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Read_All_Crop";
        requestQueue = Volley.newRequestQueue(getActivity());
        JsonRequest jsonObjectRequest = new JsonRequest(Request.Method.GET, url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                JsonArray result = response.get("result").getAsJsonArray();
                setRecyclerView(result);
                cleandata();
                assortdata(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void cleandata() {
        all = new JsonArray();
        rice = new JsonArray();
        vegetable = new JsonArray();
        fruit = new JsonArray();
        flower = new JsonArray();
        others = new JsonArray();
    }

    private void assortdata(JsonArray result) {
        all = result;
        for (int i = 0; i < result.size(); i++) {
            JsonObject object = result.get(i).getAsJsonObject();
            switch (object.get("CropType").getAsString()) {
                case "米":
                    rice.add(object);
                    break;
                case "蔬菜":
                    vegetable.add(object);
                    break;
                case "水果":
                    fruit.add(object);
                    break;
                case "花卉":
                    flower.add(object);
                    break;
                case "其他":
                    others.add(object);
                    break;
            }
        }
    }

    private void setupComponent(View view) {
        context = getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        segmentedGroup = (SegmentedGroup) view.findViewById(R.id.segmented);
        segmentedGroup.setOnCheckedChangeListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    public void setRecyclerView(JsonArray jsonArray) {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new RecyclerViewAdapter(recyclerView.getContext(), R.layout.item_grid_commity, jsonArray, requestQueue));
        progressBar.setVisibility(View.GONE);//Loading圖隱藏
        segmentedGroup.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);//資料呈現
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.all:
                setRecyclerView(all);
                break;
            case R.id.rice:
                setRecyclerView(rice);
                break;
            case R.id.vegetable:
                setRecyclerView(vegetable);
                break;
            case R.id.Fruit:
                setRecyclerView(fruit);
                break;
            case R.id.flower:
                setRecyclerView(flower);
                break;
            case R.id.others:
                setRecyclerView(others);
                break;
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private JsonArray jsonArray;
        private Context context;
        private int layoutId;
        private RequestQueue requestQueue;

        public RecyclerViewAdapter(Context context, int layoutId, JsonArray jsonArray, RequestQueue requestQueue) {
            this.jsonArray = jsonArray;
            this.context = context;
            this.layoutId = layoutId;
            this.requestQueue = requestQueue;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(layoutId, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final View view = holder.mView;
            // 註冊項目點擊事件

            final JsonObject jsonObject = jsonArray.get(position).getAsJsonObject();
            final String CropID = jsonObject.get("CropID").getAsString();

            ListImgSet.set(requestQueue, holder.img, jsonObject.get("Photo").getAsString());
            holder.title.setText(jsonObject.get("CropName").getAsString());
            holder.price.setText(String.format("%s元/%s",
                    jsonObject.get("Price").getAsString(),
                    jsonObject.get("P_unit").getAsString()
            ));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommodityActivity.class);
                    intent.putExtra("CropID", CropID);
                    context.startActivity(intent);
                    History.addata(context, jsonObject.toString());
                }
            });
        }

        @Override
        public int getItemCount() {
            // 設定為固定7個項目
            return jsonArray.size();
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







































