package com.nectar.nectaronline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    private Fragment_Cart cart;
    private Fragment_Profile profile;
    private Fragment_Shop shop;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        setSupportActionBar(toolbar);

        shop = new Fragment_Shop();
        cart = new Fragment_Cart();
        profile = new Fragment_Profile();

        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        adapter.addFragment(shop, "SHOP");
        adapter.addFragment(cart, "CART");
        adapter.addFragment(profile, "PROFILE");
        viewPager.setAdapter(adapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_add_shopping_cart_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_logo_cart_empty_straight_lines_bold_svg);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_local_shipping_24);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("SEARCH", query);

        } else {
            Log.i("NO SEARCH", "Query");

        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.developer:
                drawerLayout.closeDrawers();
                Log.i("DEV", "onNavigationItemSelected: ");
                break;
            case R.id.about:
                drawerLayout.closeDrawers();
                Log.i("ABOUT", "onNavigationItemSelected: ");
                break;
            case R.id.contact:
                drawerLayout.closeDrawers();
                Log.i("CONTACT", "onNavigationItemSelected: ");
                break;
            case R.id.logout:
                drawerLayout.closeDrawers();
                Log.i("LOGOUT", "onNavigationItemSelected: ");
                break;

        }

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_app_bar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchQuery) {
               // Log.i("Query", searchQuery);
                boolean search=true;
                shop.fetch(search,searchQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               //Log.i("Query Text Change", newText);
                boolean search=true;
                shop.fetch(search,newText);
                return false;
            }
        });


        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                Log.i("APP SEARCH BAR", "onOptionsItemSelected: ");
                break;
            case R.id.help:
                Log.i("HELP", "onOptionsItemSelected: ");
                break;
            case R.id.share:
                Log.i("SHARE", "onOptionsItemSelected: ");
                break;
            case R.id.improve:
                Log.i("IMPROVE", "onOptionsItemSelected: ");
                break;

        }
        return true;

    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentsList = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentsList.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);


        }
    }


}