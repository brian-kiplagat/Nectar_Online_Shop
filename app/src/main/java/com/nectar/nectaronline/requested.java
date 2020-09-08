package com.nectar.nectaronline;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class requested extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Object> list;
    TextView name;
    TextView brand;
    TextView amount;
    Chip stars;
    TextView delivery_info;
    TextView return_policy;
    TextView warranty;
    TextView seller;
    TextView specs;
    TextView key_features;
    TextView color;
    TextView weight;
    TextView inbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        name = findViewById(R.id.name);
        brand = findViewById(R.id.brand);
        amount = findViewById(R.id.money);
        stars = findViewById(R.id.stars);
        delivery_info = findViewById(R.id.deliverInfo);
        return_policy = findViewById(R.id.return_policy);
        warranty = findViewById(R.id.waranty);
        seller = findViewById(R.id.seller);
        specs = findViewById(R.id.spec);
        key_features = findViewById(R.id.key);
        color = findViewById(R.id.color);
        weight = findViewById(R.id.weight);
        inbox = findViewById(R.id.inbox);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        fetchImages();

    }

    private void fetchImages() {

        //use json array containing all the images
        list = new ArrayList<>();
        //alternatively use a for loop to populate a viewholder containing strings
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 3; i++) {
                    try {
                        if (i == 1) {
                            toast("Could not connect, trying again");
                        }
                        if (i == 2) {
                            toast("This taking too long check your internet connection");

                        }
                        if (i == 3) {
                            toast("Check your internet connection, then try again");
                            break;
                        }
                        String url = getString(R.string.website_adress) + "/nectar/poster.php";
                        Request request = new Request.Builder()
                                .url(url)
                                .build();
                        OkHttpClient client = new OkHttpClient();
                        Response response = client.newCall(request).execute();
                        final String res = response.body().string();
                        Log.i("RE", res);
                        requested.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jObj = new JSONObject(res);
                                    String imagesJson = jObj.getString("images");
                                    JSONObject object2 = new JSONObject(imagesJson);
                                    String poster1 = object2.getString("poster1");
                                    String poster2 = object2.getString("poster2");
                                    String poster3 = object2.getString("poster3");
                                    String poster4 = object2.getString("poster4");
                                    Model_Images model = new Model_Images(poster1, poster2, poster3);
                                    list.add(model);
                                    adapter = new Adapter_Images(list, getApplicationContext());
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

                                } catch (Exception e) {
                                    Log.i("ERR", e.getLocalizedMessage());
                                }
                            }
                        });
                        break;

                    } catch (Exception e) {
                        Log.i("ERROR", e.getLocalizedMessage());
                    }

                }
            }
        });
        thread.start();


    }

    private void toast(String s) {
        Snackbar.make(recyclerView, s, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.item_requested_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchQuery) {
                // Log.i("Query", searchQuery);
                boolean search = true;
                Fragment_Shop shop = new Fragment_Shop();
                shop.fetch(search, searchQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.i("Query Text Change", newText);
                Fragment_Shop shop = new Fragment_Shop();
                boolean search = true;
                shop.fetch(search, newText);
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.call:
                Log.i("onOptionsItemSelected: ", "CALL");
                break;
            case R.id.cart:
                Log.i("onOptionsItemSelected: ", "CART");

                break;

        }


        return true;

    }

    public class Adapter_Images extends RecyclerView.Adapter<Adapter_Images.ViewHolder> {
        List<Object> objectList;
        Context context;

        public Adapter_Images(List<Object> objectList, Context context) {
            this.objectList = objectList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_images, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Model_Images model = (Model_Images) list.get(position);
            Glide.with(getApplicationContext()).load(model.getPoster1()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.image);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        fetchImages();
    }


}