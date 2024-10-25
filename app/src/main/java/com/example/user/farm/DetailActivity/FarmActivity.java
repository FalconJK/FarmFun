package com.example.user.farm.DetailActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.DetailActivity.MoreDetail.FarmMachineActivity;
import com.example.user.farm.DetailActivity.MoreDetail.ListCommityActivity;
import com.example.user.farm.Funtional.GetImageSet;
import com.example.user.farm.Producer.Activity.AddCropActivity;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;

import org.json.JSONException;
import org.json.JSONObject;

public class FarmActivity extends AppCompatActivity {
    private ImageView imageView;
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
    private String Data;
    private String FarmID;
    private String FarmName;
    private String StoreID;
    private RequestQueue queue;
    private TextView farm, address, coordinate, store, area, quantity, introduce;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        setupComponent();
        getData();
        setupListener();
    }

    private void setupComponent() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        imageView = (ImageView) findViewById(R.id.photo_farm);
        farm = (TextView) findViewById(R.id.farm_name);
        address = (TextView) findViewById(R.id.farm_address);
        coordinate = (TextView) findViewById(R.id.farm_coordinate);
        store = (TextView) findViewById(R.id.farm_store);
        area = (TextView) findViewById(R.id.farm_area);
        quantity = (TextView) findViewById(R.id.farm_quantity);
        introduce = (TextView) findViewById(R.id.farm_introduce);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FarmActivity.this, AddCropActivity.class)
                        .putExtra("FarmID", FarmID));
            }
        });

        findViewById(R.id.farm_commity).setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FarmActivity.this, ListCommityActivity.class)
                        .putExtra("Method", "Read_Farm_All_Crop?FarmID=" + FarmID));
            }
        });

        findViewById(R.id.farm_data).setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FarmActivity.this, FarmMachineActivity.class)
                        .putExtra("FarmID", FarmID)
                        .putExtra("StoreID", StoreID));
            }
        });

    }

    private void getData() {
        Intent intent = getIntent();
        Data = intent.getStringExtra("Data");
        if (Data.equals("GetOnNetwork")) {
            FarmID = intent.getStringExtra("ID");
            queue = Volley.newRequestQueue(this);//創建一個RequestQueue
            queue.add(getStoreData(FarmID));
        } else {
            try {
                setupData(new JSONObject(Data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupData(JSONObject data) {
        try {
            FarmID = data.getString("FarmID");
            StoreID = data.getString("Belong_StoreID");

            FarmName=data.getString("FarmName");
            toolbarLayout.setTitle(FarmName);
            toolbarLayout.setTitleEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            farm.setText("農場名稱　　： " +FarmName);
            address.setText("營業地址　　： " + data.getString("Post") + data.getString("Address"));
            coordinate.setText(String.format("經度緯度　　： < %s°N , %s°E >",
                    data.getString("Latitude"),
                    data.getString("Longitude")
            ));
            store.setText("銷售店家　　： " + data.getString("Belong_StoreName"));
            area.setText("種植面積　　： " + data.getString("Area"));
            quantity.setText("種植產量　　： " + data.getString("Area"));
            GetImageSet.gisl(imageView, data.getString("Image"));
            introduce.setText(data.getString("Introduce"));
            if (StoreID.equals(AccountData.getbelong_StoreID(this)))
                fab.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JsonObjectRequest getStoreData(String FarmID) {
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Read_Farm?FarmID=" + FarmID;
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






















