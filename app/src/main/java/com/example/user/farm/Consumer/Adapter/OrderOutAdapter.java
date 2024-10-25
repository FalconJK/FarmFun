package com.example.user.farm.Consumer.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.user.farm.Funtional.JsonRequest;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * Created by user on 2018/4/29.
 */
public class OrderOutAdapter extends RecyclerView.Adapter<OrderOutAdapter.ViewHolder> {

    private Context context;
    private int layoutId;
    private JsonArray jsonArray;
    private RequestQueue queue;
    private Boolean r = false;

    public OrderOutAdapter(Context context, int layoutId, JsonArray jsonArray, RequestQueue queue, Boolean r) {
        this.context = context;
        this.layoutId = layoutId;
        this.jsonArray = jsonArray;
        this.queue = queue;
        this.r = r;
    }

    public OrderOutAdapter(Context context, int layoutId, JsonArray jsonArray, RequestQueue queue) {
        this.context = context;
        this.layoutId = layoutId;
        this.jsonArray = jsonArray;
        this.queue = queue;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderOutAdapter.ViewHolder holder, final int position) {
        View view = holder.mView;
        JsonObject data = jsonArray.get(position).getAsJsonObject();

        JsonArray order = data.get("Order").getAsJsonArray();
        final String StoreID = data.get("StoreID").getAsString();
        String StoreName = data.get("StoreName").getAsString();

        holder.storename.setText(StoreName);
        holder.order.setAdapter(new OrderInAdapter(R.layout.item_linear_dealin, order, context));

        holder.btn.setVisibility(r?View.VISIBLE:View.GONE);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (r)
                    new AlertDialog.Builder(context)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("確認收貨")
                            .setMessage("確認已收到此商店商品")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    queue.add(Order_Get(StoreID, position));
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return jsonArray.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView storeicon;
        public RecyclerView order;
        public TextView storename;
        public Button btn;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            storeicon = (ImageView) view.findViewById(R.id.storeicon);
            storename = (TextView) view.findViewById(R.id.storename);
            order = (RecyclerView) view.findViewById(R.id.recyclerView);
            btn=(Button)view.findViewById(R.id.recieve);
            order.setLayoutManager(new LinearLayoutManager(view.getContext()));
        }
    }

    private JsonRequest Order_Get(final String StoreID, final int pos) {
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Order_Get";
        JsonRequest request = new JsonRequest(Request.Method.POST, url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if (response.get("result").getAsString().equals("Successfully_Updated")){
                    jsonArray.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, getItemCount());
//                    refresh.refresh();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            // 携带参数
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                //post的欄位放這
                hashMap.put("StoreID", StoreID);
                hashMap.put("CustmerID", AccountData.getID(context));
                return hashMap;
            }
        };
        return request;
    }


}