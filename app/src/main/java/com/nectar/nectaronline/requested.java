package com.nectar.nectaronline;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    Chip instock;
    Chip state;

    TextView material;
    TextView description;
    TextView size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BUY");
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
        instock = findViewById(R.id.instock);
        material = findViewById(R.id.material);
        description = findViewById(R.id.description);
        size = findViewById(R.id.size);
        state = findViewById(R.id.state);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Intent intent = getIntent();
        String ID = intent.getStringExtra("id");
        String BRAND = intent.getStringExtra("brand");
        String NAME = intent.getStringExtra("name");
        String NEWPRICE = intent.getStringExtra("newPrice");
        String OLD = intent.getStringExtra("old");
        String DESC = intent.getStringExtra("description");
        String SPEC = intent.getStringExtra("specification");
        String KEYFEATURES = intent.getStringExtra("keyfeatures");
        String SIZE = intent.getStringExtra("size");
        String COLOR = intent.getStringExtra("color");
        String INSTOCK = intent.getStringExtra("instock");
        String WEIGHT = intent.getStringExtra("weight");
        String MATERIAL = intent.getStringExtra("material");
        String WHATSINTHEBOX = intent.getStringExtra("inbox");
        String WARANTY = intent.getStringExtra("waranty");
        String STATE = intent.getStringExtra("state");
        String IMAGES = intent.getStringExtra("images");
        fetchImages(IMAGES);

        name.setText(NAME);
        brand.setText("Brand " + BRAND);
        amount.setText("KSH " + NEWPRICE);
        warranty.setText(WARANTY);
        specs.setText(SPEC);
        key_features.setText(KEYFEATURES);
        color.setText(COLOR);
        weight.setText(WEIGHT);
        inbox.setText(WHATSINTHEBOX);
        instock.setText(INSTOCK);
        material.setText(MATERIAL);
        description.setText(DESC);
        size.setText(SIZE);
        if (STATE.contentEquals("BRAND")) {
            state.setText("BRAND NEW");
            state.setChipIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_brand_new, null));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                state.setChipIconTint(getResources().getColorStateList(R.color.yellow, null));
            }
        }
        if (STATE.contentEquals("REFURBISHED")) {
            state.setText("REFURBISHED");
            state.setChipIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_refurb, null));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                state.setChipIconTint(getResources().getColorStateList(R.color.green, null));
            }
        }
        if (STATE.contentEquals("SECOND")) {
            state.setText("SECOND HAND");
            state.setChipIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_second_hand, null));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                state.setChipIconTint(getResources().getColorStateList(R.color.orange, null));
            }
        }


    }

    private void fetchImages(String images) {

        //use json array containing all the images
        Log.i("fetchImages: ", images);
        list = new ArrayList<>();
        try {
            JSONObject jObj = new JSONObject(images);
            JSONArray array=jObj.getJSONArray("image");
            for (int i=0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                String poster=object.getString("poster");
                Model_Images model = new Model_Images(poster);
                list.add(model);
                adapter = new Adapter_Images(list, getApplicationContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }


        } catch (Exception e) {
            Log.i("ERR", e.getLocalizedMessage());
        }

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
                Log.i("Query", searchQuery);
                boolean search = true;
                Fragment_Shop shop = new Fragment_Shop();
                shop.fetch(search, searchQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("Query Text Change", newText);
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
            Glide.with(getApplicationContext()).load(model.getPoster()).into(holder.imageView);
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

}