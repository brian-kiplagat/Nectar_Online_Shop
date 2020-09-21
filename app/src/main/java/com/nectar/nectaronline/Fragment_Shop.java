package com.nectar.nectaronline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

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
 * Use the {@link Fragment_Shop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Shop extends Fragment implements Adapter_Chips.ChipInterfaceListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private RecyclerView recyclerChips;
    private Context context;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adapter_chips;
    SwipeRefreshLayout swipeRefreshLayout;
    private List<Object> list = new ArrayList<>();
    private List<Object> listChips = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerChips = v.findViewById(R.id.recycler_view_horizontal);
        recyclerChips.setHasFixedSize(true);
        recyclerChips.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        fetchChips();
        final String queryWord = "";
        boolean search = false;
        fetch(search, queryWord);
        swipeRefreshLayout.setOnRefreshListener(this);
        return v;
    }

    private void fetchChips() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.clear();//clear both before each iteration
        listChips.clear();//clear before the itaeration so that doent fill up again
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

    public void fetch(boolean search, final String queryWord) {
        swipeRefreshLayout.setRefreshing(true);
        Log.i(queryWord, "fetch: ");
        final String QUERY;
        if (search == true) {
            //Make the code to search the database for matching keywords
            QUERY = "true";

        } else {
            //Make the code to pull everything in database indiscriminately
            QUERY = "false";

        }
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = getString(R.string.website_adress) + "/nectar/getitems.php";
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
                    final String res = response.body().string().trim();
                    Log.i("response", res);
                    JSONObject obj = new JSONObject(res);
                    String code = obj.getString("RESPONSE_CODE");
                    if (code.contentEquals("SUCCESS")) {
                        String STUFF = obj.getString("SHOP_ITEMS");
                        //Log.i("SHOP ITEMS: ", STUFF);
                        JSONObject object = new JSONObject(STUFF);
                        JSONArray array = object.getJSONArray("items");
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
                            Log.i("BRAND", brand);
                            Log.i("NAME", name);
                            Log.i("NEW_PRICE", newPrice);
                            Log.i("OLD_PRICE", old);
                            Log.i("DESC", description);
                            Log.i("SPEC", specification);
                            Log.i("KEY", keyfeatures);
                            Log.i("SIZE", size);
                            Log.i("COLOR", color);
                            Log.i("INSTOCK", instock);
                            Log.i("WEIGHT", weight);
                            Log.i("MATERIAL", material);
                            Log.i("INBOX", inbox);
                            Log.i("BRAND", brand);
                            Log.i("WARRANTY", waranty);
                            Log.i("STATE", state);
                            Log.i("IMAGES", images);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ModeL_Shop_Items model = new ModeL_Shop_Items(name, brand, id, newPrice, old, description, keyfeatures, specification, color, size, weight, material, inbox, waranty, instock, state, images);
                                    list.add(model);
                                    adapter = new Adapter_Shop(context, list);
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);

                                }
                            });
                        }


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
        swipeRefreshLayout.setRefreshing(false);


    }


    @Override
    public void onClicked(String string) {
        fetch(true, string);
    }

    @Override
    public void onRefresh() {
        fetch(false, "");
    }

    public class Adapter_Shop extends RecyclerView.Adapter<Adapter_Shop.ViewHolder> {
        Context context;
        List<Object> list;

        public Adapter_Shop(Context context, List<Object> list) {
            this.context = context;
            this.list = list;
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
            String link = getString(R.string.website_adress) + "/nectar/" + model.getImages();
            Glide.with(context).load(link).into(holder.image);
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
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView brand;
            TextView price;
            ImageView image;
            MaterialCardView cardView;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                brand = itemView.findViewById(R.id.brand);
                price = itemView.findViewById(R.id.price);
                image = itemView.findViewById(R.id.image);
                cardView = itemView.findViewById(R.id.cardView);
            }
        }
    }
}