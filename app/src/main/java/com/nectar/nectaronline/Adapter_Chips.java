package com.nectar.nectaronline;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

public class Adapter_Chips extends RecyclerView.Adapter<Adapter_Chips.ViewHolder> {
    List<Object> list;
    Context context;
    private ClickedListener clickedListener;
    public interface ClickedListener{
        void onClicked(String filter);
    }

    public Adapter_Chips(List<Object> list, Context context, ClickedListener listener) {
        this.list = list;
        this.context = context;
        this.clickedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chips, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_Chips model = (Model_Chips) list.get(position);
        holder.chip.setText(model.getText());
        holder.chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, model.getText(), Toast.LENGTH_SHORT).show();
                clickedListener.onClicked(model.getText());
            }
        });
        boolean pressed = false;
        if (pressed){
            holder.chip.setCheckable(true);
            pressed=true;

        }else{
            holder.chip.setCheckable(false);
            pressed=false;

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Chip chip;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip);
        }
    }


}
