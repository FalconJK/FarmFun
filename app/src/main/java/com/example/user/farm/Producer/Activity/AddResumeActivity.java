package com.example.user.farm.Producer.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.user.farm.Funtional.JsonRequest;
import com.example.user.farm.Funtional.ReadImgToBinary2;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.google.gson.JsonObject;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class AddResumeActivity extends AppCompatActivity {
    private static final int REQUEST_LIST_CODE = 0;
    private static final int REQUEST_CAMERA_CODE = 1;

    private RequestQueue queue;
    private Context context = this;
    private DatePickerDialog datePickerDialog;

    private ImageView imageView;


    private EditText name;
    private EditText date;
    private EditText introduce;

    private Button btn;

    private String CropID;
    private String large;
    private String small;

    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resume);
        setupComponent();
        setupListener();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");

            for (String path : pathList) {
                Glide.with(this).load(path).into(imageView);
                large = ReadImgToBinary2.img2LargeString(path, bitmap);
                small = ReadImgToBinary2.img2SmallString(path, bitmap);
            }
        } else if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            String path = data.getStringExtra("result");
            large = ReadImgToBinary2.img2LargeString(path, bitmap);
            small = ReadImgToBinary2.img2SmallString(path, bitmap);
        }
    }

    private void setupComponent() {
        queue = Volley.newRequestQueue(this);
        imageView = (ImageView) findViewById(R.id.addresume_photo);
        name = (EditText) findViewById(R.id.addresume_name);
        date = (EditText) findViewById(R.id.addresume_date);
        introduce = (EditText) findViewById(R.id.addresume_introduce);
        btn = (Button) findViewById(R.id.btn);
        CropID = getIntent().getStringExtra("CropID");

        GregorianCalendar calendar = new GregorianCalendar();

        datePickerDialog = new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
            //將設定的日期顯示出來
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String time = String.format("%d/%02d/%02d", year, ++monthOfYear, dayOfMonth);
                Log.d("date", time);
                date.setText(time);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

    }

    private void setupListener() {
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectImage();
                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue.add(InsertResume());
            }
        });
        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    datePickerDialog.show();
            }
        });
    }

    private void selectImage() {
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });

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

    private JsonRequest InsertResume() {
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Insert_Resume";
        JsonRequest request = new JsonRequest(Request.Method.POST, url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                Toast.makeText(context, "新增成功", Toast.LENGTH_SHORT).show();
                finish();
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
                hashMap.put("SellerID", AccountData.getID(context));
                hashMap.put("CropID", CropID);
                hashMap.put("ResumeName", name.getText().toString());
                hashMap.put("Date", date.getText().toString());
                hashMap.put("Introduce", introduce.getText().toString());
                hashMap.put("Photo_Large", large);
                hashMap.put("Photo_Small", small);
                return hashMap;
            }
        };
        return request;
    }
}
