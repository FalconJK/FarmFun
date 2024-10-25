package com.example.user.farm.Consumer.Actitvity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QrcodeActivity extends AppCompatActivity {
    private Context context=this;
    private AccountData data = new AccountData(this);
    private RequestQueue queue;
    private String storeID;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        Barcode barcode = getIntent().getParcelableExtra("barcode");
        String data = barcode.displayValue;
        queue = Volley.newRequestQueue(this);
        try {
            JSONObject jsonObject = new JSONObject(data);
            storeID = jsonObject.getString("storeID");
            level = jsonObject.getString("level");
            queue.add(InsertSeller(storeID, level));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "抱歉，無法辨認此QrCode", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private StringRequest InsertSeller(final String storeID, final String level) {
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Insert_Seller";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                if (jsonObject.get("result").getAsString().equals("Successfully_Registered")) {
                    data.setSeller(
                            AccountData.getID(context),
                            "2",
                            QrcodeActivity.this.storeID
                    );
                    data.trueseller();
                    showToast("以註冊成功");
                } else
                    showToast("註冊失敗");
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
                hashMap.put("AcceptMachineCode", "b8:28:eb:39:a7:1f");
                hashMap.put("Level", level);
                hashMap.put("Belong_StoreID", storeID);
                return hashMap;
            }
        };
        return request;
    }


    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
