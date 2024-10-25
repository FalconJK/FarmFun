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

import com.example.user.farm.DetailActivity.CommodityActivity;

public class Cropfarm extends RecyclerView.Adapter<Cropfarm.ViewHolder> {

    private Context context;
    // 使用的畫面配置資源編號
    private int layoutId;

    public Cropfarm(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
    }

    @Override
    public Cropfarm.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent. getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder,
                                 final int position) {
        final View view = holder.mView;
        // 註冊項目點擊事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view,
//                        "Item position: " + position,
//                        Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CommodityActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // 設定為固定7個項目
        return 20;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;


        public ViewHolder(View view) {
            super(view);
            mView = view;

        }
    }

}
