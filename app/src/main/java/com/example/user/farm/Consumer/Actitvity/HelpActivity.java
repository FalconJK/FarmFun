package com.example.user.farm.Consumer.Actitvity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.farm.R;
import com.example.user.farm.SharePreference.History;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        History.clear(this);
    }
}
