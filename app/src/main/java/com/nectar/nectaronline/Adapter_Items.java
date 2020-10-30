package com.nectar.nectaronline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.text.NumberFormat;
import java.util.List;
import java.util.NoSuchElementException;

public class Adapter_Items extends RecyclerView.Adapter<Adapter_Items.ViewHolder> {
    Context context;
    List<Object> list;
    Clicked clicked;

    public Adapter_Items(Context context, List<Object> list, Clicked clicked) {
        this.context = context;
        this.list = list;
        this.clicked = clicked;
    }

    public interface Clicked {
        void onClick();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ModeL_Shop_Items model = (ModeL_Shop_Items) list.get(position);
        holder.content.setVisibility(View.INVISIBLE);
        try {
            // JSONObject object = new JSONObject(model.getImages());
            JSONArray array = new JSONArray(model.getImages());
            String prelink = array.getString(0);
            Log.i("LINK",prelink);
            String link = context.getString(R.string.website_adress) + "/nectar/seller/" +prelink;
            //     Glide.with(context).load(link).into(holder.image);
            Picasso.get().load(link).placeholder(R.drawable.alien).into(holder.image);

        } catch (Exception e) {
            Log.i("PARSE ERROR",e.getLocalizedMessage());
        }
        holder.content.setVisibility(View.VISIBLE);
        holder.brand.setText(model.getBrand());
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true); // this will also round numbers, 3
        holder.price.setText("KSH " + myFormat.format(Integer.parseInt(model.getFinalPrice())));
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
                intent.putExtra("sellerID", sellerID);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i(String.valueOf(list.size()), "getItemCount: ");

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView brand;
        TextView price;
        ImageView image;
        MaterialCardView cardView;
        RelativeLayout content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            brand = itemView.findViewById(R.id.brand);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }


}
