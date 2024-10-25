package com.example.user.farm.Consumer.Actitvity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.user.farm.Consumer.Adapter.ViewPagerFragmentAdapter;
import com.example.user.farm.Consumer.Fragment.Login.Login_Fragment;
import com.example.user.farm.Consumer.Fragment.Login.Signin_Fragment;
import com.example.user.farm.R;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity{

    private static ViewPager myViewPager;
    private TabLayout tabLayout;
    private String[] TabTitle = {"註冊", "登入"};

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myViewPager = (ViewPager) findViewById(R.id.myViewPager);
        tabLayout = (TabLayout) findViewById(R.id.TabLayout);
        tabLayout.setupWithViewPager(myViewPager);

        Intent intent = getIntent();
        int page = intent.getIntExtra("pos", 0);

        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        setViewPager();

        setTabLayoutIcon();
        setv(page);
    }

    public void setTabLayoutIcon() {
        for (int i = 0; i < TabTitle.length; i++) {
            tabLayout.getTabAt(i).setText(TabTitle[i]);
        }

    }

    public static void setv(int pos) {
        myViewPager.setCurrentItem(pos);
    }

    private void setViewPager() {
        Signin_Fragment signin = new Signin_Fragment();
        Login_Fragment login = new Login_Fragment();

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        fragmentList.add(signin);
        fragmentList.add(login);

        ViewPagerFragmentAdapter myFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        myViewPager.setAdapter(myFragmentAdapter);
    }
}

