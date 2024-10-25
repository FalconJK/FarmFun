package com.example.user.farm.Consumer.Actitvity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.user.farm.Consumer.Adapter.ViewPagerFragmentAdapter;
import com.example.user.farm.Consumer.Fragment.Deal.Receipt_Fragment;
import com.example.user.farm.Consumer.Fragment.Deal.Shipping_Fragment;
import com.example.user.farm.Consumer.Fragment.Login.Login_Fragment;
import com.example.user.farm.Consumer.Fragment.Login.Signin_Fragment;
import com.example.user.farm.R;

import java.util.ArrayList;
import java.util.List;

public class DealActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager myViewPager;
    private TabLayout tabLayout;
    private String[] TabTitle = {"待出貨","待收貨"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        myViewPager = (ViewPager) findViewById(R.id.deal_myViewPager);
        tabLayout = (TabLayout) findViewById(R.id.deal_TabLayout);
        tabLayout.setupWithViewPager(myViewPager);

        Intent intent = getIntent();
        int page = intent.getIntExtra("pos",0);

        setViewPager();
        setTabLayoutIcon();

        myViewPager.setCurrentItem(page);
    }

    public void setTabLayoutIcon(){
        for(int i =0; i < TabTitle.length;i++){
            tabLayout.getTabAt(i).setText(TabTitle[i]);
        }

    }
    private void setViewPager(){
        Receipt_Fragment receipt = new Receipt_Fragment();
        Shipping_Fragment shipping = new Shipping_Fragment();


        List<Fragment> fragmentList = new ArrayList<Fragment>();

        fragmentList.add(shipping);//待收貨
        fragmentList.add(receipt);//待出貨


        ViewPagerFragmentAdapter myFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        myViewPager.setAdapter(myFragmentAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}
