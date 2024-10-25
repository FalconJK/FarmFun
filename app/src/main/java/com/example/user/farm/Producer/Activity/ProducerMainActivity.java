package com.example.user.farm.Producer.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.farm.Producer.Fragment.AddEmployee;
import com.example.user.farm.Producer.Fragment.Crop;
import com.example.user.farm.Producer.Fragment.Datacart;
import com.example.user.farm.Producer.Fragment.Employee;
import com.example.user.farm.Producer.Fragment.Navfarm;
import com.example.user.farm.Producer.Fragment.Purchaseorder;
import com.example.user.farm.Producer.Fragment.Resume;
import com.example.user.farm.Producer.Fragment.Storeshop;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;

public class ProducerMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView name;
    private ImageView imageView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer);
        setupComponent();


        toolbar.setTitle("農產品");
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        Fragment fragment = new Crop();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    private void setupComponent() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.add).setVisible(AccountData.is_high_level(this));

        View headerview = navigationView.getHeaderView(0);
        name=headerview.findViewById(R.id.main_nav_name);
        imageView=headerview.findViewById(R.id.main_nav_photo);
        name.setText(AccountData.getcustomerName(this));
        Glide.with(this).load(AccountData.getsex(this)?
                R.drawable.profile_male:
                R.drawable.profile_female)
                .into(imageView);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.producer, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return jumpToFragment(item);
    }

    private boolean jumpToFragment(MenuItem menuItem) {
        menuItem.setChecked(true);
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.crop:
                fragment = new Crop();
                break;
            case R.id.stores:
                fragment = new Storeshop();
                break;
            case R.id.farm:
                fragment = new Navfarm();
                break;
            case R.id.resume:
                fragment = new Resume();
                break;
            case R.id.purchaseorder:
                fragment = new Purchaseorder();
                break;
            case R.id.employee:
                fragment = new Employee();
                break;
            case R.id.add:
                fragment = new AddEmployee();
                break;
            case R.id.datacart:
                fragment = new Datacart();
                break;
        }
        toolbar.setTitle(menuItem.getTitle());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
