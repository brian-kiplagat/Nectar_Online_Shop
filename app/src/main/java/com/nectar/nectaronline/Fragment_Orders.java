package com.nectar.nectaronline;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

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
 * Use the {@link Fragment_Orders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Orders extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
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

    public Fragment_Orders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Orders.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Orders newInstance(String param1, String param2) {
        Fragment_Orders fragment = new Fragment_Orders();
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
        View v = inflater.inflate(R.layout.fragment__orders, container, false);
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
        final List<Integer> totalPriceList = new ArrayList<>();
        swipeRefreshLayout.setRefreshing(true);
        shimmerFrameLayout.startShimmer();
        String url = getString(R.string.website_adress) + "/nectar/buy/getorder.php";//can get to cart and filer all the IDs from the product ID can send back all the data as a json volley
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
                    //Log.i("ORDERS", res);
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
                            final String delivery = obje.getString("delivery");

                            //Here within this for loop i could add the prices
                            int subTotal = Integer.parseInt(newPrice) * Integer.parseInt(quantity);
                            totalPriceList.add(subTotal);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Model_Orders model = new Model_Orders(name, brand, id, newPrice, old, description, keyfeatures, specification, color, size, weight, material, inbox, waranty, instock, state, images, sellerID, productID, quantity,delivery);
                                    list.add(model);
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    swipeRefreshLayout.setRefreshing(false);
                                    adapter = new Adapter_Orders(context,list);
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);


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
                                //totalCash.setText(String.valueOf(finalTotal));
                               // PRICE = String.valueOf(String.valueOf(finalTotal));
                               // Log.i("TOTAL NOW", PRICE);
                            }
                        });
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

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetch(new Preferences(context).getEmail());
    }
}