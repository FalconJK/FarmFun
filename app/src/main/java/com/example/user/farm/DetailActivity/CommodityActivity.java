package com.example.user.farm.DetailActivity;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.user.farm.DetailActivity.MoreDetail.ResumeActivity;
import com.example.user.farm.Funtional.GetImageSet;
import com.example.user.farm.Funtional.JsonRequest;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.ShoppingCar;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class CommodityActivity extends AppCompatActivity {
    private Context context = this;
    private Toolbar toolbar;
    private ImageView imageView, storeicon, farmphoto;
    private String CropID = null;
    private String StoreID = null;
    private RequestQueue queue;

    private ObjectAnimator rotate;
    private ObjectAnimator color;

    private String text;
    private String CropName, Price, Photo;
    private Boolean get = false;

    private int red = Color.parseColor("#FF4081");
    private int org = Color.parseColor("#FF9800");

    private FloatingActionButton fab;

    private TextView cropname, price, cpackage, type, storetext, farmtext, startime, endtime, markertime, introduce;
    private TextView storestore, storecount;
    private TextView farmfarm, farmarea, farmaddress;
    private JsonObject crop;
    private JSONObject store, farm;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);

        Intent intent = getIntent();
        CropID = intent.getStringExtra("CropID");

        get = ShoppingCar.contain(context, CropID);

        setupComponent();
        setupListener();
        getData();
        setSupportActionBar(toolbar);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (get) {
            ShoppingCar.add(this, crop.toString());
        } else {
            ShoppingCar.remove(this, crop.toString());
        }
        queue.stop();
    }

    private void setupComponent() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setRotation(get ? 45 : 0);
        fab.setBackgroundTintList(ColorStateList.valueOf(get ? org : red));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (ImageView) findViewById(R.id.comm_photo);

        cropname = (TextView) findViewById(R.id.comm_CropName);
        price = (TextView) findViewById(R.id.comm_price);
        cpackage = (TextView) findViewById(R.id.comm_cpackage);
        type = (TextView) findViewById(R.id.comm_type);
        storetext = (TextView) findViewById(R.id.comm_store);
        farmtext = (TextView) findViewById(R.id.comm_farm);
        startime = (TextView) findViewById(R.id.comm_begintime);
        endtime = (TextView) findViewById(R.id.comm_finishtime);
        markertime = (TextView) findViewById(R.id.comm_marketime);
        introduce = (TextView) findViewById(R.id.comm_introduce);

        storestore = (TextView) findViewById(R.id.storename);
        storecount = (TextView) findViewById(R.id.comm_storecount);
        storeicon = (ImageView) findViewById(R.id.storeicon);

        farmfarm = (TextView) findViewById(R.id.comm_farmfarm);
        farmarea = (TextView) findViewById(R.id.comm_farmarea);
        farmaddress = (TextView) findViewById(R.id.comm_farmaddress);
        farmphoto = (ImageView) findViewById(R.id.comm_farmphoto);

        crop = new JsonObject();
        toolbar.setTitle("");
    }

    private void setupListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get = !get;
                fab.setEnabled(false);
                if (get) {
                    text = "已加入購物車";
                } else {
                    text = "已從購物車移除商品";
                }
                setrotate();
                setcolor();
                Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.comm_lookresume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ResumeActivity.class)
                        .putExtra("CropID", CropID)
                        .putExtra("StoreID", StoreID));
            }
        });

        findViewById(R.id.comm_linstore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, StoreActivity.class)
                        .putExtra("Data", store.toString()));
            }
        });

        findViewById(R.id.comm_linfarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FarmActivity.class)
                        .putExtra("Data", farm.toString()));
            }
        });
    }

    @SuppressLint("ObjectAnimatorBinding")
    private void setcolor() {
        color = ObjectAnimator.ofInt(fab, "backgroundTint", get ? red : org, get ? org : red);
        color.setDuration(800);
        color.setEvaluator(new ArgbEvaluator());
        color.setInterpolator(new DecelerateInterpolator(2));
        color.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                fab.setBackgroundTintList(ColorStateList.valueOf(animatedValue));
            }
        });
        color.start();
    }

    private void setrotate()  {
        rotate = ObjectAnimator.ofFloat(fab, "rotation", fab.getRotation() + 405);
        rotate.setDuration(800);
        rotate.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fab.setEnabled(true);
            }
            //region
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
            //endregion
        });
        rotate.start();
    }

    private void getData() {
        queue = Volley.newRequestQueue(this);//創建一個RequestQueue
        queue.add(getCropData(CropID));
    }

    private JsonRequest getCropData(String cropID) {
        String url = String.format("http://120.101.8.52/2017farmer/WebService1.asmx/Read_Crop?CropID=%s", cropID);
        JsonRequest request = new JsonRequest(Request.Method.GET, url,
                new Response.Listener<JsonObject>() {
                    @Override
                    public void onResponse(JsonObject response) {
                        String P_unit = response.get("P_unit").getAsString();
                        String Package = response.get("Package").getAsString();
                        String CropType = response.get("CropType").getAsString();
                        String PlantBegin = response.get("PlantBegin").getAsString().replace("-", "/");
                        String PlantEnd = response.get("PlantEnd").getAsString().replace("-", "/");
                        String ListingTime = response.get("ListingTime").getAsString().replace("-", "/");
                        String Belong_StoreName = response.get("Belong_StoreName").getAsString();
                        String Belong_FarmName = response.get("Belong_FarmName").getAsString();
                        String Introduce = response.get("Introduce").getAsString();
                        String Belong_FarmID = response.get("Belong_FarmID").getAsString();

                        Photo = response.get("Photo").getAsString();
                        CropName = response.get("CropName").getAsString();
                        Price = response.get("Price").getAsString();
                        StoreID = response.get("Belong_StoreID").getAsString();

                        crop.addProperty("CropID", CropID);
                        crop.addProperty("Count", 1);
                        crop.addProperty("Photo", Photo);
                        crop.addProperty("Check", false);
                        crop.addProperty("CropName", CropName);
                        crop.addProperty("Price", Price);

                        GetImageSet.gisl(imageView, Photo);
//                        imageView.setImageResource(R.mipmap.birthday);
                        cropname.setText(CropName);
                        price.setText(String.format("$%s/%s", Price, P_unit));
                        cpackage.setText(String.format("(%s)", Package));
                        type.append(CropType);
                        startime.append(PlantBegin);
                        endtime.append(PlantEnd);
                        markertime.append(ListingTime);
                        storetext.append(Belong_StoreName);
                        farmtext.append(Belong_FarmName);
                        introduce.setText(Introduce);
                        queue.add(getFarmData(Belong_FarmID));
                        queue.add(getStoreData(StoreID));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //當發生錯誤時，提示使用者發生錯誤
                Log.d("Error", "Error");
            }
        }
        );
        return request;
    }

    private JsonObjectRequest getStoreData(String storeid) {
        String url = String.format("http://120.101.8.52/2017farmer/WebService1.asmx/Read_Store?StoreID=%s", storeid);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            store = response;
                            storestore.setText(response.getString("StoreName"));
                            storecount.setText(response.getString("CropCount"));
                            GetImageSet.giss(storeicon, response.getString("Logo"));

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

    private JsonObjectRequest getFarmData(String farmid) {
        String url = String.format("http://120.101.8.52/2017farmer/WebService1.asmx/Read_Farm?FarmID=%s", farmid);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            farm = response;
                            farmfarm.setText(response.getString("FarmName"));
                            farmarea.setText(response.getString("Area"));
                            farmaddress.setText(response.getString("Address"));
                            GetImageSet.giss(farmphoto, response.getString("Image"));

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
}


















