package com.example.user.farm.Consumer.Actitvity;

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
import android.widget.TextView;

import com.example.user.farm.Consumer.Fragment.Deal.Shipping_Fragment;
import com.example.user.farm.DetailActivity.CommodityActivity;
import com.example.user.farm.Funtional.GetImageSet;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_history);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new RecyclerViewAdapter(recyclerView.getContext(),R.layout.item_grid_commity, History.getdata(this)));
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
                final JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length()-position-1);
                final String CropID=jsonObject.getString("CropID");

                GetImageSet.gisl(holder.img, jsonObject.getString("Photo"));
                holder.title.setText(jsonObject.getString("CropName"));
                holder.price.setText(String.format("%s元/%s",
                        jsonObject.getString("Price"),
                        jsonObject.getString("P_unit")
                ));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, CommodityActivity.class);
                        intent.putExtra("CropID",CropID);
                        context.startActivity(intent);
                    }
                });

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
