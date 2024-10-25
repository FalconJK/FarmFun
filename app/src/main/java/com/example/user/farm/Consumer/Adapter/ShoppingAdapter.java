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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.DetailActivity.CommodityActivity;
import com.example.user.farm.Funtional.Item;
import com.example.user.farm.Funtional.ListImgSet;
import com.example.user.farm.Producer.Fragment.Crop;
import com.example.user.farm.R;
import com.google.gson.JsonArray;
import com.jmf.addsubutils.AddSubUtils;

import java.util.ArrayList;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {

    private Context context;
    private int layoutId;
    private ArrayList<Item> items;
    private RequestQueue queue;

    public ShoppingAdapter(Context context, int layoutId, ArrayList<Item> items) {
        this.context = context;
        this.layoutId = layoutId;
        this.items = items;
        this.queue = Volley.newRequestQueue(context);
    }

    @Override
    public ShoppingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        View view = holder.mView;
        ListImgSet.set(queue, holder.img, items.get(position).getPhoto());

        holder.title.setText(items.get(position).getCropName());
        holder.price.append(items.get(position).getPrice());
        holder.checkBox.setChecked(items.get(position).getCheck());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                items.get(position).setCheck(isChecked);
            }
        });

        holder.addSubUtils
                .setPosition(position)
                .setCurrentNumber(items.get(position).getCount())
                .setOnChangeValueListener(new AddSubUtils.OnChangeValueListener() {
                    @Override
                    public void onChangeValue(int value, int position) {
                        items.get(position).setCount(value);

                    }
                });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,CommodityActivity.class)
                        .putExtra("CropID",items.get(position).getCropID()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public CheckBox checkBox;
        public ImageView img;
        public TextView title;
        public TextView price;
        public AddSubUtils addSubUtils;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            checkBox = (CheckBox) view.findViewById(R.id.check);
            img = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            addSubUtils = (AddSubUtils) view.findViewById(R.id.add_sub);
        }
    }

}
