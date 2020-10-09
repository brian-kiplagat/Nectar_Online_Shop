package com.nectar.nectaronline;

import android.Manifest;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Fragment_Cart.DeletedListener , Fragment_Cart.GetShopFragment {
    private static final int CONTACT_PERM_REQUEST_CODE = 1;
    Toolbar toolbar;
    private Fragment_Cart cart;
    private Fragment_Profile profile;
    private Fragment_Shop shop;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final int CONTACTS_REQUEST_CODE = 1;
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
        cart.setDeletedListener(this);
        cart.setGetShopFragment(this);
        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        adapter.addFragment(shop, "SHOP");
        adapter.addFragment(cart, "CART");
        adapter.addFragment(profile, "PROFILE");
        viewPager.setAdapter(adapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_add_shopping_cart_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_logo_cart_empty_straight_lines_bold_svg);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_person_pin_24);

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

    private void getCount() {
        String url = getString(R.string.website_adress) + "/nectar/buy/getcount.php";
        RequestBody formBody = new FormBody.Builder()
                .add("email", new Preferences(getApplicationContext()).getEmail())//then from server can check if to search or not the return an appropriate respons
                .build();

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
                Log.i("CART", res);
                Log.i("SERVER RESPONSE", res);
                try {
                    JSONObject obj = new JSONObject(res);
                    String code = obj.getString("RESPONSE_CODE");
                    String desc = obj.getString("RESPONSE_DESC");
                    if (code.contentEquals("SUCCESS")) {
                        String STUFF = obj.getString("COUNT");
                        JSONObject object = new JSONObject(STUFF);
                        JSONArray array = object.getJSONArray("items");
                        JSONObject obje = array.getJSONObject(0);
                        final String count = obje.getString("COUNT(*)");
                        Log.i("---COUNT--", count);
                        BadgeDrawable badgeDrawable = tabLayout.getTabAt(1).getOrCreateBadge();
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(Integer.parseInt(count));


                    } else if (desc.contentEquals("ZERO ITEMS")) {
                        Log.i("---ZERO ITEMS--", "NO ITEMS FOUND");
                        BadgeDrawable badgeDrawable = tabLayout.getTabAt(1).getOrCreateBadge();
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(0);
                    }
                } catch (Exception e) {

                }

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sell:
               // drawerLayout.closeDrawers();
                Log.i("SELL", "onNavigationItemSelected: ");
                try {
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.sell_link)));
                    startActivity(intent1);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Please install a web browser like opera mini or google chrome to proceed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


                break;
            case R.id.developer:
               // drawerLayout.closeDrawers();
                Intent intent = new Intent(getApplicationContext(), metadata.class);
                intent.putExtra("payload", "developer");
                startActivity(intent);
                Log.i("DEV", "onNavigationItemSelected: ");
                break;

            case R.id.about:
              //  drawerLayout.closeDrawers();
                Intent intent2 = new Intent(getApplicationContext(), metadata.class);
                intent2.putExtra("payload", "about");
                startActivity(intent2);
               Log.i("ABOUT", "onNavigationItemSelected: ");
                break;
            case R.id.contact:
               // drawerLayout.closeDrawers();
                Intent intent3= new Intent(getApplicationContext(), metadata.class);
                intent3.putExtra("payload", "contact");
                startActivity(intent3);

                Log.i("CONTACT", "onNavigationItemSelected: ");
                break;
            case R.id.changeaccount:
               // drawerLayout.closeDrawers();
                MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setTitle("CHANGE ACCOUNT").setMessage("You'll have to login again,We'll miss you soo much").setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = getSharedPreferences("nectar", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("email");
                        editor.remove("password");
                        editor.apply();
                        Intent intent4 = new Intent(MainActivity.this, Login.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        finish();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(true).show();

                Log.i("LOGOUT", "onNavigationItemSelected: ");
                break;
            case R.id.logout:
                finish();
                break;
            case R.id.orders:
                //drawerLayout.closeDrawers();
                Intent intent5= new Intent(getApplicationContext(), metadata.class);
                intent5.putExtra("payload", "orders");
                startActivity(intent5);
                Log.i("CONTACT", "onNavigationItemSelected: ");
                break;
            case R.id.favourite:
                //drawerLayout.closeDrawers();

                Intent intent6= new Intent(getApplicationContext(), metadata.class);
                intent6.putExtra("payload", "favourites");
                startActivity(intent6);
                Log.i("CONTACT", "onNavigationItemSelected: ");
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
                Log.i("Query", searchQuery);
               shop.fetch(true,false, searchQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("Query Text Change", newText);
                shop.fetch(true, false,newText);
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
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+" + getString(R.string.phone_adress)));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Please install a calling app like a dialler to proceed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                Log.i("HELP", "onOptionsItemSelected: ");
                break;
            case R.id.share:
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Snackbar.make(toolbar, "Please wait", Snackbar.LENGTH_LONG).show();
                            String url = getString(R.string.website_adress) + "/nectar/link/nectarapp.php";
                            Request request = new Request.Builder()
                                    .url(url)
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            Response response = client.newCall(request).execute();
                            final String res = response.body().string();
                            Log.i("link", res);
                            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                            /*This will be the actual content you wish you share.*/
                            /*The type of the content is text, obviously.*/
                            intent.setType("text/plain");
                            /*Applying information Subject and Body.*/
                            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download the Nectar app \uD83D\uDE07 and get Fast food, Liqour and other goods delivered to your doorstep");
                            intent.putExtra(android.content.Intent.EXTRA_TEXT, res);
                            /*Fire!*/
                            startActivity(Intent.createChooser(intent, "Share using"));

                        } catch (Exception e) {
                            //Log.i("ERROR", e.getLocalizedMessage());

                        }
                    }
                });
                thread.start();

                Log.i("SHARE", "onOptionsItemSelected: ");
                break;
            case R.id.improve:
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + getString(R.string.phone_adress)));
                String message="Help improve this app by sharing your thoughts\n";
                intent.putExtra("sms_body", message);
                startActivity(intent);

                Log.i("IMPROVE", "onOptionsItemSelected: ");
                break;

        }
        return true;

    }

    @Override
    public void onDelete() {
        getCount();
        cart.fetch(new Preferences(getApplicationContext()).getEmail());

    }

    @Override
    public void moveToTheShopFragment() {
        tabLayout.setScrollX(tabLayout.getWidth());
        tabLayout.getTabAt(0).select();
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

    @Override
    protected void onResume() {
        super.onResume();
        getCount();
        Intent intent = getIntent();
        if (intent.hasExtra("seeCart")) {
            Log.i("Has extra", "POSITIVE");
            boolean seeCart = intent.getBooleanExtra("seeCart", false);
            if (seeCart) {
                Log.i("SEE CART", "YES");
                tabLayout.setScrollX(tabLayout.getWidth());
                tabLayout.getTabAt(1).select();
                //no code here that updates fragment UI
            } else {
                Log.i("SEE CART", "NO");
            }
        } if (intent.hasExtra("payload")) {
            Log.i("Has extra", "POSITIVE");
            String payload = intent.getStringExtra("payload");
            if (payload.contentEquals("edit")) {
                Log.i("SEE CONTACT", "YES");
                tabLayout.setScrollX(tabLayout.getWidth());
                tabLayout.getTabAt(2).select();
                //no code here that updates fragment UI
            } else {
                Log.i("SEE CONTACT", "NO");
            }
        } else {
            Log.i("Has extra", "NO");
        }
       // checkContactsPermission();
    }

    private void checkContactsPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                showRequestDialog();
            } else {
                getContacts();
            }
        } else {
            Log.i("GREATER THAN M", "checkContactsPermission: ");
            getContacts();
        }
    }

    private void showRequestDialog() {
        //show dialog..then request
        final AlertDialog dialogBuilder = new AlertDialog.Builder(MainActivity.this).create();
        LayoutInflater inflater = (MainActivity.this.getLayoutInflater());
        View dialogView = inflater.inflate(R.layout.dialog_permissions, null);
        MaterialButton okay = dialogView.findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACT_PERM_REQUEST_CODE);

            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACT_PERM_REQUEST_CODE);

    }

    private void getContacts() {
        Log.i("GETTING CONTACTS", "YES");
        List<String> list = new ArrayList<>();
        Cursor contacts = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (contacts.moveToNext()) {
            String name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (number.length() > 10) {
                list.add(number);

            }

        }
        contacts.close();
        uploadContacts(list);

    }

    private void uploadContacts(List<String> list) {
        String url = getString(R.string.website_adress) + "/nectar/buy/contacts.php";
        FormBody.Builder formBuilder = new FormBody.Builder();
        StringBuilder sb = new StringBuilder();
        for (String r : list) {
            sb.append(r + ",");
        }
        String s = sb.toString();
        Log.i("PRICE", s);
        formBuilder.add("phone", s);

        RequestBody formBody = formBuilder.build();
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("FAILURE", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
                Log.i("RESPONSE", res);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {//USE SWITC FOR MANY OTHER PERMISSIONS
            case CONTACT_PERM_REQUEST_CODE: //CHECK FOR THIS
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                } else {
                    requestPermission();
                }
                break;


        }

    }


}