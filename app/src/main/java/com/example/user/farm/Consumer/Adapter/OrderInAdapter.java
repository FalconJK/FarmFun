package com.example.user.farm.Consumer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.Funtional.ListImgSet;
import com.example.user.farm.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by user on 2018/4/29.
 */
public class OrderInAdapter extends RecyclerView.Adapter<OrderInAdapter.ViewHolder> {
    private int layoutId;
    private JsonArray jsonArray;
    private RequestQueue queue;


    public OrderInAdapter(int layoutId, JsonArray jsonArray, Context context) {
        this.layoutId = layoutId;
        this.jsonArray = jsonArray;
        this.queue = Volley.newRequestQueue(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JsonObject data = jsonArray.get(position).getAsJsonObject();

        String OrderID = data.get("OrderID").getAsString();
        String CropID = data.get("CropID").getAsString();
        String CropName = data.get("CropName").getAsString();
        String Photo = data.get("Photo").getAsString();
        String Quantity = data.get("Quantity").getAsString();
        String Price = data.get("Price").getAsString();

        ListImgSet.set(queue,holder.photo,Photo);
        holder.name.setText(CropName);
        holder.count.append(Quantity);
        holder.price.append(Price);
    }

    @Override
    public int getItemCount() {
        return jsonArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView photo;
        public TextView name;
        public TextView count;
        public TextView price;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            photo = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            count = (TextView) view.findViewById(R.id.count);
            price = (TextView) view.findViewById(R.id.price);

        }
    }
}