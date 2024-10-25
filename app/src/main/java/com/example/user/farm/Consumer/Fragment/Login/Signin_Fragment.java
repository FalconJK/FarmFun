package com.example.user.farm.Consumer.Fragment.Login;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.user.farm.Consumer.Actitvity.DealActivity;
import com.example.user.farm.Consumer.Actitvity.LoginActivity;
import com.example.user.farm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by user on 2018/1/24.
 */

public class Signin_Fragment extends Fragment
        implements View.OnClickListener,View.OnLongClickListener,RadioGroup.OnCheckedChangeListener,DialogInterface.OnClickListener{
    //region元件宣告
    private ImageView userphoto;
    private ImageView dateselect;

    private EditText sign_account;
    private EditText sign_password;
    private EditText sign_recheckpassword;
    private EditText sign_name;
    private EditText sign_birthday;
    private EditText sign_phone;
    private EditText sign_post;
    private EditText sign_address;

    private RadioGroup sex;

    private Button assign;

    private DatePickerDialog datePickerDialog;

    private String date;
    private String sex_selected="1";

    private Dialog dialog;

    //endregion
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.account_sigin_fragment, container, false);

        //region元件連結
        userphoto=(ImageView)view.findViewById(R.id.user_profile_photo);
        dateselect=(ImageView)view.findViewById(R.id.sign_select_date);

        sign_account=(EditText)view.findViewById(R.id.sign_account);
        sign_password=(EditText)view.findViewById(R.id.sign_password);
        sign_recheckpassword=(EditText)view.findViewById(R.id.sign_recheckpassword);
        sign_name=(EditText)view.findViewById(R.id.sign_name);
        sign_birthday=(EditText)view.findViewById(R.id.sign_birthday);
        sign_phone=(EditText)view.findViewById(R.id.sign_phone);
        sign_post=(EditText)view.findViewById(R.id.sign_post);
        sign_address=(EditText)view.findViewById(R.id.sign_address);

        sex=(RadioGroup)view.findViewById(R.id.sex);

        assign=(Button)view.findViewById(R.id.assign);

        GregorianCalendar calendar = new GregorianCalendar();
        //endregion

        //region設定監聽

        datePickerDialog = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_DARK,new OnDateSetListener() {
            //將設定的日期顯示出來
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date=String.format("%d%02d%02d",year,++monthOfYear,dayOfMonth);
                Log.d("date",date);
                sign_birthday.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        assign.setOnClickListener(this);
        sex.setOnCheckedChangeListener(this);
        userphoto.setOnLongClickListener(this);
        dateselect.setOnClickListener(this);

        sign_birthday.setInputType(InputType.TYPE_NULL);
        sign_birthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    datapickerdialog();
                }
            }
        });
        sign_birthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                datapickerdialog();

            }
        });
//endregion

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_select_date:
                datapickerdialog();
                break;
            case R.id.assign:
                if(check()){
                    dialog = ProgressDialog.show(getActivity(), "註冊中", "請等待...", true);
                    Signinserver();
                }
                break;
        }

    }

    private void Signinserver(){
        String url ="http://120.101.8.52/2017farmer/WebService1.asmx/Insert_Customer";
        RequestQueue RequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String result=jsonObject.getString("result");
                            dialog.dismiss();
                            showDialog(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                showDialog("NetWorkError");
                System.out.println("請求錯誤:" + error.toString());
            }
        }) {
            // 攜帶參數
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                //post的欄位放這

                hashMap.put("Email", sign_account.getText().toString());
                hashMap.put("Password",sign_password.getText().toString() );
                hashMap.put("CustomerName", sign_name.getText().toString());
                hashMap.put("Sex", sex_selected);
                hashMap.put("Birth", date);
                hashMap.put("Phone", sign_phone.getText().toString());
                hashMap.put("Post", sign_post.getText().toString());
                hashMap.put("Address", sign_address.getText().toString());
                hashMap.put("Seller", "0");

                return hashMap;
            }
        };
        RequestQueue.add(mStringRequest);
    }

    private void showDialog(String result) {
        switch (result){
            case "Successfully_Registered":
                new AlertDialog.Builder(getActivity())
                        .setTitle("註冊成功")
                        .setMessage("您已註冊成功，請輸入帳密登入")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("了解", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoginActivity.setv(1);
                                Login_Fragment.setdata(sign_account.getText().toString(),sign_password.getText().toString());
                            }
                        }
                        )
                        .show();
                break;
            case "Email_Have_Been_Registered":
                new AlertDialog.Builder(getActivity())
                        .setTitle("註冊失敗")
                        .setMessage("此帳號已被註冊，換一個帳號試試吧")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("了解", this)
                        .show();
                break;
            case "NetWorkError":
                new AlertDialog.Builder(getActivity())
                        .setTitle("註冊失敗")
                        .setMessage("請確認已經已經開啟網路")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("了解", this)
                        .show();
                break;
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(sign_account.getText().toString())){
            sign_account.setError("帳號欄位必須填入資料");
            return false;
        }if (TextUtils.isEmpty(sign_password.getText().toString())){
            sign_password.setError("密碼欄位必須填入資料");
            return false;
        }if (TextUtils.isEmpty(sign_recheckpassword.getText().toString())){
            sign_recheckpassword.setError("確認密碼欄位必須填入資料");
            return false;
        }if (TextUtils.isEmpty(sign_name.getText().toString())){
            sign_name.setError("姓名欄位必須填入資料");
            return false;
        }if (TextUtils.isEmpty(sign_birthday.getText().toString())){
            sign_birthday.setError("生日欄位必須填入資料");
            return false;
        }if (TextUtils.isEmpty(sign_phone.getText().toString())){
            sign_phone.setError("生日欄位必須填入資料");
            return false;
        }if (TextUtils.isEmpty(sign_post.getText().toString())){
            sign_post.setError("郵遞區號欄位必須填入資料");
            return false;
        }if (TextUtils.isEmpty(sign_address.getText().toString())){
            sign_address.setError("住址欄位必須填入資料");
            return false;
        }if (!sign_password.getText().toString().equals(sign_recheckpassword.getText().toString())){
            Toast.makeText(getActivity(),"請確認您的密碼是否正確",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void datapickerdialog(){
        if (Build.VERSION.RELEASE.equals("7.0"))
            Toast.makeText(getActivity(),"左上角可以快速選擇年份喔!!",Toast.LENGTH_LONG).show();
        datePickerDialog.show();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.sign_radioButton_male:
                sex_selected="1";
                Glide.with(this).load(R.drawable.profile_male).into(userphoto);
                break;
            case R.id.sign_radioButton_female:
                sex_selected="0";
                Glide.with(this).load(R.drawable.profile_female).into(userphoto);
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
