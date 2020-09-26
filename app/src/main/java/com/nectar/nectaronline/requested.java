package com.nectar.nectaronline;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    Chip instock;
    Chip state;

    TextView material;
    TextView description;
    TextView size;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BUY");
        setSupportActionBar(toolbar);
        add = findViewById(R.id.button);
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
        ID = intent.getStringExtra("id");
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
            case R.id.whatsapp:
                Log.i("onOptionsItemSelected: ", "WHATSAPP");
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("saved to clip", getString(R.string.kopokopTill_for_messaging_payment));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Till number: " + getString(R.string.kopokopTill_for_messaging_payment) + " copied to clipboard", Toast.LENGTH_SHORT).show();
                String link = getString(R.string.website_adress) + "/nectar/" + IMAGES;
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

    public void addToCart(View view) {
        add.setEnabled(false);
        toast("Adding to cart");
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    SharedPreferences preferences = getSharedPreferences("nectar", MODE_PRIVATE);
                    String number = preferences.getString("number", "");
                    String email = preferences.getString("email", "");
                    String url = getString(R.string.website_adress) + "/nectar/addtocart.php";
                    RequestBody formBody = new FormBody.Builder()
                            .add("phone", number)
                            .add("id", ID)
                            .add("email", email)
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

}