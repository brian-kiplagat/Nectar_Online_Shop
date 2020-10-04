package com.nectar.nectaronline;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class metadata extends AppCompatActivity {
    Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    Fragment_About about;
    Fragment_Developer developer;
    Fragment_Info info;
    Fragment_Orders orders;
    Fragment_Favourites favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metadata);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        about = new Fragment_About();
        developer = new Fragment_Developer();
        info=new Fragment_Info();
        orders=new Fragment_Orders();
        favourites=new Fragment_Favourites();
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        viewPagerAdapter.addFragment(orders, "ORDERS");
        viewPagerAdapter.addFragment(favourites, "FAVOURITES");
        viewPagerAdapter.addFragment(info, "INFO");
        viewPagerAdapter.addFragment(about, "ABOUT US");
        viewPagerAdapter.addFragment(developer, "DEVELOPER");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_logo_cart_empty_straight_lines_bold_svg);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_favorite_border_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_outline_supervised_user_circle_24);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_person_pin_24);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_baseline_code_24);


    }



    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent.hasExtra("payload")) {
            String x = intent.getStringExtra("payload");
            if (x.contentEquals("developer")) {
                tabLayout.setScrollX(tabLayout.getWidth());
                tabLayout.getTabAt(4).select();
            } else if (x.contentEquals("about")) {
                tabLayout.setScrollX(tabLayout.getWidth());
                tabLayout.getTabAt(3).select();
            } else if (x.contentEquals("contact")) {
                tabLayout.setScrollX(tabLayout.getWidth());
                tabLayout.getTabAt(2).select();
            } else if (x.contentEquals("orders")) {
                tabLayout.setScrollX(tabLayout.getWidth());
                tabLayout.getTabAt(0).select();
            } else if (x.contentEquals("favourites")) {
                tabLayout.setScrollX(tabLayout.getWidth());
                tabLayout.getTabAt(1).select();
            }
        }
    }
}