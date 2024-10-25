package com.example.user.farm.Funtional;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 2018/3/28.
 */

public class VolleyMethod{
    Context mContext;

    public VolleyMethod(Context mContext) {
        this.mContext = mContext;
    }
    public void PoST(){
        String url =" http://120.101.8.52/2017farmer/WebService1.asmx/Insert_Store";
        RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            jsonObject.get("DataTitle");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("请求错误:" + error.toString());
            }
        }) {
            // 携带参数
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                //post的欄位放這
                hashMap.put("欄位1", "資料1");
                hashMap.put("欄位1", "資料2");
                return hashMap;
            }
        };
        mRequestQueue.add(mStringRequest);
        GeT();
    }
    private void GeT(){
        RequestQueue queue = Volley.newRequestQueue(mContext);//創建一個RequestQueue
        String url ="http://120.101.8.52/2017farmer/WebService1.asmx/Read_Crop?CropID=6"+"123";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            response.get("DataTitle");
                            Log.d("Success",response+"");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //當發生錯誤時，提示使用者發生錯誤
                Log.d("Error","Error");
            }
        }
        );
        //將Request加入RequestQueue
        queue.add(jsonObjectRequest);
        PoST();
    }



}
