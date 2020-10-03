package com.nectar.nectaronline;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        int high = 3;
        int int_random = rand.nextInt(high - low) + low;
        Log.i("RANDOM:->", String.valueOf(int_random));
        if (int_random == 0) {
            holder.alien.setImageResource(R.drawable.ic_ufo);
        } else if (int_random == 1) {
            holder.alien.setImageResource(R.drawable.alien);
        }
        if (int_random == 2) {
            holder.alien.setImageResource(R.drawable.alien2);
        } else {
            holder.alien.setImageResource(R.drawable.alien3);

        }

    }

    @Override
    public int getItemCount() {
        Log.i(String.valueOf(list.size()), "getItemCount: ");

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView alien;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alien = itemView.findViewById(R.id.alien);
        }
    }


}
