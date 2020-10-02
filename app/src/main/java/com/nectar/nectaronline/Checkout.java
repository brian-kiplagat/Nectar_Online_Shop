package com.nectar.nectaronline;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Checkout extends AppCompatActivity {
    Fragment_Delivery delivery;
    Fragment_Payment payment;
    Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        delivery = new Fragment_Delivery();
        payment = new Fragment_Payment();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("CHECKOUT");
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayShowTitleEnabled(false); REMOVE TITLE
        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        adapter.addFragment(delivery, "DELIVERY");
        adapter.addFragment(payment, "PAYMENT");
        viewPager.setAdapter(adapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_local_shipping_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_payment_method);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }
        Intent intent = getIntent();
        if (intent.hasExtra("price")) {
            String price = intent.getStringExtra("price");
            Log.i("PRICE", price);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.checkout_menu, menu);
        return true;
    }
}