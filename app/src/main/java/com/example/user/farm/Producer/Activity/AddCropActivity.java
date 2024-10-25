package com.example.user.farm.Producer.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
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

import info.hoang8f.android.segmented.SegmentedGroup;

public class AddCropActivity extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener,
        View.OnFocusChangeListener,
        View.OnClickListener {
    private static final int REQUEST_LIST_CODE = 0;
    private static final int REQUEST_CAMERA_CODE = 1;

    private RequestQueue queue;
    private Context context = this;

    private DatePickerDialog datePickerDialog;


    //regionUI元件宣告
    private ImageView imageView;

    private EditText name, begintime, endtime, listtime, introduce, quantity, q_unit, price, p_unit;

    private TextInputLayout packagehint;

    private TextInputEditText cpackage;

    private SegmentedGroup segmentedGroup;

    private Button button;
    //endregion

    private EditText dateset;

    private String large;
    private String small;
    private String farmID;
    private String cropType = "米";

    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crop);

        Intent intent = getIntent();
        farmID = intent.getStringExtra("FarmID");

        setupComponent();
        setupListener();
    }

    private void setupComponent() {
        queue = Volley.newRequestQueue(this);

        imageView = (ImageView) findViewById(R.id.addcrop_photo);

        name = (EditText) findViewById(R.id.addcrop_name);
        begintime = (EditText) findViewById(R.id.addcrop_begintime);
        endtime = (EditText) findViewById(R.id.addcrop_endtime);
        listtime = (EditText) findViewById(R.id.addcrop_listime);
        introduce = (EditText) findViewById(R.id.addcrop_introduce);
        quantity = (EditText) findViewById(R.id.addcrop_quantity);
        q_unit = (EditText) findViewById(R.id.addcrop_qunit);
        price = (EditText) findViewById(R.id.addcrop_price);
        p_unit = (EditText) findViewById(R.id.addcrop_punit);
        cpackage = (TextInputEditText) findViewById(R.id.addcrop_package);

        packagehint = (TextInputLayout) findViewById(R.id.packagehint);

        segmentedGroup = (SegmentedGroup) findViewById(R.id.segmented);

        GregorianCalendar calendar = new GregorianCalendar();

        button = (Button) findViewById(R.id.btn);

        datePickerDialog = new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
            //將設定的日期顯示出來
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = String.format("%d%02d%02d", year, ++monthOfYear, dayOfMonth);
                Log.d("date", date);
                dateset.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue.add(InsertCrop());
            }
        });

        segmentedGroup.setOnCheckedChangeListener(this);

        begintime.setInputType(InputType.TYPE_NULL);
        endtime.setInputType(InputType.TYPE_NULL);
        listtime.setInputType(InputType.TYPE_NULL);

        begintime.setOnClickListener(this);
        endtime.setOnClickListener(this);
        listtime.setOnClickListener(this);

        begintime.setOnFocusChangeListener(this);
        endtime.setOnFocusChangeListener(this);
        listtime.setOnFocusChangeListener(this);

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

    private JsonRequest InsertCrop() {
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Insert_Crop";
        JsonRequest request = new JsonRequest(Request.Method.POST, url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                Toast.makeText(context, "新增成功!!", Toast.LENGTH_LONG).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            // 携带参数
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("SellerID", AccountData.getID(context));
                hashMap.put("FarmID", farmID);
                hashMap.put("CropName", name.getText().toString());
                hashMap.put("CropType", cropType);
                hashMap.put("PlantBegin", begintime.getText().toString());
                hashMap.put("PlantEnd", endtime.getText().toString());
                hashMap.put("ListingTime", listtime.getText().toString());
                hashMap.put("Introduce", introduce.getText().toString());
                hashMap.put("Photo_Large", large);
                hashMap.put("Photo_Small", small);
                hashMap.put("Quantity", quantity.getText().toString());
                hashMap.put("Q_unit", q_unit.getText().toString());
                hashMap.put("Price", price.getText().toString());
                hashMap.put("P_unit", p_unit.getText().toString());
                hashMap.put("Package", cpackage.getText().toString());
                hashMap.put("Discount", "0");

                return hashMap;
            }
        };
        return request;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rice:
                cropType = "米";
                packagehint.setHint("單位說明(ex:一袋30台斤)");
                break;
            case R.id.vegetable:
                cropType = "蔬菜";
                packagehint.setHint("單位說明(ex:一斤10把)");
                break;
            case R.id.Fruit:
                cropType = "水果";
                packagehint.setHint("單位說明(ex:一箱30顆)");
                break;
            case R.id.flower:
                cropType = "花";
                packagehint.setHint("單位說明(ex:一束5朵)");
                break;
            case R.id.others:
                cropType = "其他";
                packagehint.setHint("單位說明");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addcrop_begintime:
                setDatePicker(begintime);
                break;
            case R.id.addcrop_endtime:
                setDatePicker(endtime);
                break;
            case R.id.addcrop_listime:
                setDatePicker(listtime);
                break;

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.addcrop_begintime:
                    setDatePicker(begintime);
                    break;
                case R.id.addcrop_endtime:
                    setDatePicker(endtime);
                    break;
                case R.id.addcrop_listime:
                    setDatePicker(listtime);
                    break;
            }
        }
    }

    private void setDatePicker(EditText editText) {
        if (Build.VERSION.RELEASE.equals("7.0"))
            Toast.makeText(this, "左上角可以快速選擇年份喔!!", Toast.LENGTH_LONG).show();
        dateset = editText;
        datePickerDialog.show();
    }
}
