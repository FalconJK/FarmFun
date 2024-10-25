package com.example.user.farm.DetailActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.DetailActivity.MoreDetail.ListCommityActivity;
import com.example.user.farm.Funtional.GetImageSet;
import com.example.user.farm.R;

import org.json.JSONException;
import org.json.JSONObject;

public class StoreActivity extends AppCompatActivity {
    private ImageView imageView;
    private Toolbar toolbar;
    private LinearLayout commity;
    private Context context = this;
    private String Data;
    private String StoreID;
    private RequestQueue queue;
    private CollapsingToolbarLayout toolbarLayout;
    private TextView store, croptype, phone, farm, address, begintime, endtime, cropcount, introduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        setupComponent();
        setupListener();
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        Data = intent.getStringExtra("Data");
        if (Data.equals("GetOnNetwork")) {
            StoreID = intent.getStringExtra("StoreID");
            queue = Volley.newRequestQueue(this);//創建一個RequestQueue
            queue.add(getStoreData(StoreID));
        } else {
            try {
                setupData(new JSONObject(Data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupComponent() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        imageView = (ImageView) findViewById(R.id.photo_store);
        commity = (LinearLayout) findViewById(R.id.store_commodity);
        store = (TextView) findViewById(R.id.storename);
        croptype = (TextView) findViewById(R.id.store_croptype);
        phone = (TextView) findViewById(R.id.store_phone);
        farm = (TextView) findViewById(R.id.store_farm);
        address = (TextView) findViewById(R.id.store_address);
        begintime = (TextView) findViewById(R.id.store_begintime);
        endtime = (TextView) findViewById(R.id.store_endtime);
        cropcount = (TextView) findViewById(R.id.store_count);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        introduce=(TextView)findViewById(R.id.store_introduce);
    }

    private void setupListener() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        commity.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ListCommityActivity.class)
                        .putExtra("Method", "Read_Store_All_Crop?StoreID=" + StoreID));
            }
        });
    }

    private void setupData(JSONObject data) {
        try {
            StoreID = data.getString("StoreID");
            GetImageSet.gisl(imageView, data.getString("Logo"));
            toolbarLayout.setTitle(data.getString("StoreName"));
            toolbarLayout.setTitleEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            store.setText("店家名稱　　： " + data.getString("StoreName"));
            phone.setText("營業電話　　： " + data.getString("Phone"));
            address.setText("營業地址　　： " + data.getString("Post") + data.getString("Address"));
            begintime.setText("開始營業時間： " + data.getString("WorkTimeStart"));
            endtime.setText("結束營業時間： " + data.getString("WorkTimeEnd"));
            cropcount.setText("商品數量　　： " + data.getString("CropCount"));

//            introduce.setText(data.getString(""));
            croptype.setText(data.getString("croptype"));
            farm.setText(data.getString("farm"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JsonObjectRequest getStoreData(String StoreID) {
        String url = String.format("http://120.101.8.52/2017farmer/WebService1.asmx/Read_Store?StoreID=%s", StoreID);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setupData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        return jsonObjectRequest;
    }

}
