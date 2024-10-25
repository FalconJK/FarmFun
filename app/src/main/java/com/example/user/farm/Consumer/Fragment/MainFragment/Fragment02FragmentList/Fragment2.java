package com.example.user.farm.Consumer.Fragment.MainFragment.Fragment02FragmentList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.farm.Consumer.Adapter.ViewPagerFragmentAdapter;
import com.example.user.farm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomoya on 4/5/17.
 */

public class Fragment2 extends Fragment {
    private ViewPager myViewPager;
    private TabLayout tabLayout;
    
    private String[] IconResTitlte = {"菜類","店家","農場"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fragment2, container, false);
        myViewPager = (ViewPager) view.findViewById(R.id.myViewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.TabLayout);
        setViewPager();
        tabLayout.setupWithViewPager(myViewPager);
        setTabLayoutIcon();

        return view;
    }

    public void setTabLayoutIcon(){
        for(int i =0; i < IconResTitlte.length;i++){
            tabLayout.getTabAt(i).setText(IconResTitlte[i]);
        }
    }
    private void setViewPager() {
        FragmentList_One myFragment1 = new FragmentList_One();
        FragmentList_Two myFragment2 = new FragmentList_Two();
        FragmentList_Three myFragment3 = new FragmentList_Three();

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        fragmentList.add(myFragment1);
        fragmentList.add(myFragment2);
        fragmentList.add(myFragment3);

        ViewPagerFragmentAdapter myFragmentAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), fragmentList);
        myViewPager.setAdapter(myFragmentAdapter);
    }

}