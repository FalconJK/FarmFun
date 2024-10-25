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
import com.example.user.farm.DetailActivity.StoreActivity;
import com.example.user.farm.Funtional.ListImgSet;
import com.example.user.farm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private Context context;
    // 使用的畫面配置資源編號
    private int layoutId;
    private JSONArray jsonArray;
    private RequestQueue queue;

    public StoreAdapter(Context context, int layoutId, JSONArray jsonArray, RequestQueue queue) {
        this.context = context;
        this.layoutId = layoutId;
        this.jsonArray = jsonArray;
        this.queue = queue;
    }

    @Override
    public StoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final View view = holder.mView;
        try {

            JSONObject data = jsonArray.getJSONObject(position);
            JSONArray comm = data.getJSONArray("Crop");

            data.getString("StoreID");

            final String StoreID = data.getString("StoreID");
            ListImgSet.set(queue,holder.logo, data.getString("Logo"));
            holder.name.setText(data.getString("StoreName"));
            holder.count.setText(data.getString("CropCount"));

            for (int i = 0; i < 4; i++) {
                JSONObject comd = comm.getJSONObject(i);

                final String CropID = comd.getString("CropID");

                ListImgSet.set(queue,holder.comm[i], comd.getString("Logo"));
                holder.price[i].setText(comd.getString("Price"));

                holder.comm[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CommodityActivity.class);
                        intent.putExtra("CropID", CropID);
                        context.startActivity(intent);
                    }
                });
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StoreActivity.class);
                    intent.putExtra("Data", "GetOnNetwork");
                    intent.putExtra("StoreID", StoreID);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView logo;
        public ImageView[] comm = new ImageView[4];
        public TextView name;
        public TextView count;
        public TextView[] price = new TextView[4];

        public ViewHolder(View view) {
            super(view);
            mView = view;
            logo = (ImageView) view.findViewById(R.id.storeitem_logo);
            name = (TextView) view.findViewById(R.id.storeitem_name);
            count = (TextView) view.findViewById(R.id.storeitem_count);

            comm[0] = (ImageView) view.findViewById(R.id.storeitem_comm1_img);
            comm[1] = (ImageView) view.findViewById(R.id.storeitem_comm2_img);
            comm[2] = (ImageView) view.findViewById(R.id.storeitem_comm3_img);
            comm[3] = (ImageView) view.findViewById(R.id.storeitem_comm4_img);

            price[0] = (TextView) view.findViewById(R.id.storeitem_comm1_price);
            price[1] = (TextView) view.findViewById(R.id.storeitem_comm2_price);
            price[2] = (TextView) view.findViewById(R.id.storeitem_comm3_price);
            price[3] = (TextView) view.findViewById(R.id.storeitem_comm4_price);
        }
    }

}
