package com.example.user.farm.Consumer.Actitvity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.user.farm.R;

public class SearchableActivity extends AppCompatActivity {
    public TextView textView;
    public String query;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        handleIntent(getIntent());
        // 建立與設定 Toolbar 物件
        Toolbar toolbar = (Toolbar) findViewById(R.id.searchable_toolbar);
        toolbar.setZ(15);
        setSupportActionBar(toolbar);
//        toolbar.setTitle("123");
        // 取得 ActionBar 物件
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // 設定返回按鈕
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle(query);
        textView=(TextView)findViewById(R.id.textView);
        textView.setText(query);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 如果選擇 app bar 左側的返回鍵
        if (item.getItemId() == android.R.id.home) {
            // 結束元件
            finish();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
