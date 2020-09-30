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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
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

    private void scaleImage(ImageView view) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
            //bitmap = Ion.with(view).getBitmap();
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(100);
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        Log.i("Test", "done");
    }

    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}
