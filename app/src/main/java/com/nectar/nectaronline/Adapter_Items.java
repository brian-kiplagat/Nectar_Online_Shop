package com.nectar.nectaronline;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

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
        String link = context.getString(R.string.website_adress) + "/nectar/seller/" + model.getImages();
        Glide.with(context).load(link).into(holder.image);
        holder.shimm.stopShimmer();
        holder.shimm.setVisibility(View.GONE);
        holder.content.setVisibility(View.VISIBLE);
        holder.brand.setText(model.getBrand());
        holder.price.setText("KSH " + model.getFinalPrice());
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
        com.facebook.shimmer.ShimmerFrameLayout shimm;
        RelativeLayout content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            shimm = itemView.findViewById(R.id.shimmer);
            brand = itemView.findViewById(R.id.brand);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
