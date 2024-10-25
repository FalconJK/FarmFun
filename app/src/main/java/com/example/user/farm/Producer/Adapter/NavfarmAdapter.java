package com.example.user.farm.Producer.Adapter;

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

import com.example.user.farm.DetailActivity.CommodityActivity;
import com.example.user.farm.DetailActivity.FarmActivity;
import com.example.user.farm.Funtional.GetImageSet;
import com.example.user.farm.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class NavfarmAdapter extends RecyclerView.Adapter<NavfarmAdapter.ViewHolder> {

    private Context context;
    private int layoutId;
    private JsonArray jsonArray;

    public NavfarmAdapter(Context context, int layoutId, JsonArray jsonArray) {
        this.context = context;
        this.layoutId = layoutId;
        this.jsonArray = jsonArray;
    }

    @Override
    public NavfarmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final View view = holder.mView;
        JsonObject jsonObject = jsonArray.get(position).getAsJsonObject();
        final String FarmID = jsonObject.get("FarmID").getAsString();

        GetImageSet.gisl(holder.img, jsonObject.get("Image").getAsString());
        holder.title.setText(jsonObject.get("FarmName").getAsString());
        holder.area.setText(jsonObject.get("Area").getAsString());
        holder.address.setText(jsonObject.get("Address").getAsString());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FarmActivity.class);
                intent.putExtra("Data", "GetOnNetwork");
                intent.putExtra("ID", FarmID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jsonArray.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView img;
        public TextView title;
        public TextView area;
        public TextView address;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            img = (ImageView) view.findViewById(R.id.linfarm_img);
            title = (TextView) view.findViewById(R.id.linfarm_title);
            area = (TextView) view.findViewById(R.id.linfarm_area);
            address = (TextView) view.findViewById(R.id.linfarm_address);

        }
    }

}
