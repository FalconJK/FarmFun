package com.example.user.farm.Producer.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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

import java.util.HashMap;
import java.util.List;

public class AddFarmActivity extends AppCompatActivity implements LocationListener {
    //region 宣告
    private static final int REQUEST_LIST_CODE = 0;
    private static final int REQUEST_CAMERA_CODE = 1;

    static final int MIN_TIME = 5000;
    static final float MIN_DIST = 0;
    private LocationManager mgr;
    private RequestQueue queue;
    private Context context=this;

    private ImageView imageView;

    private EditText name,post,address,area,coordinate,quantity,introduce;

    private Button button;

    private Double Latitude,Longitude;
    private String large;
    private String small;

    private Bitmap bitmap;

    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_farm);
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        queue = Volley.newRequestQueue(this);
        setupComponent();
        setupListener();
        checkPermission();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");

            for (String path : pathList) {
                Glide.with(this).load(path).into(imageView);
                large= ReadImgToBinary2.img2LargeString(path,bitmap);
                small=ReadImgToBinary2.img2SmallString(path,bitmap);
            }
        } else if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            String path = data.getStringExtra("result");
            large=ReadImgToBinary2.img2LargeString(path,bitmap);
            small=ReadImgToBinary2.img2SmallString(path,bitmap);
        }
    }


    private void setupComponent() {
        imageView = (ImageView) findViewById(R.id.addfarm_photo);

        name = (EditText) findViewById(R.id.addfarm_name);
        post = (EditText) findViewById(R.id.addfarm_post);
        address = (EditText) findViewById(R.id.addfarm_address);
        area = (EditText) findViewById(R.id.addfarm_area);
        coordinate = (EditText) findViewById(R.id.addfarm_coordinate);
        quantity = (EditText) findViewById(R.id.addfarm_quantity);
        introduce = (EditText) findViewById(R.id.addfarm_introduce);

        button=(Button)findViewById(R.id.btn);

        coordinate.setText("取得位置中...");
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
    }

    private void setupListener(){
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
                queue.add(InsetFarm());
            }
        });
    }
//    座標-----------------------------------------------------------
    //檢查若尚未授權，向使用者要求定位權限
    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantRessults) {
        if (requestCode == 200) {
            if (grantRessults.length >= 1 &&
                    grantRessults[0] != PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(this)
                        .setTitle("定位授權")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("程式需要定位權限才能運作，成為賣家?")
                        .setPositiveButton("好", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkPermission();
                            }
                        })
                        .setNegativeButton("不用了，謝謝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        enableLocationUpdates(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        enableLocationUpdates(false);
    }

    private void enableLocationUpdates(boolean isTurnOn) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (isTurnOn) {
                //檢查GPS 與網路定位是否可用
                isGPSEnabled = mgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    Toast.makeText(this, "請確定已開啟定位功能", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "取得定位資訊中... ", Toast.LENGTH_SHORT).show();
                    if (isGPSEnabled)
                        mgr.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, this);
                    if (isNetworkEnabled)
                        mgr.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_TIME, this);
                }
            } else {
                mgr.removeUpdates(this);
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {//位置變更事件
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();
        String str = "定位提供者:" + location.getProvider();
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        str = String.format("< %.5f°N , %.5f°E >", Latitude, Longitude);
        coordinate.setText(str);
        coordinate.setFocusable(false);
    }
//    -----------------------------------------------------------

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

    private JsonRequest InsetFarm(){
        String url="http://120.101.8.52/2017farmer/WebService1.asmx/Insert_Farm";
        JsonRequest request=new JsonRequest(Request.Method.POST,url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                Toast.makeText(context,"新增成功!!",Toast.LENGTH_LONG).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            // 携带参数
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("SellerID",AccountData.getID(context));
                hashMap.put("FarmName", name.getText().toString());
                hashMap.put("Post", post.getText().toString());
                hashMap.put("Address", address.getText().toString());
                hashMap.put("Longitude", Longitude.toString() );
                hashMap.put("Latitude", Latitude.toString());
                hashMap.put("Image_Large", large);
                hashMap.put("Image_Small", small);
                hashMap.put("Area", area.getText().toString());
                hashMap.put("Quantity", quantity.getText().toString());
                hashMap.put("Introduce", introduce.getText().toString());
                return hashMap;
            }
        };
        return request;
    }


    //region
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
    //endregion
}
