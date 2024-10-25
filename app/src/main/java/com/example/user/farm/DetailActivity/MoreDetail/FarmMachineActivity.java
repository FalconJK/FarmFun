package com.example.user.farm.DetailActivity.MoreDetail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.Funtional.JsonRequest;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class FarmMachineActivity extends AppCompatActivity {
    private RequestQueue queue;
    private Context context = this;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private View item;
    private String FramID;
    private String StoreID;
    private Boolean belong;
    private Boolean level;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_machine);
        setupComponent();
        setupListener();
    }

    private void setupComponent() {
        FramID = getIntent().getStringExtra("FarmID");
        StoreID = getIntent().getStringExtra("StoreID");
        queue = Volley.newRequestQueue(this);

        belong = StoreID.equals(AccountData.getbelong_StoreID(context));
        level = AccountData.is_highest_level(context);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        item = LayoutInflater.from(this).inflate(R.layout.dialog_addmachine, null);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(belong && level ? View.VISIBLE : View.GONE);
        dialog = new AlertDialog.Builder(context)
                .setTitle("新增機器")
                .setView(item)
                .setCancelable(false)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText machine = (EditText) item.findViewById(R.id.machinecode);
                        EditText number = (EditText) item.findViewById(R.id.number);
                        EditText name = (EditText) item.findViewById(R.id.name);

                        String machines = machine.getText().toString();
                        String numbers = number.getText().toString();
                        String names = name.getText().toString();

                        if (TextUtils.isEmpty(machines) ||
                                TextUtils.isEmpty(numbers) ||
                                TextUtils.isEmpty(names)) {
                            Toast.makeText(getApplicationContext(), "請輸入機器資料", Toast.LENGTH_SHORT).show();
                        } else {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            //post的欄位放這
                            hashMap.put("SellerID", AccountData.getID(context));
                            hashMap.put("MachineCode", machines);
                            hashMap.put("Number", numbers);
                            hashMap.put("Name", names);
                            hashMap.put("Belong_FarmID", FramID);
                            queue.add(InsertMachine(machines,numbers,names));
                        }
                    }
                }).create();
    }

    private JsonRequest InsertMachine(final String machine, final String number, final String name) {
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Insert_Machine";
        JsonRequest request = new JsonRequest(Request.Method.POST, url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                switch (response.get("result").getAsString()) {
                    case "Successfully_Insert":
                        Toast.makeText(context, "新增成功", Toast.LENGTH_SHORT).show();
                        break;
                    case "Have_Been_Registered":
                        Toast.makeText(context, "重複編號，請換編號再新增", Toast.LENGTH_SHORT).show();
                        break;
                }

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
                hashMap.put("SellerID", AccountData.getID(context));
                hashMap.put("MachineCode", machine);
                hashMap.put("Number", number);
                hashMap.put("Name", name);
                hashMap.put("Belong_FarmID", FramID);
                return hashMap;
            }
        };
        return request;
    }

    private void setupListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Read_Farm_All_Machine?FarmID=" + FramID;
        final JsonRequest request = new JsonRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                JsonArray array = response.get("Machine").getAsJsonArray();
                recyclerView.setAdapter(new RecyclerViewAdapter(context, array));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private JsonArray jsonArray;
        private Context context;

        public RecyclerViewAdapter(Context context, JsonArray jsonArray) {
            this.jsonArray = jsonArray;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_linear_datacart, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final JsonObject data = jsonArray.get(position).getAsJsonObject();
            final String ID = data.get("MachineID").getAsString();
            String code = data.get("MachineCode").getAsString();
            String num = data.get("Number").getAsString();
            try {
                holder.soil.append(data.get("Seneor_Soil").getAsString());
                holder.water.append(data.get("Sensor_Water").getAsString());
                holder.tem.append(data.get("Sensor_Tem").getAsString().replace("C", "°C"));
                holder.hum.append(data.get("Sensor_Hum").getAsString());
                String date = data.get("Sensor_DateTime").getAsString();
            } catch (Exception e) {
            }
            holder.name.setText(data.get("Name").getAsString());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        Toast.makeText(context, ID, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, ChartActivity.class)
                            .putExtra("MachineID", ID));
                }
            });

        }

        @Override
        public int getItemCount() {
            // 設定為固定7個項目
            return jsonArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            TextView name;
            TextView soil;
            TextView water;
            TextView tem;
            TextView hum;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                name = (TextView) view.findViewById(R.id.name);
                soil = (TextView) view.findViewById(R.id.soil);
                water = (TextView) view.findViewById(R.id.water);
                tem = (TextView) view.findViewById(R.id.tem);
                hum = (TextView) view.findViewById(R.id.hum);
            }
        }


    }

}
