package com.example.user.farm.DetailActivity.MoreDetail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.DetailActivity.CommodityActivity;
import com.example.user.farm.Funtional.ListImgSet;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.History;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ListCommityActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String Method;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_commity);

        Intent intent= getIntent();
        Method=intent.getStringExtra("Method");

        recyclerView = (RecyclerView)findViewById(R.id.listc_recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        progressBar = (ProgressBar)findViewById(R.id.listc_progressBar);

        queue= Volley.newRequestQueue(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        String url ="http://120.101.8.52/2017farmer/WebService1.asmx/"+Method;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonArray jsonArray=new JsonParser()
                        .parse(response).getAsJsonObject()
                        .get("Crop").getAsJsonArray();
                recyclerView.setAdapter(new ListCommityAdapter(recyclerView.getContext(), R.layout.item_grid_commity,queue,jsonArray));
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        queue.add(request);
    }





































    public class ListCommityAdapter extends RecyclerView.Adapter<ListCommityAdapter.ViewHolder> {

        private Context context;
        // 使用的畫面配置資源編號
        private int layoutId;
        private RequestQueue queue;
        private JsonArray jsonArray;

        public ListCommityAdapter(Context context, int layoutId, RequestQueue queue, JsonArray jsonArray) {
            this.context = context;
            this.layoutId = layoutId;
            this.queue = queue;
            this.jsonArray = jsonArray;
        }


        @Override
        public ListCommityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent. getContext())
                    .inflate(layoutId, parent, false);
            return new ListCommityAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final JsonObject jsonObject= jsonArray.get(position).getAsJsonObject();

            final View view = holder.mView;
            final String CropID=jsonObject.get("CropID").getAsString();

            ListImgSet.set(queue,holder.img, jsonObject.get("Photo").getAsString());
            holder.title.setText(jsonObject.get("CropName").getAsString());
            holder.price.setText(String.format("%s元/%s",
                    jsonObject.get("Price").getAsString(),
                    jsonObject.get("P_unit").getAsString()
            ));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommodityActivity.class);
                    intent.putExtra("CropID",CropID);
                    context.startActivity(intent);
                    History.addata(context,jsonObject.toString());

                }
            });
        }

        @Override
        public int getItemCount() {
            // 設定為固定7個項目
            return jsonArray.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public ImageView img;
            public TextView title;
            public TextView price;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                img = (ImageView) view.findViewById(R.id.grid_crop_photo);
                title = (TextView) view.findViewById(R.id.grid_crop_title);
                price = (TextView) view.findViewById(R.id.grid_crop_price);
            }
        }

    }

}


