package com.nectar.nectaronline;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Adapter_Shop extends RecyclerView.Adapter<Adapter_Shop.ViewHolder> {
    Context context;
    List<Object> list;
    ChangedListener changedListener;

    public interface ChangedListener {
        void onChange();
    }


    public Adapter_Shop(Context context, List<Object> list, ChangedListener changedListener) {
        this.context = context;
        this.list = list;
        this.changedListener = changedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ModeL_Shop_Items model = (ModeL_Shop_Items) list.get(position);
        final int min = 1;
        final int max = 6;
        final int random = new Random().nextInt((max - min) + 1) + min;
        Animation slide= AnimationUtils.loadAnimation(context,R.anim.fromleft);
        holder.ratingBar.startAnimation(slide);
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setRating((float) random);

        try {
            // JSONObject object = new JSONObject(model.getImages());
            JSONArray array = new JSONArray(model.getImages());
            String prelink = array.getString(0);
            Log.i("LINK",prelink);
            String link = context.getString(R.string.website_adress) + "/nectar/seller/" +prelink;
            Picasso.get().load(link).placeholder(R.drawable.alien).into(holder.image);
            //Glide.with(context).load(link).into(holder.image);
        } catch (Exception e) {
            Log.i("PARSE ERROR",e.getLocalizedMessage());
        }
        holder.brand.setText(model.getBrand());
        holder.name.setText(model.getName());
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true); // this will also round numbers, 3
        holder.price.setText(context.getString(R.string.cashUnit)+" " + myFormat.format(Integer.parseInt(model.getFinalPrice())));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = model.getId();
                final String brand = model.getBrand();
                final String name = model.getName();
                final String newPrice = model.getFinalPrice();
                final String old = model.getInitialPrice();
                final String description = model.getDescription();
                final String specification = model.getSpecification();
                final String keyfeatures = model.getKeyFeatures();
                final String size = model.getSize();
                final String color = model.getColour();
                final String instock = model.getInstock();
                final String weight = model.getWeight();
                final String material = model.getMaterial();
                final String inbox = model.getInsideBox();
                final String waranty = model.getWarranty();
                final String state = model.getState();
                final String images = model.getImages();
                final String sellerID = model.getSellerID();
                Intent intent = new Intent(context, requested.class);
                intent.putExtra("id", id);
                intent.putExtra("brand", brand);
                intent.putExtra("name", name);
                intent.putExtra("newPrice", newPrice);
                intent.putExtra("old", old);
                intent.putExtra("description", description);
                intent.putExtra("specification", specification);
                intent.putExtra("keyfeatures", keyfeatures);
                intent.putExtra("size", size);
                intent.putExtra("color", color);
                intent.putExtra("instock", instock);
                intent.putExtra("weight", weight);
                intent.putExtra("material", material);
                intent.putExtra("inbox", inbox);
                intent.putExtra("waranty", waranty);
                intent.putExtra("state", state);
                intent.putExtra("images", images);
                intent.putExtra("sellerID",sellerID);
                intent.putExtra("rating",String.valueOf(random));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity(intent);

            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Adding", v);
                addToCart(model.getId(), holder.add);

            }
        });

        holder.favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtofav(model.getId(), holder.favourites);
            }
        });

    }

    private void addToCart(String ID, final View v) {
        Preferences preferences = new Preferences(context);
        String url = context.getString(R.string.website_adress) + "/nectar/buy/addtocart.php";
        RequestBody formBody = new FormBody.Builder()

                .add("productID", ID)
                .add("email", preferences.getEmail())
                .add("phone", preferences.getNumber())
                .add("quantity", "1")
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
                try {
                    final String res = response.body().string().trim();
                    Log.i("ADDING TO CART", res);
                    JSONObject obj = new JSONObject(res);
                    String code = obj.getString("RESPONSE_CODE");
                    if (code.contentEquals("SUCCESS")) {
                        toast("Added to cart", v);
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                changedListener.onChange();


                            }
                        });

                    } else {
                        toast("Ops ! Lets try that again", v);
                        String desc = obj.getString("RESPONSE_DESC");
                        Log.i("ERROR: ", desc);
                    }
                } catch (Exception e) {
                    Log.i("ERROR: ", e.getLocalizedMessage());
                }
            }
        });

    }

    private void addtofav(final String id, final ImageView favourites) {
        Preferences preferences = new Preferences(context);
        String url = context.getString(R.string.website_adress) + "/nectar/buy/addtofav.php";
        RequestBody formBody = new FormBody.Builder()
                .add("email", preferences.getEmail())
                .add("productID", id)
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
                try {
                    JSONObject obj = new JSONObject(res);
                    String code = obj.getString("RESPONSE_CODE");
                    String desc = obj.getString("RESPONSE_DESC");
                    if (code.contentEquals("SUCCESS")) {
                        toast("Added to favourites",favourites);
                        favourites.post(new Runnable() {
                            @Override
                            public void run() {
                                favourites.setImageResource(R.drawable.ic_baseline_favorite_24);


                            }
                        });
                    } else{
                        toast("Ops! Try again",favourites);

                    }
                } catch (Exception e) {
                    toast("Ops! An error occured Try again",favourites);

                }

            }
        });
        Log.i("REQUESTED", "ADD TO CART");

    }


    private void toast(String s,View v) {
        Snackbar.make(v,s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;
        TextView brand;
        TextView name;
        TextView price;
        ImageView image;
        ConstraintLayout cardView;
        ImageButton favourites;
        ImageButton add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            add=itemView.findViewById(R.id.addToCart);
            favourites=itemView.findViewById(R.id.favourite);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            name=itemView.findViewById(R.id.name);
            brand = itemView.findViewById(R.id.brand);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
