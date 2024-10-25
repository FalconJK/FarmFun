package com.example.user.farm.DetailActivity.MoreDetail;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.Funtional.GetImageSet;
import com.example.user.farm.Producer.Activity.AddResumeActivity;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResumeActivity extends AppCompatActivity {

    private RequestQueue queue;
    private Context context=this;
    private FloatingActionButton fab;
    private String CropID;
    private String StoreID;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        Intent intent = getIntent();
        CropID = intent.getStringExtra("CropID");
        StoreID = intent.getStringExtra("StoreID");

        fab=(FloatingActionButton)findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.rsm_recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.rsm_progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        getData();
        setfab();
    }

    private void setfab() {
        if(StoreID.equals(AccountData.getbelong_StoreID(this))){
            fab.setVisibility(View.VISIBLE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddResumeActivity.class)
                        .putExtra("CropID",CropID));
            }
        });
    }

    private void getData() {
        queue = Volley.newRequestQueue(this);//創建一個RequestQueue
        queue.add(getCropData(CropID));
    }

    private JsonObjectRequest getCropData(String cropID) {
        String url = String.format("http://120.101.8.52/2017farmer/WebService1.asmx/Read_Crop_All_Resume?CropID=%s", cropID);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            recyclerView.setAdapter(new RecyclerViewAdapter(recyclerView.getContext()
                                    , R.layout.item_linear_resume,
                                    response.getJSONArray("Resume")));
                            recyclerView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //當發生錯誤時，提示使用者發生錯誤
                Log.d("Error", "Error");
            }
        }
        );
        return jsonObjectRequest;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private JSONArray jsonArray;
        private Context context;
        private int layoutId;

        public RecyclerViewAdapter(Context context, int layoutId, JSONArray jsonArray) {
            this.jsonArray = jsonArray;
            this.context = context;
            this.layoutId = layoutId;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(layoutId, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final View view = holder.mView;
            // 註冊項目點擊事件
            try {
                JSONObject data = jsonArray.getJSONObject(position);
                GetImageSet.giss(holder.img, data.getString("Photo"));
                holder.title.setText(data.getString("ResumeName"));
                holder.time.append(data.getString("Date").replace("-", "/"));
                holder.introduce.setText(data.getString("Introduce"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            // 設定為固定7個項目
            return jsonArray.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public ImageView img;
            public TextView title;
            public TextView time;
            public TextView introduce;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                img = (ImageView) view.findViewById(R.id.resume_img);
                title = (TextView) view.findViewById(R.id.resume_title);
                time = (TextView) view.findViewById(R.id.resume_time);
                introduce = (TextView) view.findViewById(R.id.resume_introduce);
            }
        }
    }
}
