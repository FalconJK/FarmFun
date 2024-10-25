package com.example.user.farm.Consumer.Adapter;

/**
 * Created by user on 2018/2/28.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.user.farm.DetailActivity.CommodityActivity;
import com.example.user.farm.Funtional.ListImgSet;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainCommdityAdapter extends RecyclerView.Adapter<MainCommdityAdapter.ViewHolder> {

    private JSONArray jsonArray;
    private Context context;
    private int layoutId;
    private RequestQueue requestQueue;


    public MainCommdityAdapter(Context context, int layoutId, JSONArray jsonArray,RequestQueue requestQueue) {
        this.jsonArray = jsonArray;
        this.context = context;
        this.layoutId = layoutId;
        this.requestQueue=requestQueue;
    }

    @Override
    public MainCommdityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final View view = holder.mView;
        // 註冊項目點擊事件
        try {
            final JSONObject jsonObject = jsonArray.getJSONObject(position);
            final String CropID=jsonObject.getString("CropID");

            ListImgSet.set(requestQueue,holder.img, jsonObject.getString("Photo"));
            holder.title.setText(jsonObject.getString("CropName"));
            holder.price.setText(String.format("%s元/%s",
                    jsonObject.getString("Price"),
                    jsonObject.getString("P_unit")
            ));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommodityActivity.class);
                    intent.putExtra("CropID",CropID);
                    context.startActivity(intent);
                    History.addata(context,jsonObject.toString());
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
