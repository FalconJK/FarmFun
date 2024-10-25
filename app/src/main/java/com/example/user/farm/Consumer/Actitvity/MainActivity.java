package com.example.user.farm.Consumer.Actitvity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.user.farm.Consumer.Fragment.MainFragment.Fragment02FragmentList.Fragment2;
import com.example.user.farm.Consumer.Fragment.MainFragment.Fragment03FragmentList.Fragment3;
import com.example.user.farm.Consumer.Fragment.MainFragment.Fragment1;
import com.example.user.farm.Manifest;
import com.example.user.farm.R;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private BottomNavigationView navigation;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    protected Toolbar toolbar;

    private Fragment1 fragment1 = new Fragment1();
    private Fragment2 fragment2 = new Fragment2();
    private Fragment3 fragment3 = new Fragment3();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //添加viewPager事件監聽（很容易忘）
        viewPager.addOnPageChangeListener(this);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        toolbar = (Toolbar) findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ShoppingCartActivity.class));

            }
        });
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return fragment1;
                    case 1:
                        return fragment2;
                    case 2:
                        return fragment3;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        toolbar.setTitle(R.string.title_home);
//        toolbar.setZ(15);
        setSupportActionBar(toolbar);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //點擊BottomNavigationView的Item項，切換ViewPager頁面
            //menu/navigation.xml裡加的android:orderInCategory屬性就是下面item.getOrder()取的值
            viewPager.setCurrentItem(item.getOrder());
            return true;
        }

    };
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_seach, menu);
//
//        MenuItem menuSearchItem = menu.findItem(R.id.my_search);
//
//        // 獲取SearchView並設置可搜索的配置
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menuSearchItem.getActionView();
//
//        // 假設當前的Activity是可搜索的Activity
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//
//        // 這邊讓icon可以還原到搜尋的icon
//        searchView.setIconifiedByDefault(true);
//        return true;
//    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPageSelected(int position) {
        //頁面滑動的時候，改變BottomNavigationView的Item高亮
        navigation.getMenu().getItem(position).setChecked(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            switch (position) {
                case 0:
                    toolbar.setZ(15);
                    break;
                case 1:
                    toolbar.setZ(0);
                    break;
                case 2:
                    toolbar.setZ(15);
                    break;
            }
        toolbar.setTitle(navigation.getMenu().getItem(position).getTitle());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}