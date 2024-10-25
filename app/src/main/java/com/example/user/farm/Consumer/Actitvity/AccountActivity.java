package com.example.user.farm.Consumer.Actitvity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.farm.Consumer.Modify.AccountDataModifyActivity;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;

public class AccountActivity extends AppCompatActivity {
    private static final int Result_OK = 1;
    private TextView name;
    private TextView email;
    private TextView sex;
    private TextView birth;
    private TextView phone;
    private TextView address;
    private Button modify;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setupConpoment();
        setaccountdata();
    }

    private void setupConpoment() {
        name = findViewById(R.id.acc_name);
        email = findViewById(R.id.acc_email);
        sex = findViewById(R.id.acc_sex);
        birth = findViewById(R.id.acc_birth);
        phone = findViewById(R.id.acc_ph);
        address = findViewById(R.id.acc_address);
        modify = findViewById(R.id.modify_button);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, AccountDataModifyActivity.class), Result_OK);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setaccountdata();
    }



    private void setaccountdata() {
        name.setText(AccountData.getcustomerName(this));
        email.setText(AccountData.getemail(this));
        sex.setText(AccountData.getsexText(this));
        birth.setText(AccountData.getbirth(this));
        phone.setText(AccountData.getphone(this));
        address.setText(AccountData.getaddress(this));
    }
}
