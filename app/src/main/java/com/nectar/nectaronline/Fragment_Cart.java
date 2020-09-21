package com.nectar.nectaronline;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
public class Fragment_Cart extends Fragment implements SwipeRefreshLayout.OnRefreshListener , View.OnClickListener {
    private RecyclerView recyclerView;
    private Context context;
    private RecyclerView.Adapter adapter;
    String number;
    String email;
    Button button;
    int count = 1;

    private List<Object> list;
    SwipeRefreshLayout swipeRefreshLayout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        button=v.findViewById(R.id.button);
        swipeRefreshLayout = v.findViewById(R.id.swipe);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        number = preferences.getString("number", "");
        email = preferences.getString("email", "");

        Log.i("NUMBER", number);
        fetch(number);
        swipeRefreshLayout.setOnRefreshListener(this);

        return v;

    }

    private void fetch(final String number) {
        swipeRefreshLayout.setRefreshing(true);
        list = new ArrayList<>();
        list.clear();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = getString(R.string.website_adress) + "/nectar/getcart.php";
                    RequestBody formBody = new FormBody.Builder()
                            .add("phone", number)//then from server can check if to search or not the return an appropriate respons
                            .add("email", email)//then from server can check if to search or not the return an appropriate respons
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

                        //Log.i("ARRAY TO STRING: ", array.toString());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obje = array.getJSONObject(i);
                            final String PRODUCT_ID = obje.getString("productid");
                            Log.i("id", PRODUCT_ID);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Model_Cart_Id model = new Model_Cart_Id(PRODUCT_ID);
                                    list.add(model);
                                    adapter = new Adapter_Cart(list);
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);


                                }
                            });
                        }


                    } else if (desc.contentEquals("ZERO ITEMS")) {
                        toast("Add items to your cart");
                        Log.i("ERROR: ", desc);
                    }
                } catch (Exception e) {
                    Log.i("ERROR: ", e.getLocalizedMessage());
                }

            }
        });
        thread.start();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        fetch(number);
    }

    @Override
    public void onClick(View v) {

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
            final Model_Cart_Id model = (Model_Cart_Id) list_cart_items.get(position);
            final String ID = model.getProduct_id();
            //
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        String url = getString(R.string.website_adress) + "/nectar/getaparticularproduct.php";
                        RequestBody formBody = new FormBody.Builder()
                                .add("id", ID)
                                //then from server can check if to search or not the return an appropriate respons
                                .build();

                        OkHttpClient client = new OkHttpClient();
                        final Request request = new Request.Builder()
                                .url(url)
                                .post(formBody)
                                .build();

                        Response response = client.newCall(request).execute();
                        final String res = response.body().string().trim();
                        Log.i("response", res);
                        JSONObject obj = new JSONObject(res);
                        String code = obj.getString("RESPONSE_CODE");
                        if (code.contentEquals("SUCCESS")) {
                            JSONArray array = obj.getJSONArray("DETAILS");
                            //Log.i("ARRAY TO STRING: ", array.toString());
                            JSONObject obje = array.getJSONObject(0);
                            //final String PRODUCT_ID = obje.getString("productid");
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

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.brand.setText(brand);
                                    holder.name.setText(name);
                                    holder.price.setText(newPrice);
                                    holder.number_of_items.setText("1");
                                    holder.size.setText(size);
                                    holder.instock.setText(instock);
                                    String link = getString(R.string.website_adress) + "/nectar/" + images;
                                    Glide.with(context).load(link).into(holder.prod_image);

                                    if (state.contentEquals("BRAND")) {
                                        holder.state.setText(getString(R.string.BRAND_NEW));

                                    }
                                    if (state.contentEquals("REFURBISHED")) {
                                        holder.state.setText(getString(R.string.REFURBISHED));

                                    }
                                    if (state.contentEquals("SECOND")) {
                                        holder.state.setText(getString(R.string.SECONDHAND));

                                    }
                                    holder.increase.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (count >= Integer.parseInt(instock)) {
                                                holder.reduce.setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN);
                                                toast("Ops! Only " + instock + " are available");

                                            } else {
                                                count++;
                                                holder.number_of_items.setText(String.valueOf(count));

                                            }

                                        }
                                    });
                                    holder.reduce.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (count <= 1) {
                                                holder.reduce.setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN);
                                                toast("Ops!");

                                            } else {
                                                count--;
                                                holder.number_of_items.setText(String.valueOf(count));

                                            }

                                        }
                                    });
                                }
                            });


                        } else {
                            String desc = obj.getString("RESPONSE_DESC");
                            Log.i("ERROR: ", desc);
                        }
                    } catch (Exception e) {
                        Log.i("ERROR: ", e.getLocalizedMessage());
                    }

                }
            });
            thread.start();
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toast("Removing");
                    removeItem(ID);
                    list.remove(position);
                    adapter.notifyDataSetChanged();

                }
            });


        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView instock;
            TextView brand;
            TextView name;
            TextView size;
            Chip state;
            TextView price;
            TextView number_of_items;
            ImageView increase;
            ImageView reduce;
            ImageView delete;
            ImageView prod_image;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
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


            }
        }

    }

    private void removeItem(final String id) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //remove from cart where id=id and email=user email...........
                    String url = getString(R.string.website_adress) + "/nectar/removefromcart.php";
                    RequestBody formBody = new FormBody.Builder()
                            .add("id", id)
                            .add("email", email)
                            .build();

                    OkHttpClient client = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    final String res = response.body().string().trim();
                    Log.i("response", res);
                    JSONObject obj = new JSONObject(res);
                    String code = obj.getString("RESPONSE_CODE");
                    if (code.contentEquals("SUCCESS")) {
                        toast("Removed");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fetch(number);
                            }
                        });

                    } else {
                        toast("Ops. An error happened");
                        String desc = obj.getString("RESPONSE_DESC");
                        Log.i("ERROR: ", desc);
                    }
                } catch (Exception e) {
                    Log.i("ERROR: ", e.getLocalizedMessage());
                }

            }
        });
        thread.start();
    }

    private void toast(String s) {
        Snackbar.make(recyclerView, s, Snackbar.LENGTH_SHORT).show();
    }


}