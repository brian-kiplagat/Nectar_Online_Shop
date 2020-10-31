package com.nectar.nectaronline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Shop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Shop extends Fragment implements Adapter_Chips.ClickedListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private RecyclerView recyclerChips;
    private Context context;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adapter_chips;
    SwipeRefreshLayout swipeRefreshLayout;
    ShimmerFrameLayout shimmerFrameLayout;
    private List<Object> list;
    private List<Object> listChips = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.badge. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Shop() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Shop.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Shop newInstance(String param1, String param2) {
        Fragment_Shop fragment = new Fragment_Shop();
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
        context = getActivity().getApplicationContext();
        View v = inflater.inflate(R.layout.fragment__shop, container, false);
        swipeRefreshLayout = v.findViewById(R.id.swipe);
        shimmerFrameLayout = v.findViewById(R.id.shimmer);
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerChips = v.findViewById(R.id.recycler_view_horizontal);
        recyclerChips.setHasFixedSize(true);
        recyclerChips.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        fetchChips();
        final String queryWord = "";
        boolean search = false;
        fetch(search, false, queryWord);
        swipeRefreshLayout.setOnRefreshListener(this);
        return v;
    }


    private void fetchChips() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.clear();//clear both before each iteration
        listChips.clear();//clear before the itaeration so that doent fill up again
        arrayList.add("Latest in");
        arrayList.add("Liqour");
        arrayList.add("Fast food");
        arrayList.add("Energy drinks");
        arrayList.add("Sodas");
        arrayList.add("Fashion");
        arrayList.add("Electronics");
        for (int i = 0; i < arrayList.size(); i++) {
            Model_Chips chips = new Model_Chips(arrayList.get(i));
            listChips.add(chips);
            Log.i("VALUE", arrayList.get(i));
            adapter_chips = new Adapter_Chips(listChips, context, this);
            adapter_chips.notifyDataSetChanged();
            recyclerChips.setAdapter(adapter_chips);

        }
    }

    public void fetch(boolean isToolbar, boolean ischip, final String queryWord) {
        swipeRefreshLayout.setRefreshing(true);
        shimmerFrameLayout.startShimmer();
        // Log.i("fetch THIS: ",queryWord);
        final String QUERY;
        final String MAINQUERY;

        if (isToolbar == true) {
            //Make the code to search the database for matching keywords
            QUERY = "toolbar";
            Log.i("CHIP Search", QUERY);
            Log.i("Querrword", queryWord);


        } else if (ischip == true) {

            //Make the code to search the database for matching keywords
            QUERY = "chip";
            Log.i("Querrword", queryWord);
        } else {
            //Make the code to pull everything in database indiscriminately
            QUERY = "normal";
            Log.i("Search", QUERY);
        }

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String url = getString(R.string.website_adress) + "/nectar/buy/getitems.php";
                    RequestBody formBody = new FormBody.Builder()
                            .add("search", QUERY)//then from server can check if to search or not the return an appropriate respons
                            .add("query", queryWord)
                            .build();

                    OkHttpClient client = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String res = response.body().string().trim();
                    Log.i("SERVER RESPONSE", res);
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

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ModeL_Shop_Items model = new ModeL_Shop_Items(name, brand, id, newPrice, old, description, keyfeatures, specification, color, size, weight, material, inbox, waranty, instock, state, images, sellerID);
                                    list.add(model);
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    adapter = new Adapter_Shop(context, list);
                                    adapter.notifyDataSetChanged();
                                    swipeRefreshLayout.setRefreshing(false);
                                    recyclerView.setAdapter(adapter);

                                }
                            });
                        }


                    } else if (desc.contentEquals("ERROR: ZERO ITEMS FROM NAME SEARCH")) {
                        Snackbar.make(recyclerView, "Ops! We could'nt find that", Snackbar.LENGTH_SHORT).show();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    } else if (desc.contentEquals("ERROR: ZERO ITEMS FROM CHIP SEARCH")) {
                        Snackbar.make(recyclerView, "Ops! We could'nt find that", Snackbar.LENGTH_SHORT).show();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }

                    else {
                        Log.i("DESC ERROR: ", desc);
                    }
                } catch (Exception e) {
                    Log.i("EXCEPTION ERROR: ", e.getLocalizedMessage());
                    Snackbar.make(recyclerView, "Ops ! Network connection seem to be poor, trying to connect again", Snackbar.LENGTH_LONG).show();

                }

            }
        });
        thread.start();

    }


    @Override
    public void onRefresh() {
        fetch(false, false, "");
    }

    @Override
    public void onClicked(String filter) {
        // Toast.makeText(context, filter, Toast.LENGTH_SHORT).show();
        fetch(false, true, filter);
        Log.i("SEARCHING", filter);
    }





    private void toast(String s){
        Snackbar.make(recyclerView,s,Snackbar.LENGTH_SHORT).show();
    }
}