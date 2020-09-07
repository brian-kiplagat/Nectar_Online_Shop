package com.nectar.nectaronline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_Shop extends RecyclerView.Adapter<Adapter_Shop.ViewHolder> {
    Context context;
    List<Object> list;
    InterfaceListener interfaceListener;

    public Adapter_Shop(Context context, List<Object> list, InterfaceListener interfaceListener) {
        this.context = context;
        this.list = list;
        this.interfaceListener = interfaceListener;
    }

    public interface InterfaceListener {
        void communicateBack(String string);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_shop_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        interfaceListener.communicateBack("");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
