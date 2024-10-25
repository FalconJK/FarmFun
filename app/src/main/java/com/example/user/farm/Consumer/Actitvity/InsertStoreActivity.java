package com.example.user.farm.Consumer.Actitvity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.user.farm.Funtional.ReadImgToBinary2;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.bean.Image;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertStoreActivity extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    private static final int REQUEST_LIST_CODE = 0;
    private static final int REQUEST_CAMERA_CODE = 1;

    private RequestQueue queue;
    private Bitmap bitmap;
    private Context context = this;
    private Button button;
    private ImageView imageView;
    private AccountData data=new AccountData(this);


    private EditText storename;
    private EditText machine;
    private EditText phone;
    private EditText post;
    private EditText address;
    private EditText begintime;
    private EditText endtime;

    private String large;
    private String small;
    private String storeID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_store);
        setupComponent();
        setupListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");

            for (String path : pathList) {
                Glide.with(this).load(path).into(imageView);
                large=ReadImgToBinary2.img2LargeString(path,bitmap);
                small=ReadImgToBinary2.img2SmallString(path,bitmap);
            }
        } else if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            String path = data.getStringExtra("result");
            large=ReadImgToBinary2.img2LargeString(path,bitmap);
            small=ReadImgToBinary2.img2SmallString(path,bitmap);
        }
    }

    private void setupComponent() {
        queue = Volley.newRequestQueue(context);
        imageView = (ImageView) findViewById(R.id.beseller_storephoto);

        storename = (EditText) findViewById(R.id.beseller_storename);
        machine = (EditText) findViewById(R.id.beseller_machine);
        phone = (EditText) findViewById(R.id.beseller_storephone);
        post = (EditText) findViewById(R.id.beseller_storepost);
        address = (EditText) findViewById(R.id.beseller_storeadress);
        begintime = (EditText) findViewById(R.id.beseller_storebegintime);
        endtime = (EditText) findViewById(R.id.beseller_storendtime);

        button = (Button) findViewById(R.id.btn);
        imageView = (ImageView) findViewById(R.id.beseller_storephoto);

        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
    }

    private void setupListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue.add(InsertStore());
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectImage();
                return false;
            }
        });

        begintime.setInputType(InputType.TYPE_NULL);
        endtime.setInputType(InputType.TYPE_NULL);

        begintime.setOnClickListener(this);
        endtime.setOnClickListener(this);

        begintime.setOnFocusChangeListener(this);
        endtime.setOnFocusChangeListener(this);

    }

    private void selectImage() {
        ISListConfig config = new ISListConfig.Builder()
                .multiSelect(false)
                .needCamera(true)
                .backResId(R.drawable.ic_arrow_back_black_24dp)
                .title("圖片")
                .titleBgColor(Color.parseColor("#1c9359"))
                .cropSize(1, 1, 1500, 1500)
                .needCrop(true)
                .build();
        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }

    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    private StringRequest InsertStore() {
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Insert_Store";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jsonObject=new JsonParser().parse(response).getAsJsonObject();
                switch (jsonObject.get("result").getAsString()){
                    case "Successfully_Registered":
                        storeID=jsonObject.get("StoreID").getAsString();
                        queue.add(InsertSeller());
                        break;
                    case "StoreName_Have_Been_Registered":
                        showToast("此店名已被註冊");
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast(error.toString());
            }
        }) {
            // 携带参数
            @Override
            protected Map<String, String> getParams()
                    throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                //post的欄位放這
                hashMap.put("StoreName", storename.getText().toString());
                hashMap.put("Logo_Large", large);
                hashMap.put("Logo_Small", small);
                hashMap.put("Phone",phone.getText().toString());
                hashMap.put("Post", post.getText().toString());
                hashMap.put("Address", address.getText().toString());
                hashMap.put("WorkTimeStart",begintime.getText().toString());
                hashMap.put("WorkTimeEnd",endtime.getText().toString());
                hashMap.put("Belong_FaID", "0");
                hashMap.put("Belong_StoreID", "0");
                return hashMap;
            }
        };
        return request;
    }

    private StringRequest InsertSeller(){
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Insert_Seller";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jsonObject=new JsonParser().parse(response).getAsJsonObject();
                if (jsonObject.get("result").getAsString().equals("Successfully_Registered"))
                    data.setSeller(
                            AccountData.getID(context),
                            "2",
                            storeID
                    );
                    data.trueseller();
                showToast("以註冊成功");
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast(error.toString());
            }
        }) {
            // 携带参数
            @Override
            protected Map<String, String> getParams()
                    throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                //post的欄位放這
                hashMap.put("Email", AccountData.getemail(context));
                hashMap.put("Password", AccountData.getpassword(context));
                hashMap.put("AcceptMachineCode", machine.getText().toString());
                hashMap.put("Level","2");
                hashMap.put("Belong_StoreID", storeID);
                return hashMap;
            }
        };
        return request;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setTime(final EditText editText) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a new instance of TimePickerDialog and return it
        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editText.setText(hourOfDay + ":" + minute);
            }
        }, hour, minute, false).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.beseller_storebegintime:
                setTime(begintime);
                break;
            case R.id.beseller_storendtime:
                setTime(endtime);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)
            switch (v.getId()) {
                case R.id.beseller_storebegintime:
                    setTime(begintime);
                    break;
                case R.id.beseller_storendtime:
                    setTime(endtime);
                    break;
            }
    }
}


