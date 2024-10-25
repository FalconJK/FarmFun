package com.example.user.farm.Consumer.Fragment.Login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.example.user.farm.SharePreference.History;
import com.example.user.farm.SharePreference.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 2018/1/24.
 */

@SuppressLint("ValidFragment")
public class Login_Fragment extends Fragment implements DialogInterface.OnClickListener{
    private static EditText etEmail;
    private static EditText etPassword;
    private Dialog dialog;
    private Context context;
    private AccountData data;

    String email;
    String password;

    public static void setdata(String email, String password){
        etEmail.setText(email);
        etPassword.setText(password);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.account_login_fragment, container, false);
        etEmail = (EditText) view.findViewById(R.id.et_email);
        etPassword  = (EditText)view.findViewById(R.id.et_password);
        etEmail.setText(email);
        etPassword.setText(password);
        context=getActivity();
        data=new AccountData(context);


        view.findViewById(R.id.login_in_button).setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                dialog = ProgressDialog.show(getActivity(), "登入中", "請等待...", true);
                tryLogin();
            }
        });

        return view;
    }
    private void tryLogin(){
        String email = String.valueOf(etEmail.getText()).trim();
        String password = String.valueOf(etPassword.getText()).trim();
        check(email, password);
    }

    private void check(final String email,final String password){
        String url ="http://120.101.8.52/2017farmer/WebService1.asmx/Customer_Login";
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject=new JSONObject(response);
                            String result=jsonObject.getString("result");
                            Log.d("login",response+"");

                            switch (result){
                                case "Successfully_Login":
                                    showDialog("Successfully_Login");
                                    markUserLogin();
                                    data.setID(jsonObject.getString("CustomerID"));
                                    storgeUserData(email,password);
                                    break;
                                case "Email_or_Password_Incorrect":
                                    showDialog("Email_or_Password_Incorrect");
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                System.out.println("請求錯誤:" + error.toString());
                showDialog("NetWorkError");
            }
        }) {
            // 攜帶參數
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                //post的欄位放這
                hashMap.put("Email", email);
                hashMap.put("Password", password);
                return hashMap;
            }

        };
        mRequestQueue.add(mStringRequest);
        mStringRequest.setShouldCache(false);
    }

    private void storgeUserData(final String email,final String password) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());//創建一個RequestQueue

        String urlcustomer =String.format("http://120.101.8.52/2017farmer/WebService1.asmx/Read_Customer?Email=%s&Password=%s",email,password);
        String urlseller =String.format("http://120.101.8.52/2017farmer/WebService1.asmx/Read_Seller?Email=%s&Password=%s",email,password);

        JsonObjectRequest customer = new JsonObjectRequest(Request.Method.GET, urlcustomer, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Read_Customer",response.toString());
                        try {
                            data.setCustomer(
                                    response.getString("Email"),
                                    response.getString("Password"),
                                    response.getString("CustomerName"),
                                    response.getString("Sex"),
                                    response.getString("Birth"),
                                    response.getString("Phone"),
                                    response.getString("Post"),
                                    response.getString("Address"),
                                    response.getString("Seller")
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //當發生錯誤時，提示使用者發生錯誤
                Log.d("Error","Error");
            }
        }
        );
        JsonObjectRequest seller = new JsonObjectRequest(Request.Method.GET, urlseller, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            data.setSeller(
                                    response.getString("SellerID"),
                                    response.getString("Level"),
                                    response.getString("Belong_StoreID")
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //當發生錯誤時，提示使用者發生錯誤
                Log.d("Error","Error");
            }
        }
        );
        //將Request加入RequestQueue
        queue.add(customer);
        queue.add(seller);
        restatapp();
    }

    private void restatapp() {
        final Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
//        final Intent intent1 = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void markUserLogin(){
        Login.login(getActivity());
    }

    private void showDialog(String result) {
        switch (result){
            case "Successfully_Login":
                new AlertDialog.Builder(getActivity())
                        .setTitle("登入成功")
                        .setMessage("已登入成功，請等待App重啟")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .show();

                break;
            case "Email_or_Password_Incorrect":
                new AlertDialog.Builder(getActivity())
                        .setTitle("登入失敗")
                        .setMessage("請確認帳號密碼是否正確")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("了解", this)
                        .show();
                break;
            case "NetWorkError":
                new AlertDialog.Builder(getActivity())
                        .setTitle("登入失敗")
                        .setMessage("請確認已經已經開啟網路")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("了解", this)
                        .show();
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {}

}
