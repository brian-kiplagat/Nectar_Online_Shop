package com.nectar.nectaronline;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;
import java.util.Random;

public class Adapter_PlaceHolder extends RecyclerView.Adapter<Adapter_PlaceHolder.ViewHolder> {
    Context context;
    List<Object> list;

    public Adapter_PlaceHolder(Context context, List<Object> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_empty_cart_placeholder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_PlaceHolder model = (Model_PlaceHolder) list.get(position);
        Random rand = new Random(); //instance of random class
        int low = 0;
        int high =5;
        int int_random = rand.nextInt(high - low) + low;
        Log.i("RANDOM:->", String.valueOf(int_random));
        if (int_random == 0) {
            holder.shop.setText("Add items");
            holder.lotie.setAnimation(R.raw.shopping1);
            holder.lotie.setRepeatCount(Animation.INFINITE);

        } else if (int_random == 1) {
            holder.lotie.setAnimation(R.raw.shopping_checlist_app);
            holder.shop.setText("Ops! Nothing here");
            holder.lotie.setRepeatCount(Animation.INFINITE);

        }
        if (int_random == 2) {
            holder.lotie.setAnimation(R.raw.shopping3);
            holder.shop.setText("Could find anything");
            holder.lotie.setRepeatCount(Animation.INFINITE);

        }
        if (int_random == 3) {
            holder.lotie.setAnimation(R.raw.delivery);
            holder.shop.setText("20 minute delivery");
            holder.lotie.setRepeatCount(Animation.INFINITE);

        }
        if (int_random == 4) {
            holder.lotie.setAnimation(R.raw.truck);
            holder.shop.setText("Fast home delivery");
            holder.lotie.setRepeatCount(Animation.INFINITE);

        }
        if (int_random == 5) {
            holder.lotie.setAnimation(R.raw.truck);
            holder.shop.setText("Direct doorstep delivery");
            holder.lotie.setRepeatCount(Animation.INFINITE);

        }

    }

    @Override
    public int getItemCount() {
        Log.i(String.valueOf(list.size()), "getItemCount: ");

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LottieAnimationView lotie;
        TextView shop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lotie = itemView.findViewById(R.id.lotieanimation);
            shop = itemView.findViewById(R.id.word);
        }
    }


}
