package com.example.user.farm.Consumer.Modify;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.JsonObject;

import java.util.HashMap;

public class AccountDataModifyActivity extends AppCompatActivity {

    private String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Update_Customer";
    private RequestQueue queue;
    private EditText name;
    private EditText email;
    private EditText sex;
    private EditText birth;
    private EditText phone;
    private EditText address;
    private Button modify;
    private Context context = this;

    private String dataemail;
    private String datapassword;
    private String datacustomerName;
    private Boolean datasex;
    private Boolean isseller;
    private String databirth;
    private String dataphone;
    private String datapost;
    private String dataaddress;
    private AccountData data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_data_modify);
        setupVariable();
        setupConpoment();
        setaccountdata();
    }

    private void setupVariable() {
        data = new AccountData(context);
        dataemail = data.getemail(context);
        datapassword = data.getpassword(context);
        datacustomerName = data.getcustomerName(context);
        datasex = data.getsex(context);
        isseller = data.isseller(context);
        databirth = data.getbirth(context);
        dataphone = data.getphone(context);
        datapost = data.getpost(context);
        dataaddress = data.getaddress(context);
    }


    private void setupConpoment() {
        queue = Volley.newRequestQueue(this);//創建一個RequestQueue
        name = findViewById(R.id.modify_name);
        email = findViewById(R.id.modify_email);
        sex = findViewById(R.id.modify_sex);
        birth = findViewById(R.id.modify_birth);
        phone = findViewById(R.id.modify_ph);
        address = findViewById(R.id.modify_address);
        modify = findViewById(R.id.confirm_button);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"sending",Toast.LENGTH_SHORT).show();
                queue.add(new JsonRequest(Request.Method.POST, url, new Response.Listener<JsonObject>() {
                    @Override
                    public void onResponse(JsonObject response) {
                        if (response.get("result").getAsString()
                                .equals("Successfully_Updated")) {
                            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                            data.setCustomer(
                                    dataemail,
                                    datapassword,
                                    name.getText().toString(),
                                    datasex.equals("男性") ? "True" : "False",
                                    databirth,
                                    dataphone,
                                    datapost,
                                    dataaddress,
                                    isseller ? "True" : "False"
                            );
                            setResult(1);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    // 携带参数
                    @Override
                    protected HashMap<String, String> getParams()
                            throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        //post的欄位放這
                        hashMap.put("OriginalEmail", data.getemail(context));
                        hashMap.put("OriginalPassword", data.getpassword(context));
                        hashMap.put("Email", data.getemail(context));
                        hashMap.put("Password", data.getpassword(context));
                        hashMap.put("CustomerName", name.getText().toString());
                        hashMap.put("Sex", data.getsex(context)?"1":"0");
                        hashMap.put("Birth", birth.getText().toString());
                        hashMap.put("Phone", phone.getText().toString());
                        hashMap.put("Post", data.getpost(context));
                        hashMap.put("Address", address.getText().toString());
                        Log.d("hashMap",hashMap.toString());
                        return hashMap;
                    }
                });
            }
        });
    }


    private void setaccountdata() {
        name.setText(data.getcustomerName(this));
        email.setText(data.getemail(this));
        sex.setText(data.getsexText(this));
        birth.setText(data.getbirth(this));
        phone.setText(data.getphone(this));
        address.setText(data.getaddress(this));

    }
}
