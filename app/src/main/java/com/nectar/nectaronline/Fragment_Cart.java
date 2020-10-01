package com.nectar.nectaronline;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
 * Use the {@link Fragment_Cart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Cart extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private Context context;
    private RecyclerView.Adapter adapter;
    String number;
    String email;
    Button button;
    ShimmerFrameLayout shimmerFrameLayout;
    int count = 1;
    MaterialCardView subtotal;
    TextView totalCash;
    String PRICE;

    private List<Object> list;
    SwipeRefreshLayout swipeRefreshLayout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int milliseconds;

    public Fragment_Cart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Cart.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Cart newInstance(String param1, String param2) {
        Fragment_Cart fragment = new Fragment_Cart();
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
        View v = inflater.inflate(R.layout.fragment__cart, container, false);
        context = getActivity().getApplicationContext();
        recyclerView = v.findViewById(R.id.recycler_view);
        button = v.findViewById(R.id.button);
        swipeRefreshLayout = v.findViewById(R.id.swipe);
        shimmerFrameLayout = v.findViewById(R.id.shimmer);
        totalCash = v.findViewById(R.id.PRICE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        number = preferences.getString("number", "");
        email = preferences.getString("email", "");
        button.setOnClickListener(this);
        subtotal = v.findViewById(R.id.subtotal);
        subtotal.setVisibility(View.GONE);
        Log.i("NUMBER", number);
        //fetch(number);
        swipeRefreshLayout.setOnRefreshListener(this);

        return v;

    }

    public void fetch(final String email) {
        //deletedListener.onDelete();
        swipeRefreshLayout.setRefreshing(true);
        shimmerFrameLayout.startShimmer();
        final List<Integer> totalPriceList = new ArrayList<>();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {//AM HERE
                    Preferences preferences = new Preferences(context);
                    String url = getString(R.string.website_adress) + "/nectar/buy/getclientcart.php";//can get to cart and filer all the IDs from the product ID can send back all the data as a json volley
                    RequestBody formBody = new FormBody.Builder()
                            //then from server can check if to search or not the return an appropriate respons
                            .add("email", preferences.getEmail())//then from server can check if to search or not the return an appropriate respons
                            .build();

                    OkHttpClient client = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    final String res = response.body().string().trim();
                    Log.i("Cart response", res);
                    JSONObject obj = new JSONObject(res);
                    String code = obj.getString("RESPONSE_CODE");
                    String desc = obj.getString("RESPONSE_DESC");

                    if (code.contentEquals("SUCCESS")) {
                        String STUFF = obj.getString("SHOP_ITEMS");
                        //Log.i("SHOP ITEMS: ", STUFF);
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
                            final String productID = obje.getString("productID");
                            final String quantity = obje.getString("quantity");

                            //Here within this for loop i could add the prices
                            int subTotal = Integer.parseInt(newPrice) * Integer.parseInt(quantity);
                            totalPriceList.add(subTotal);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ModeL_Cart_Items model = new ModeL_Cart_Items(name, brand, id, newPrice, old, description, keyfeatures, specification, color, size, weight, material, inbox, waranty, instock, state, images, sellerID, productID, quantity);
                                    list.add(model);
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    swipeRefreshLayout.setRefreshing(false);
                                    adapter = new Adapter_Cart(list);
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);
                                    subtotal.setVisibility(View.VISIBLE);

                                }
                            });
                        }
                        int total = 0;
                        for (int i = 0; i < totalPriceList.size(); i++) {
                            int newTotal = totalPriceList.get(i) + total;
                            Log.i("Total at this instance", String.valueOf(newTotal));
                            total = newTotal;

                        }
                        Log.i("Final instance", String.valueOf(total));
                        final int finalTotal = total;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                totalCash.setText(String.valueOf(finalTotal));
                                PRICE = String.valueOf(String.valueOf(finalTotal));
                                Log.i("TOTAL NOW", PRICE);
                            }
                        });
                    } else if (desc.contentEquals("ZERO ITEMS")&&Fragment_Cart.this.isResumed()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
                                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                                View dialogView = layoutInflater.inflate(R.layout.dialog_empty_cart, null);
                                MaterialButton button = dialogView.findViewById(R.id.continueAdding);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        subtotal.setVisibility(View.GONE);
                                        dialogBuilder.dismiss();
                                        getShopFragment.moveToTheShopFragment();
                                    }
                                });
                                dialogBuilder.setView(dialogView);
                                dialogBuilder.setCancelable(false);
                                dialogBuilder.show();
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);

                            }
                        });
                    } else {
                        subtotal.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (Exception e) {
                    Log.i("ERROR EXC: ", e.getLocalizedMessage());
                }

            }
        });
        thread.start();

    }

    @Override
    public void onRefresh() {
        fetch(number);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            Log.i("FINISH", "onClick: ");
            //Use a for loop to circle through the cart legnth
        }
    }

    public class Adapter_Cart extends RecyclerView.Adapter<Adapter_Cart.ViewHolder> {
        List<Object> list_cart_items;

        public Adapter_Cart(List<Object> list_cart_items) {
            this.list_cart_items = list_cart_items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart_items, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public int getItemCount() {
            return list_cart_items.size();
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            final ModeL_Cart_Items model = (ModeL_Cart_Items) list_cart_items.get(position);
            holder.number_of_items.setText(model.getQuantity());
            final int[] priceCount = {Integer.parseInt(holder.number_of_items.getText().toString().trim())};
            holder.shimm.stopShimmer();
            holder.shimm.setVisibility(View.GONE);
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
            final String productID = model.getProductID();
            final String cartID = model.getId();
            String link = getString(R.string.website_adress) + "/nectar/seller/" + images;
            Glide.with(context).load(link).into(holder.prod_image);
            holder.cash.setText("Ksh " + model.getFinalPrice());
            holder.brand.setText(model.getBrand());
            holder.name.setText(model.getName());
            holder.instock.setText("Stock: " + model.getInstock());
            holder.state.setText(model.getState());
            holder.brand.setText(model.getBrand());
            holder.price.setText(String.valueOf(priceCount[0] * Integer.parseInt(model.getFinalPrice())));
            holder.number_of_items.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String count = holder.number_of_items.getText().toString().trim();
                    if (!count.isEmpty()) {
                        if (!(Integer.parseInt(count) >= Integer.parseInt(model.getInstock()))) {
                            priceCount[0] = Integer.parseInt(count);
                            holder.price.setText(String.valueOf(priceCount[0] * Integer.parseInt(model.getFinalPrice())));
                            //update the quantity here
                            updateQuantity(id, email, priceCount[0]);
                        } else {
                            vibrate();
                            toast("Ops, only " + model.getInstock() + " are in stock");

                        }
                    } else {
                        toast("Ops, a field is empty");
                        vibrate();
                    }


                }
            });

            if (model.getState().contentEquals("BRAND")) {
                holder.state.setText("BRAND NEW");
                holder.state.setChipIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_brand_new, null));
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    holder.state.setChipIconTint(getResources().getColorStateList(R.color.yellow, null));
                }
            }
            if (model.getState().contentEquals("REFURBISHED")) {
                holder.state.setText("REFURBISHED");
                holder.state.setChipIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_refurb, null));
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    holder.state.setChipIconTint(getResources().getColorStateList(R.color.green, null));
                }
            }
            if (model.getState().contentEquals("SECOND")) {
                holder.state.setText("SECOND HAND");
                holder.state.setChipIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_second_hand, null));
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    holder.state.setChipIconTint(getResources().getColorStateList(R.color.orange, null));
                }
            }
            holder.size.setText("Size: " + model.getSize());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String longString = brand + " " + name + " " + description + " " + specification + " " + keyfeatures + " " + weight + " " + material;
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                    materialAlertDialogBuilder.setTitle("Remove from cart").setMessage("Are you sure you want to remove " + longString).setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toast("Removing");
                            removeItem(id, position);

                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setCancelable(true).show();


                }
            });
            holder.increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String count = holder.number_of_items.getText().toString().trim();
                    if (!count.isEmpty()) {//confirm its not empty
                        if (!(Integer.parseInt(count) >= Integer.parseInt(model.getInstock()))) {
                            if (priceCount[0] >= 1) {//only increment when the value is greater or equal to one
                                int newPrice = priceCount[0] + 1;
                                //HERE
                                priceCount[0] = newPrice;
                                Log.i(String.valueOf(priceCount[0]), "onClick: ");
                                holder.number_of_items.setText(String.valueOf(priceCount[0]));
                            } else {
                                toast("Ops, you cannot order less than one");
                                vibrate();
                            }
                        } else {
                            toast("Ops, only " + model.getInstock() + " are in stock");
                            vibrate();
                        }


                    } else {
                        Log.i(String.valueOf(priceCount[0]), "onClick: ");
                        toast("Ops, a field is empty");
                        vibrate();
                    }
                }
            });
            holder.reduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String count = holder.number_of_items.getText().toString().trim();
                    if (!count.isEmpty()) {
                        if (priceCount[0] > 1) {//only decrement when value is greater than 1
                            int newPrice = priceCount[0] - 1;
                            //HERE
                            priceCount[0] = newPrice;
                            Log.i(String.valueOf(priceCount[0]), "onClick: ");
                            holder.number_of_items.setText(String.valueOf(priceCount[0]));
                        } else {
                            Log.i(String.valueOf(priceCount[0]), "onClick: ");
                            toast("Ops, you cannot order less than one");
                            vibrate();
                        }

                    } else {
                        toast("Ops, a field is empty");
                        vibrate();
                    }
                }
            });
            holder.fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                    materialAlertDialogBuilder.setTitle("Save for later").setMessage("We will remove " + name + " from yor cart and transfer to your favourites").setPositiveButton("HELL YEAH", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toast("Removing");
                            removeItemAndAddToFavourites(id, productID, position);

                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setCancelable(true).show();
                }
            });


        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView cash;
            TextView instock;
            TextView brand;
            TextView name;
            TextView size;
            Chip state;
            TextView price;
            EditText number_of_items;
            ImageView increase;
            ImageView reduce;
            ImageView delete;
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
                instock = itemView.findViewById(R.id.INSTOCK);
                brand = itemView.findViewById(R.id.BRAND);
                name = itemView.findViewById(R.id.NAME);
                size = itemView.findViewById(R.id.SIZE);
                state = itemView.findViewById(R.id.STATE);
                price = itemView.findViewById(R.id.PRICE);
                number_of_items = itemView.findViewById(R.id.center);
                increase = itemView.findViewById(R.id.increase);
                reduce = itemView.findViewById(R.id.reduce);
                delete = itemView.findViewById(R.id.delete);
                prod_image = itemView.findViewById(R.id.drinkimage);
                shimm = itemView.findViewById(R.id.shimmer);
                explanation = itemView.findViewById(R.id.Explanation);
            }
        }

    }

    private void updateQuantity(String id, String email, int quantity) {
        String url = getString(R.string.website_adress) + "/nectar/buy/editquantity.php";
        RequestBody formBody = new FormBody.Builder()//Try to UPDATE `quantity` WHERE `email`=$email AND `id`=$id
                .add("id", id)
                .add("email", email)
                .add("quantity", String.valueOf(quantity))

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
                final String res = response.body().string().trim();
                Log.i("EDIT RESPONSE", res);

                try {
                    JSONObject obj = new JSONObject(res);
                    String code = obj.getString("RESPONSE_CODE");
                    if (code.contentEquals("SUCCESS")) {
                        toast("Edited");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                deletedListener.onDelete();
                                Log.i("Removed to fav: ", "Yeah");
                            }
                        });

                    } else {
                        toast("Ops. An error happened, try again");
                        String desc = obj.getString("RESPONSE_DESC");
                        Log.i("ERROR: ", desc);
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void vibrate() {
        milliseconds = 300;
        final Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));

        } else {
            vib.vibrate(milliseconds);
        }
    }

    private void removeItemAndAddToFavourites(String id, String productID, final int position) {
        String url = getString(R.string.website_adress) + "/nectar/buy/removefromcartandaddtofavourites.php";
        RequestBody formBody = new FormBody.Builder()
                .add("id", id)
                .add("email", email)
                .add("productID", productID)
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
                    Log.i("response", res);
                    JSONObject obj = new JSONObject(res);
                    String code = obj.getString("RESPONSE_CODE");
                    if (code.contentEquals("SUCCESS")) {
                        toast("Removed");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position, list.size());

                                deletedListener.onDelete();
                                Log.i("Removed: ", "Yeah");
                            }
                        });

                    } else {
                        toast("Ops. An error happened");
                        String desc = obj.getString("RESPONSE_DESC");
                        Log.i("ERROR: ", desc);
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void removeItem(final String id, final int position) {
        String url = getString(R.string.website_adress) + "/nectar/buy/removefromcart.php";
        RequestBody formBody = new FormBody.Builder()
                .add("id", id)
                .add("email", email)
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
                    Log.i("response", res);
                    JSONObject obj = new JSONObject(res);
                    String code = obj.getString("RESPONSE_CODE");
                    if (code.contentEquals("SUCCESS")) {
                        toast("Removed");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //fetch(number);
                                list.remove(position);
                                //recyclerView.removeViewAt(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position, list.size());
                                //adapter.notifyDataSetChanged();
                                deletedListener.onDelete();
                                Log.i("Removed: ", "Yeah");
                            }
                        });

                    } else {
                        toast("Ops. An error happened");
                        String desc = obj.getString("RESPONSE_DESC");
                        Log.i("ERROR: ", desc);
                    }
                } catch (Exception e) {

                }
            }
        });

    }

    private void toast(String s) {
        Snackbar.make(recyclerView, s, Snackbar.LENGTH_SHORT).show();
    }

    DeletedListener deletedListener;

    public void setDeletedListener(DeletedListener deletedListener) {
        this.deletedListener = deletedListener;
    }

    public interface DeletedListener {
        void onDelete();
    }

    GetShopFragment getShopFragment;

    public interface GetShopFragment {
        void moveToTheShopFragment();
    }

    public void setGetShopFragment(GetShopFragment getShopFragment) {
        this.getShopFragment = getShopFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetch(new Preferences(context).getEmail());
    }

}