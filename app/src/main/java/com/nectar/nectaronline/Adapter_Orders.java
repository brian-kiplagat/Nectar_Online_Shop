package com.nectar.nectaronline;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import java.util.List;

public class Adapter_Orders extends RecyclerView.Adapter<Adapter_Orders.ViewHolder> {
    Context context;
    List<Object> list;

    public Adapter_Orders(Context context, List<Object> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_orders, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_Orders model = (Model_Orders) list.get(position);
        String link = context.getString(R.string.website_adress) + "/nectar/seller/" + model.getImages();
        Glide.with(context).load(link).into(holder.prod_image);
        holder.brand.setText(model.getBrand());
        holder.name.setText(model.getName());
        holder.number_of_items.setText(model.getQuantity());
        holder.cash.setText(context.getString(R.string.cashUnit) + " " + model.getFinalPrice());

        holder.shimm.stopShimmer();
        holder.shimm.setVisibility(View.GONE);
        holder.prod_image.setVisibility(View.VISIBLE);
        holder.brand.setText(model.getBrand());
        holder.price.setText(model.getFinalPrice());
        if (model.getState().contentEquals("BRAND")) {
            holder.state.setText("BRAND NEW");
            holder.state.setChipIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_baseline_brand_new, null));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                holder.state.setChipIconTint(context.getResources().getColorStateList(R.color.yellow, null));
            }
        } else if (model.getState().contentEquals("REFURBISHED")) {
            holder.state.setText("REFURBISHED");
            holder.state.setChipIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_baseline_refurb, null));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                holder.state.setChipIconTint(context.getResources().getColorStateList(R.color.blue_azure, null));
            }
        } else if (model.getState().contentEquals("SECOND")) {
            holder.state.setText("SECOND HAND");
            holder.state.setChipIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_baseline_second_hand, null));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                holder.state.setChipIconTint(context.getResources().getColorStateList(R.color.orange, null));
            }
        } else if (model.getState().contentEquals("FRESH")) {
            holder.state.setText("FRESH");
            holder.state.setChipIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_vegetables, null));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                holder.state.setChipIconTint(context.getResources().getColorStateList(R.color.blue_azure, null));
            }
        } else if (model.getState().contentEquals("FRESHLY COOKED")) {
            holder.state.setText("FRESHLY COOKED");
            holder.state.setChipIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_baseline_fastfood_24, null));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                holder.state.setChipIconTint(context.getResources().getColorStateList(R.color.purple_light, null));
            }
        }

        holder.size.setText("Size: " + model.getSize());
        if (model.getDelivery().contentEquals("processing")) {
            holder.delivery.setText("Processing");
            holder.delivery.setChipIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_baseline_security_24, null));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                holder.state.setChipIconTint(context.getResources().getColorStateList(R.color.yellow, null));
            }
        } else if (model.getDelivery().contentEquals("transit")) {
            holder.delivery.setText("In transit");
            holder.delivery.setChipIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_baseline_local_shipping_24, null));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                holder.state.setChipIconTint(context.getResources().getColorStateList(R.color.blue_azure, null));
            }
        } else if (model.getDelivery().contentEquals("delivered")) {
            holder.delivery.setText("Delivered");
            holder.delivery.setChipIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_baseline_done_all_24, null));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                holder.state.setChipIconTint(context.getResources().getColorStateList(R.color.orange, null));
            }
        }

    }

    @Override
    public int getItemCount() {
        Log.i(String.valueOf(list.size()), "getItemCount: ");

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cash;

        TextView brand;
        TextView name;
        TextView size;
        Chip state;
        Chip delivery;
        TextView price;
        TextView number_of_items;
        ImageView prod_image;
        ImageView fav;
        RelativeLayout cartStuff;
        TextView explanation;
        com.facebook.shimmer.ShimmerFrameLayout shimm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cash = itemView.findViewById(R.id.CASH);
            fav = itemView.findViewById(R.id.favourite);
            cartStuff = itemView.findViewById(R.id.cartStuff);
            delivery = itemView.findViewById(R.id.DELIVERY);
            brand = itemView.findViewById(R.id.BRAND);
            name = itemView.findViewById(R.id.NAME);
            size = itemView.findViewById(R.id.SIZE);
            state = itemView.findViewById(R.id.STATE);
            price = itemView.findViewById(R.id.PRICE);
            number_of_items = itemView.findViewById(R.id.center);
            prod_image = itemView.findViewById(R.id.drinkimage);
            shimm = itemView.findViewById(R.id.shimmer);
            explanation = itemView.findViewById(R.id.Explanation);
        }
    }


}
