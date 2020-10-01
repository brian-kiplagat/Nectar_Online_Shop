package com.nectar.nectaronline;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class metadata extends AppCompatActivity {
    Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    Fragment_About about;
    Fragment_Developer developer;
    Fragment_Info info;

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
        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(info, "INFO");
        viewPagerAdapter.addFragment(about, "ABOUT US");
        viewPagerAdapter.addFragment(developer, "DEVELOPER");

        viewPager.setAdapter(viewPagerAdapter);


    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragentTitle.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent.hasExtra("payload")) {
            String x = intent.getStringExtra("payload");
            if (x.contentEquals("developer")) {
                tabLayout.setScrollX(tabLayout.getWidth());
                tabLayout.getTabAt(2).select();
            } else if (x.contentEquals("about")) {
                tabLayout.setScrollX(tabLayout.getWidth());
                tabLayout.getTabAt(1).select();
            } else if (x.contentEquals("contact")) {
                tabLayout.setScrollX(tabLayout.getWidth());
                tabLayout.getTabAt(0).select();
            }
        }
    }
}