package com.example.user.farm.Consumer.Actitvity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.Consumer.Adapter.ShoppingAdapter;
import com.example.user.farm.Funtional.Item;
import com.example.user.farm.Funtional.JsonRequest;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.example.user.farm.SharePreference.Login;
import com.example.user.farm.SharePreference.ShoppingCar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingCartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Item> items;
    private Context context = this;
    private RequestQueue queue;
    private Gson gson = new Gson();
    private ShoppingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // 建立與設定 Toolbar 物件
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 讀取折疊 Toolbar 物件
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // 設定折疊 Toolbar 的標題
        collapsingToolbar.setTitle("購物車");

        queue = Volley.newRequestQueue(context);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//設置監聽器
            @Override
            public void onRefresh() {//當使用者於頂端垂直滑動手勢時
                setdata();
                swipeRefreshLayout.setRefreshing(false);//移除SwipeRefreshLayout更新時的loading圖示
            }
        });


        // 設定 RecylerView 使用的 Adapter 物件
    }

    @Override
    public void onStart() {
        super.onStart();
        setdata();
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onRestart() {
        super.onRestart();
        updateData();
        setdata();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updateData();
    }

    public void setdata() {
        String data = ShoppingCar.getdata(this);
        Type listType = new TypeToken<ArrayList<Item>>() {}.getType();
        items = gson.fromJson(data, listType);
        recyclerView = (RecyclerView) findViewById(R.id.shoppingre);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new ShoppingAdapter(recyclerView.getContext(), R.layout.item_linear_shoppingcart, items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 如果選擇 app bar 左側的返回鍵
        if (item.getItemId() == android.R.id.home) {
            // 結束元件
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void checkout(View view) {
        if (Login.isLogin(this)) {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item.getCheck()) {
                    queue.add(InserOrder(item.getCropID(), item.getCount().toString()));
                    items.remove(i);
                }
            }
            updateData();
            setdata();
            adapter.notifyDataSetChanged();
        }
        //region else
        else {
            new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle("請登入帳號")
                    .setMessage("登入帳號才可以能使用購買功能")
                    .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(context, LoginActivity.class)
                                    .putExtra("pos", 1));
                        }
                    })
                    .setNegativeButton("註冊", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(context, LoginActivity.class)
                                    .putExtra("pos", 0));

                        }
                    }).show();
        }
        //endregion else
    }

    private void updateData() {
        String string = gson.toJson(items);
        ShoppingCar.update(this, string);
    }

    private JsonRequest InserOrder(final String CropID, final String Quantity) {
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Insert_Order";
        JsonRequest request = new JsonRequest(Request.Method.POST, url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                Toast.makeText(context, "成功上傳訂單" + CropID, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            // 携带参数
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                //post的欄位放這
                hashMap.put("CustomerID", AccountData.getID(context));
                hashMap.put("CropID", CropID);
                hashMap.put("Quantity", Quantity);
                return hashMap;
            }
        };
        return request;
    }

}
