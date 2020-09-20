package com.nectar.nectaronline;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class Fragment_Cart extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView recyclerChips;
    private Context context;
    private RecyclerView.Adapter adapter;
    private List<Object> list = new ArrayList<>();
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
        swipeRefreshLayout = v.findViewById(R.id.swipe);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        final String number = preferences.getString("number", "");
        Log.i("NUMBER", number);
        fetch(number);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch(number);
            }
        });
        return v;

    }

    private void fetch(final String number) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = getString(R.string.website_adress) + "/nectar/getcart.php";
                    RequestBody formBody = new FormBody.Builder()
                            .add("phone", number)//then from server can check if to search or not the return an appropriate respons
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
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Model_Cart_Id model = (Model_Cart_Id) list_cart_items.get(position);
            final String ID = model.getProduct_id();
            //
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = getString(R.string.website_adress) + "/nectar/getaparticularproduct.php";
                        RequestBody formBody = new FormBody.Builder()
                                .add("id", ID)//then from server can check if to search or not the return an appropriate respons
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
                            //Log.i("ARRAY TO STRING: ", array.toString());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obje = array.getJSONObject(i);
                                final String PRODUCT_ID = obje.getString("productid");
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

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

    }

}