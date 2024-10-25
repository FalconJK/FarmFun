package com.example.user.farm.Funtional;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.user.farm.R;

import org.json.JSONObject;

/**
 * Created by user on 2018/3/28.
 */

public class ListImgSet {
    private RequestQueue queue;
    private Context context;

    public ListImgSet(RequestQueue queue, Context context) {
        this.queue = queue;
        this.context = context;
    }

    public static void set(RequestQueue queue, final ImageView imageView, String photoID) {
        final Context context = imageView.getContext();
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Read_Small_Photo?PhotoID=" + photoID;

        Boolean notNull = !photoID.equals("null");
        Boolean notEmpty = !photoID.equals("");

        if (notNull && notEmpty) {
            Glide.with(context).load(R.drawable.giphy).into(imageView);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Success", response + "");
                            try {
                                byte[] imgg = Base64.decode(response.getString("Small").replace(" ", "+"), Base64.DEFAULT);
                                Glide.with(context).load(imgg).into(imageView);
                            } catch (Exception e) {
                                Glide.with(context).load(R.drawable.noimageavailable).into(imageView);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //當發生錯誤時，提示使用者發生錯誤
                    Log.d("ListImgSetError", "Error");
                    Glide.with(context).load(R.drawable.noimageavailable).into(imageView);
                }
            }
            );
            //將Request加入RequestQueue
            queue.add(jsonObjectRequest);
        } else
            Glide.with(context).load(R.drawable.noimageavailable).into(imageView);
    }
}
