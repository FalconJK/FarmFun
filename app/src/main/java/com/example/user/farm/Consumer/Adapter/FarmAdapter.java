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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.user.farm.DetailActivity.FarmActivity;
import com.example.user.farm.Funtional.ListImgSet;
import com.example.user.farm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.ViewHolder> {

    private Context context;
    private int layoutId;
    private JSONArray jsonArray;
    private RequestQueue queue;


    public FarmAdapter(Context context, int layoutId, JSONArray jsonArray,RequestQueue queue) {
        this.context = context;
        this.layoutId = layoutId;
        this.jsonArray = jsonArray;
        this.queue=queue;
    }

    @Override
    public FarmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent. getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final View view = holder.mView;

        try {
            JSONObject data=jsonArray.getJSONObject(position);
            final String FarmID=data.getString("FarmID");
            holder.title.setText(data.getString("FarmName"));
            holder.area.append(data.getString("Area"));
            holder.address.append(data.getString("Address"));
            ListImgSet.set(queue,holder.img,data.getString("Image"));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FarmActivity.class);
                    intent.putExtra("Data","GetOnNetwork");
                    intent.putExtra("ID",FarmID);
                    context.startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
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
