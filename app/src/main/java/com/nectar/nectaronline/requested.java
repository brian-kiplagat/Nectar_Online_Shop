package com.nectar.nectaronline;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class requested extends AppCompatActivity implements Adapter_Items.Clicked, View.OnClickListener {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView sellersRecyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.Adapter sellersAdapter;

    List<Object> list;
    List<Object> sellerList = new ArrayList<>();
    TextView name;
    TextView brand;
    TextView amount;
    Chip stars;
    Chip previos;
    com.mikhaellopez.circularimageview.CircularImageView circularImageView;
    com.mikhaellopez.circularimageview.CircularImageView circularImageView2;
    TextView sellerName2;
    TextView sellerDescription;

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

    String SELLERID;
    String ID;
    String BRAND;
    String NAME;
    String NEWPRICE;
    String OLD;
    String DESC;
    String SPEC;
    String KEYFEATURES;
    String SIZE;
    String COLOR;
    String INSTOCK;
    String WEIGHT;
    String MATERIAL;
    String WHATSINTHEBOX;
    String WARANTY;
    String STATE;
    String IMAGES;
    Button add;
    ImageView share;
    ImageView favourites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BUY");
        setSupportActionBar(toolbar);

        circularImageView = findViewById(R.id.circularImageView);
        circularImageView2 = findViewById(R.id.circularImageViewseller);
        sellerName2 = findViewById(R.id.seller_name);
        sellerDescription = findViewById(R.id.seller_desc);
        add = findViewById(R.id.button);
        favourites = findViewById(R.id.fav);
        share = findViewById(R.id.share);
        name = findViewById(R.id.name);
        brand = findViewById(R.id.brand);
        amount = findViewById(R.id.money);
        stars = findViewById(R.id.stars);
        previos = findViewById(R.id.previos);

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
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        sellersRecyclerView = findViewById(R.id.recycler_view_sellers);
        sellersRecyclerView.setHasFixedSize(true);
        sellersRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        share.setOnClickListener(this);
        favourites.setOnClickListener(this);
        add.setOnClickListener(this);

        Intent intent = getIntent();
        ID = intent.getStringExtra("id");//Each is unique to the product
        SELLERID = intent.getStringExtra("sellerID");//Each is unique to the product
        BRAND = intent.getStringExtra("brand");
        NAME = intent.getStringExtra("name");
        NEWPRICE = intent.getStringExtra("newPrice");
        OLD = intent.getStringExtra("old");
        DESC = intent.getStringExtra("description");
        SPEC = intent.getStringExtra("specification");
        KEYFEATURES = intent.getStringExtra("keyfeatures");
        SIZE = intent.getStringExtra("size");
        COLOR = intent.getStringExtra("color");
        INSTOCK = intent.getStringExtra("instock");
        WEIGHT = intent.getStringExtra("weight");
        MATERIAL = intent.getStringExtra("material");
        WHATSINTHEBOX = intent.getStringExtra("inbox");
        WARANTY = intent.getStringExtra("waranty");
        STATE = intent.getStringExtra("state");
        IMAGES = intent.getStringExtra("images");
        fetchImages(IMAGES);
        previos.setText("KSH " + OLD);
        name.setText(NAME);
        brand.setText("Brand: " + BRAND);
        amount.setText("KSH " + NEWPRICE);
        warranty.setText(WARANTY);
        specs.setText(SPEC);
        key_features.setText(KEYFEATURES);
        color.setText(COLOR);
        weight.setText(WEIGHT);
        inbox.setText(WHATSINTHEBOX);
        instock.setText("Instock " + INSTOCK);
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
        // new Seller().execute();
        getSellerInfo(SELLERID);

    }

    private void getSellerInfo(String sellerid) {
        String url = getString(R.string.website_adress) + "/nectar/seller/getsellerinfo.php";
        RequestBody formBody = new FormBody.Builder()
                .add("id", sellerid)//then from server can check if to search or not the return an appropriate respons
                .build();

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        //Response response = client.newCall(request).execute();
        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String res = response.body().string();
                    Log.i("RESPONSE", res);
                    try {
                        JSONObject obj = new JSONObject(res);
                        String code = obj.getString("RESPONSE_CODE");
                        String desc = obj.getString("RESPONSE_DESC");
                        updateContacts(obj);
                        sellerList.clear();
                        if (code.contentEquals("SUCCESS")) {
                            String STUFF = obj.getString("SHOP_ITEMS");
                            JSONObject object = new JSONObject(STUFF);
                            JSONArray array = object.getJSONArray("items");

                            for (int h = 0; h < array.length(); h++) {
                                JSONObject obje = array.getJSONObject(h);
                                final String id = obje.getString("id");
                                final String brand = obje.getString("brand");
                                final String name = obje.getString("name");
                                final String newPrice = obje.getString("new");
                                final String old = obje.getString("old");
                                final String description = obje.getString("description");
                                final String specification = obje.getString("specification");
                                final String keyfeatures = obje.getString("keyfeatures");
                                final String size = obje.getString("size");
                                final String color = obje.getString("color");
                                final String instock = obje.getString("instock");
                                final String weight = obje.getString("weight");
                                final String material = obje.getString("material");
                                final String inbox = obje.getString("inbox");
                                final String waranty = obje.getString("waranty");
                                final String state = obje.getString("state");
                                final String images = obje.getString("images");
                                final String sellerID = obje.getString("sellerID");
                                ModeL_Shop_Items model = new ModeL_Shop_Items(name, brand, id, newPrice, old, description, keyfeatures, specification, color, size, weight, material, inbox, waranty, instock, state, images, sellerID);
                                sellerList.add(model);

                                requested.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sellersAdapter = new Adapter_Items(getApplicationContext(), sellerList, requested.this);
                                        sellersAdapter.notifyDataSetChanged();
                                        sellersRecyclerView.setAdapter(sellersAdapter);
                                    }
                                });


                            }


                        } else {
                            Log.i("DESC ERROR: ", desc);
                        }
                    } catch (Exception e) {
                        Log.i("EXCEPTION", e.getLocalizedMessage());

                    }


                }
            });
        } catch (Exception e) {
            Log.i("eeee EXCEPTION", e.getLocalizedMessage());
        }
    }


    private void fetchImages(String images) {
        list = new ArrayList<>();
        Model_Images model = new Model_Images(IMAGES);
        list.add(model);
        adapter = new Adapter_Images(list, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /*//use json array containing all the images
        Log.i("fetchImages: ", images);
        list = new ArrayList<>();
        try {
            JSONObject jObj = new JSONObject(images);
            JSONArray array = jObj.getJSONArray("image");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String poster = object.getString("poster");
                Model_Images model = new Model_Images(poster);
                list.add(model);
                adapter = new Adapter_Images(list, getApplicationContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }


        } catch (Exception e) {
            Log.i("ERR", e.getLocalizedMessage());
        }*/

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

                Fragment_Shop shop = new Fragment_Shop();
                shop.fetch(true, true, searchQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("Query Text Change", newText);
                Fragment_Shop shop = new Fragment_Shop();
                shop.fetch(true, true, newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.whatsapp:
                Log.i("onOptionsItemSelected: ", "WHATSAPP");
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("saved to clip", getString(R.string.kopokopTill_for_messaging_payment));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Till number: " + getString(R.string.kopokopTill_for_messaging_payment) + " copied to clipboard", Toast.LENGTH_SHORT).show();
                String link = getString(R.string.website_adress) + "/nectar/seller/" + IMAGES;
                String string = "RE: Order for goods" + "\n" + "Im interested in this" + "\n" + "Brand: " + BRAND + "\n" + "Name: "
                        + NAME + "\n" + "DESCRIPTION: " + DESC + "\n" +
                        "SPECIFICATION: " + SPEC + "\n" + "KEY FEATURES: " +
                        KEYFEATURES + "\n" + "SIZE: " + SIZE + "\n" + "COLOR: " + COLOR + "\n" + "INSTOCK: " +
                        INSTOCK + "\n" + "WEIGHT: " + WEIGHT + "\n" + "MATERIAL: " + MATERIAL + "\n" + "WHATS IN THE BOX: " +
                        WHATSINTHEBOX + "\n" + "WARANTY: " + WARANTY + "\n" + "STATE: " + STATE + "\n" + "Image link: " + "\n" + link + "\n" + "PRICE: Ksh " + NEWPRICE +
                        "\n" + "To complete this order, Now go to your Mpesa menu, select lipa na Mpesa, select buy goods and services, enter our till number:" + getString(R.string.kopokopTill_for_messaging_payment) + " " + "we've copied it for you just paste, enter Ksh " + NEWPRICE + " then pay and finish the transaction, well process and deliver to your location ASAP.Note that this is a slower method. We advice you add this item to cart and checkout,  :-)" + "\n" + "****" + "\n";
                //now convert the string to a whatsapp message
                try {
                    String url = "https://api.whatsapp.com/send?phone=" + getString(R.string.phone_adress) + "&text=" + URLEncoder.encode(string, "UTF-8");
                    Toast.makeText(getApplicationContext(), "Taking you to Whatsapp", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent2);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "OPs! An error occurred, try again", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.call:
                Log.i("onOptionsItemSelected: ", "CALL");
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+" + getString(R.string.phone_adress)));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Please install a calling app like a dialler to proceed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


                break;
            case R.id.cart:
                Log.i("onOptionsItemSelected: ", "CART");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;

        }


        return true;

    }

    private void addToCart() {
        add.setEnabled(false);
        toast("Adding to cart");
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Preferences preferences = new Preferences(getApplicationContext());
                    String url = getString(R.string.website_adress) + "/nectar/buy/addtocart.php";
                    RequestBody formBody = new FormBody.Builder()

                            .add("productID", ID)
                            .add("sellerID", SELLERID)
                            .add("brand", BRAND)
                            .add("name", NAME)
                            .add("newPrice", NEWPRICE)
                            .add("old", OLD)
                            .add("color", COLOR)
                            .add("description", DESC)
                            .add("specification", SPEC)
                            .add("keyfeatures", KEYFEATURES)
                            .add("size", SIZE)
                            .add("color", COLOR)
                            .add("instock", INSTOCK)
                            .add("weight", WEIGHT)
                            .add("material", MATERIAL)
                            .add("inbox", WHATSINTHEBOX)
                            .add("waranty", WARANTY)
                            .add("state", STATE)
                            .add("images", IMAGES)
                            .add("email", preferences.getEmail())
                            .add("quantity", "1")
                            .build();

                    OkHttpClient client = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    final String res = response.body().string().trim();
                    Log.i("response", res);
                    JSONObject obj = new JSONObject(res);
                    String code = obj.getString("RESPONSE_CODE");
                    if (code.contentEquals("SUCCESS")) {
                        toast("Added to cart");
                    } else {
                        String desc = obj.getString("RESPONSE_DESC");
                        Log.i("ERROR: ", desc);
                    }
                } catch (Exception e) {
                    Log.i("ERROR: ", e.getLocalizedMessage());
                }

            }
        });
        thread.start();
        add.setEnabled(true);
    }

    @Override
    public void onClick() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("saved to clip", getString(R.string.kopokopTill_for_messaging_payment));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Till number: " + getString(R.string.kopokopTill_for_messaging_payment) + " copied to clipboard", Toast.LENGTH_SHORT).show();
                String link = getString(R.string.website_adress) + "/nectar/seller/" + IMAGES;
                String string = "Hey there check this out \uD83E\uDD13 \uD83D\uDE0E" + "\n" + "Brand: " + BRAND + "\n" + "Name: "
                        + NAME + "\n" + "DESCRIPTION: " + DESC + "\n" +
                        "SPECIFICATION: " + SPEC + "\n" + "KEY FEATURES: " +
                        KEYFEATURES + "\n" + "SIZE: " + SIZE + "\n" + "COLOR: " + COLOR + "\n" + "INSTOCK: " +
                        INSTOCK + "\n" + "WEIGHT: " + WEIGHT + "\n" + "MATERIAL: " + MATERIAL + "\n" + "WHATS IN THE BOX: " +
                        WHATSINTHEBOX + "\n" + "WARANTY: " + WARANTY + "\n" + "STATE: " + STATE + "\n" + "Image link: " + "\n" + link + "\n" + "PRICE: Ksh " + NEWPRICE +
                        "\n" + "You can buy this now by going to your Mpesa menu, select lipa na Mpesa, select buy goods and services, enter our till number:" + getString(R.string.kopokopTill_for_messaging_payment) + " " + "enter Ksh " + NEWPRICE + " then pay and finish the transaction, well process and deliver to your location ASAP. For this and other refined products click this link to download our android app,  :-) " + getString(R.string.playstore_link);
                //now convert the string to a whatsapp message

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                /*This will be the actual content you wish you share.*/
                /*The type of the content is text, obviously.*/
                intent.setType("text/plain");
                /*Applying information Subject and Body.*/
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download the Nectar app \uD83D\uDE07 and get Fast food, Liqour and other goods delivered to your doorstep");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, string);
                /*Fire!*/
                startActivity(Intent.createChooser(intent, "Share using"));

                Log.i("REQUESTED", "SHARE");
                break;

            case R.id.fav:
                Preferences preferences = new Preferences(getApplicationContext());
                String url = getString(R.string.website_adress) + "/nectar/client/addtofav.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("email", preferences.getEmail())
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
                        Log.i("FAV-RESPONSE", res);
                    }
                });
                Log.i("REQUESTED", "ADD TO CART");
                break;
            case R.id.button:
                addToCart();
                break;

        }
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
            String link = getString(R.string.website_adress) + "/nectar/seller/" + model.getPoster();
            Glide.with(context).load(link).into(holder.imageView);

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


    private void updateContacts(JSONObject obj) {
        try {
            JSONArray array = obj.getJSONArray("CONTACT");
            JSONObject object = array.getJSONObject(0);
            final String SELLER_NAME = object.getString("name");
            final String SELLER_DESC = object.getString("desc");
            String EMAIL = object.getString("email");
            String NUMBER = object.getString("phone");

            final String IMAGE = object.getString("image");
            requested.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getApplicationContext()).load(getString(R.string.website_adress) + "/nectar/seller/" + IMAGE).into(circularImageView);
                    Glide.with(getApplicationContext()).load(getString(R.string.website_adress) + "/nectar/seller/" + IMAGE).into(circularImageView2);
                    sellerDescription.setText(SELLER_DESC);
                    seller.setText(SELLER_NAME);
                    sellerName2.setText(SELLER_NAME);

                }
            });
        } catch (Exception e) {
            Log.i("E", e.getLocalizedMessage());
        }

    }
}