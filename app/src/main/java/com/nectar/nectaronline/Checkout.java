package com.nectar.nectaronline;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

public class Checkout extends AppCompatActivity implements Fragment_Delivery.ToPayment {
    private static final int LOCATION_PERMISSION_CODE = 1;
    Fragment_Delivery delivery;
    Fragment_Payment payment;
    Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    //We want the user to turn on gps
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
        delivery.setToPayment(this);
        viewPager.setAdapter(adapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_local_shipping_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_payment_method);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //START REQUESTING LOCATION UPDATES
        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(1000).setFastestInterval(900);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    String loc = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                    Log.i("onLocationResult: ", loc);
                }


            }
        };
        requestUpdates();

    }

    private void requestUpdates() {
        isGPSEnabled();
        if (isPermissionEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checkout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close:
                Log.i("CLOSE", "onOptionsItemSelected: ");
                finish();
                break;
        }
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onclick(String finalPrice) {
        tabLayout.setScrollX(tabLayout.getWidth());
        tabLayout.getTabAt(1).select();
    }

    private boolean isGPSEnabled() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

// check if enabled and if not send user to the GSP settings
// Better solution would be to display a dialog and suggesting to
// go to the settings
        if (!enabled) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Checkout.this);
            builder.setIcon(R.drawable.ic_baseline_location_on_24).setTitle("TURN ON GPS LOCATION").setMessage("To deliver direct to your doorstep, please allow us to access your GPS location so that we can come deliver to you directly")
                    .setPositiveButton("OKAY, TURN ON GPS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }).setCancelable(false).show();
            return false;
        } else {
            return true;

        }
    }

    private boolean isPermissionEnabled() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Checkout.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}