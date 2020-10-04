package com.nectar.nectaronline;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Favourites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Favourites extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private RecyclerView.Adapter adapter;
    Button button;
    List<Object> list;
    private List<Object> emptylist;
    ShimmerFrameLayout shimmerFrameLayout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Favourites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Favourites.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Favourites newInstance(String param1, String param2) {
        Fragment_Favourites fragment = new Fragment_Favourites();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment__favourites, container, false);
        context = getActivity().getApplicationContext();
        swipeRefreshLayout = v.findViewById(R.id.swipe);
        shimmerFrameLayout = v.findViewById(R.id.shimmer);
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView = v.findViewById(R.id.recycler_view);
        swipeRefreshLayout.setOnRefreshListener(this);
        return v;
    }

    @Override
    public void onRefresh() {
        fetch(new Preferences(context).getEmail());
    }

    private void fetch(String email) {

        swipeRefreshLayout.setRefreshing(true);
        shimmerFrameLayout.startShimmer();
        String url = getString(R.string.website_adress) + "/nectar/buy/getfavourites.php";//can get to cart and filer all the IDs from the product ID can send back all the data as a json volley
        RequestBody formBody = new FormBody.Builder()
                //then from server can check if to search or not the return an appropriate respons
                .add("email", email)//then from server can check if to search or not the return an appropriate respons
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
                    Log.i("FAVOURITES", res);
                    JSONObject obj = new JSONObject(res);
                    String code = obj.getString("RESPONSE_CODE");
                    String desc = obj.getString("RESPONSE_DESC");

                    if (code.contentEquals("SUCCESS")) {
                        String STUFF = obj.getString("SHOP_ITEMS");
                        //Log.i("CODE IS IN SUCESS: ","YEAH");
                        JSONObject object = new JSONObject(STUFF);
                        JSONArray array = object.getJSONArray("items");
                        list = new ArrayList<>();
                        list.clear();
                        //Log.i("ARRAY TO STRING: ", array.toString());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obje = array.getJSONObject(i);
                            final String id = obje.getString("id");
                            final String brand = obje.getString("brand");
                            final String name = obje.getString("name");
                            final String newPrice = obje.getString("new");
                            final String old = obje.getString("old");
                            final String description = obje.getString("description");
                            final String specification = obje.getString("specification");
                            final String keyfeatures = obje.getString("keyfeatures");
                            final String size = obje.getString("size");
                            final String color = obje.getString("color");
                            final String instock = obje.getString("instock");
                            final String weight = obje.getString("weight");
                            final String material = obje.getString("material");
                            final String inbox = obje.getString("inbox");
                            final String waranty = obje.getString("waranty");
                            final String state = obje.getString("state");
                            final String images = obje.getString("images");
                            final String sellerID = obje.getString("sellerID");
                            // final String quantity = obje.getString("quantity");
                            // final String delivery = obje.getString("delivery");

                            //Here within this for loop i could add the prices
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ModeL_Shop_Items model = new ModeL_Shop_Items(name, brand, id, newPrice, old, description, keyfeatures, specification, color, size, weight, material, inbox, waranty, instock, state, images, sellerID);
                                    list.add(model);
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    swipeRefreshLayout.setRefreshing(false);
                                    adapter = new Adapter_Favourites(list);
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);


                                }
                            });
                        }

                    } else if (desc.contentEquals("ZERO ITEMS")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Model_PlaceHolder model = new Model_PlaceHolder("");
                                emptylist = new ArrayList<>();
                                emptylist.add(model);
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);
                                //subtotal.setVisibility(View.GONE);
                                adapter = new Adapter_PlaceHolder(context, emptylist);
                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);


                            }
                        });
                    } else {
                        //subtotal.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (Exception e) {
                    Log.i("FATAL ERROR", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetch(new Preferences(context).getEmail());
    }

    public class Adapter_Favourites extends RecyclerView.Adapter<Adapter_Favourites.ViewHolder> {
        List<Object> list;

        public Adapter_Favourites(List<Object> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_favourites, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final ModeL_Shop_Items model = (ModeL_Shop_Items) list.get(position);
            String link = context.getString(R.string.website_adress) + "/nectar/seller/" + model.getImages();
            Glide.with(context).load(link).into(holder.prod_image);

            holder.brand.setText(model.getBrand());
            holder.name.setText(model.getName());
            holder.number_of_items.setText("Stock: " + model.getInstock());
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
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toast("Deleting");
                }
            });
            holder.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toast("Adding to cart");
                    addToCart(model.getId());
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView cash;

            TextView brand;
            TextView name;
            TextView size;
            Chip state;
            MaterialButton buy;
            MaterialButton delete;

            TextView price;
            TextView number_of_items;
            ImageView prod_image;
            ImageView fav;
            RelativeLayout cartStuff;
            TextView explanation;
            com.facebook.shimmer.ShimmerFrameLayout shimm;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                delete = itemView.findViewById(R.id.DELETE);
                buy = itemView.findViewById(R.id.BUY);
                cash = itemView.findViewById(R.id.CASH);
                fav = itemView.findViewById(R.id.favourite);
                cartStuff = itemView.findViewById(R.id.cartStuff);

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

    private void addToCart(String productID) {
        String url = getString(R.string.website_adress) + "/nectar/buy/addtocart.php";//can get to cart and filer all the IDs from the product ID can send back all the data as a json volley
        RequestBody formBody = new FormBody.Builder()
                //then from server can check if to search or not the return an appropriate respons
                .add("productID", productID)
                .add("email", new Preferences(context).getEmail())
                .add("phone", new Preferences(context).getNumber())
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
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Log.i("ADD TO CART", res);
                    try {

                        JSONObject obj = new JSONObject(res);
                        String code = obj.getString("RESPONSE_CODE");
                        String desc = obj.getString("RESPONSE_DESC");

                        if (code.contentEquals("SUCCESS")) {
                            toast("Added");
                        } else {
                            toast("Ops! Try again");
                        }
                    } catch (Exception e) {
                        Log.i("Error", e.getMessage());
                    }
                }
            }
        });

    }

    private void toast(String s) {
        Snackbar.make(recyclerView, s, Snackbar.LENGTH_SHORT).show();
    }
}