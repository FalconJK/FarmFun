package com.example.user.farm.Producer.Adapter;

/**
 * Created by user on 2018/2/28.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.lang.reflect.Method;
import java.util.HashMap;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {

    private Context context;
    private int layoutId;
    private JsonArray jsonArray;
    private RequestQueue queue;

    public PurchaseAdapter(Context context, int layoutId, JsonArray jsonArray, RequestQueue queue) {
        this.context = context;
        this.layoutId = layoutId;
        this.jsonArray = jsonArray;
        this.queue = queue;
    }


    @Override
    public PurchaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {


        final View view = holder.mView;
        JsonObject data = jsonArray.get(position).getAsJsonObject();
        final JsonArray order = data.get("Order").getAsJsonArray();

        Integer pay = 0;

        String crop = "";
        String count = "";
        String singlepay = "";
        String singlesum = "";

        final String CustomerID = data.get("CustomerID").getAsString();
        String CustomerName = data.get("CustomerName").getAsString();
        String post = data.get("Post").getAsString();
        String Address = data.get("Address").getAsString();

        for (int i = 0; i < order.size(); i++) {
            JsonObject object = order.get(i).getAsJsonObject();

            Integer scount = object.get("Quantity").getAsInt();
            Integer spay = object.get("Price").getAsInt();

            crop += object.get("CropName").getAsString() + "\n";
            count += String.format("*%d\n", scount);
            singlepay += String.format("$%d\n", spay);
            singlesum += String.format("$%d\n", scount * spay);

            pay += scount * spay;
        }

        crop = crop.substring(0, crop.length() - 1);
        count = count.substring(0, count.length() - 1);
        singlepay = singlepay.substring(0, singlepay.length() - 1);
        singlesum = singlesum.substring(0, singlesum.length() - 1);

        holder.name.setText(CustomerName);
        holder.pay.setText(pay.toString());
        holder.crop.setText(crop);
        holder.count.setText(count);
        holder.singlepay.setText(singlepay);
        holder.singlesum.setText(singlesum);
        holder.address.append(post + Address);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("確認出貨")
                        .setMessage("確認出貨此顧客訂單")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                queue.add(OrderAccept(CustomerID,position));
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
        catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        // 設定為固定7個項目
        return jsonArray.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        TextView name;
        TextView pay;
        TextView crop;
        TextView count;
        TextView singlepay;
        TextView singlesum;
        TextView address;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = view.findViewById(R.id.storename);
            pay = view.findViewById(R.id.order_pay);
            crop = view.findViewById(R.id.order_crop);
            count = view.findViewById(R.id.order_count);
            singlepay = view.findViewById(R.id.order_singlepay);
            singlesum = view.findViewById(R.id.order_singlesum);
            address = view.findViewById(R.id.order_address);

        }
    }


    private JsonRequest OrderAccept(final String CustmerID, final int pos) {
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Order_Accept";
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
                hashMap.put("StoreID", AccountData.getbelong_StoreID(context));
                hashMap.put("CustmerID", CustmerID);
                return hashMap;
            }
        };
        return request;
    }

}
