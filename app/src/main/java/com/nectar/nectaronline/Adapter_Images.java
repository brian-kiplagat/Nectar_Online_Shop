package com.nectar.nectaronline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        Model_Images model = (Model_Images) objectList.get(position);

        String link = context.getString(R.string.website_adress) + "/nectar/seller/" + model.getPoster();
        //Glide.with(context).load(link).into(holder.imageView);
        Picasso.get().load(link).placeholder(R.drawable.alien).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}