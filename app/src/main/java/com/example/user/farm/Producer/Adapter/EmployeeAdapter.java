package com.example.user.farm.Producer.Adapter;

/**
 * Created by user on 2018/2/28.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.user.farm.Funtional.ListImgSet;
import com.example.user.farm.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private Context context;
    private int layoutId;
    private JsonArray jsonArray;
    private RequestQueue queue;

    public EmployeeAdapter(Context context, int layoutId, JsonArray jsonArray, RequestQueue queue) {
        this.context = context;
        this.layoutId = layoutId;
        this.jsonArray = jsonArray;
        this.queue = queue;
    }

    @Override
    public EmployeeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    //    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final View view = holder.mView;
        JsonObject jsonObject = jsonArray.get(position).getAsJsonObject();
        String level = null;
        final String phone = jsonObject.get("SellerPhone").getAsString();
        switch (jsonObject.get("Level").getAsInt()) {
            case 0:
                level = " 員工 ";
                break;
            case 1:
                level = " 幹部 ";
                break;
            case 2:
                level = "負責人";
                break;
        }
        holder.name.setText(jsonObject.get("SellerName").getAsString());
        holder.level.setText(level);
        holder.phone.setText(phone);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent();
                it.setAction(Intent.ACTION_VIEW);
                it.setData(Uri.parse("tel:"+phone));
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        // 設定為固定7個項目
        return jsonArray.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView name;
        public TextView level;
        public TextView phone;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.employee_name);
            level = (TextView) view.findViewById(R.id.employee_level);
            phone = (TextView) view.findViewById(R.id.employee_phone);

        }
    }

}
