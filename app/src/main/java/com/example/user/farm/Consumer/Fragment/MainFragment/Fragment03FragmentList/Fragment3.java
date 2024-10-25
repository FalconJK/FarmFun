package com.example.user.farm.Consumer.Fragment.MainFragment.Fragment03FragmentList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.farm.Consumer.Actitvity.HistoryActivity;
import com.example.user.farm.Consumer.Actitvity.LoginActivity;
import com.example.user.farm.Consumer.Adapter.ViewPagerFragmentAdapter;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.example.user.farm.SharePreference.History;
import com.example.user.farm.SharePreference.Login;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomoya on 4/5/17.
 */

public class Fragment3 extends Fragment implements View.OnClickListener {

    private Context context;
    public ImageView imageView;
    private ViewPager myViewPager;
    private TabLayout tabLayout;

    private Button login;
    private Button assign;

    private LinearLayout list;
    private LinearLayout history;
    private LinearLayout account;
    private LinearLayout help;
    private TextView name;

    private String[] TabTitle = {"購買中","銷售中"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=getContext();
        Boolean sex=AccountData.getsex(context);
        if(Login.isLogin(getActivity())){
            View view=inflater.inflate(R.layout.fragment_fragment3, container, false);
            imageView=(ImageView) view.findViewById(R.id.photo);
            Glide.with(context).load(sex?R.drawable.profile_male:R.drawable.profile_female).into(imageView);
            myViewPager = (ViewPager) view.findViewById(R.id.myViewPager3);
            tabLayout = (TabLayout) view.findViewById(R.id.TabLayout3);
            name =(TextView) view.findViewById(R.id.name);
            name.setText(AccountData.getcustomerName(getActivity()));
            setViewPager();
            tabLayout.setupWithViewPager(myViewPager);
            setTabLayoutIcon();
            return view;
        }else {
            View view=inflater.inflate(R.layout.fragment_fragment3_nologin, container, false);
            login= (Button)view.findViewById(R.id.login);
            assign= (Button)view.findViewById(R.id.assign);

            list=(LinearLayout)view.findViewById(R.id.list);
            history=(LinearLayout) view.findViewById(R.id.history);
            account=(LinearLayout)view.findViewById(R.id.account);
            help=(LinearLayout)view.findViewById(R.id.help);


            login.setOnClickListener(this);
            assign.setOnClickListener(this);

            list.setOnClickListener(this);
            history.setOnClickListener(this);
            account.setOnClickListener(this);
            help.setOnClickListener(this);

            return view;
        }
    }

    public void setTabLayoutIcon(){
        for(int i =0; i < TabTitle.length;i++){
            tabLayout.getTabAt(i).setText(TabTitle[i]);
        }
    }

    private void setViewPager() {
        FragmentList_One myFragment1 = new FragmentList_One();
        FragmentList_Two myFragment2 = new FragmentList_Two();

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        fragmentList.add(myFragment1);
        fragmentList.add(myFragment2);

        ViewPagerFragmentAdapter myFragmentAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), fragmentList);
        myViewPager.setAdapter(myFragmentAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()!=R.id.help) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            switch (v.getId()) {
                case R.id.assign:
                    intent.putExtra("pos", 0);
                    startActivity(intent);
                    break;
                case R.id.login:
                case R.id.list:
                case R.id.account:
                    intent.putExtra("pos", 1);
                    startActivity(intent);
                    break;
                case R.id.history:
                    startActivity(new Intent(getActivity(), HistoryActivity.class));
                    break;
            }

        }
        else {
            Toast.makeText(getActivity(),"help",Toast.LENGTH_SHORT).show();
        }
    }
}