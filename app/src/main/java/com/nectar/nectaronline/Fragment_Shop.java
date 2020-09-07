package com.nectar.nectaronline;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Shop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Shop extends Fragment implements Adapter_Shop.InterfaceListener, Adapter_Chips.ChipInterfaceListener {
    private RecyclerView recyclerView;
    private RecyclerView recyclerChips;
    private Context context;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adapter_chips;

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
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerChips = v.findViewById(R.id.recycler_view_horizontal);
        recyclerChips.setHasFixedSize(true);
        recyclerChips.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        fetchChips();
        String queryWord = "";
        boolean search = false;
        fetch(search, queryWord);
        return v;
    }

    private void fetchChips() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Trousers");
        arrayList.add("Shirts");
        arrayList.add("Shoes");
        arrayList.add("Sneakers");
        arrayList.add("Liqour");
        arrayList.add("Vodka");
        for (int i = 0; i < arrayList.size(); i++) {
            Model_Chips chips = new Model_Chips(arrayList.get(i));
            listChips.add(chips);
            Log.i("VALUE", arrayList.get(i));
            adapter_chips=new Adapter_Chips(listChips,context,this);
            adapter_chips.notifyDataSetChanged();
            recyclerChips.setAdapter(adapter_chips);

        }
    }

    public void fetch(boolean search, String queryWord) {
        if (search == true) {
            //Make the code to search the database for matching keywords
            String searchEndpoint = "https://cashmobil.co.ke/nectar/search.php";
        } else {
            //Make the code to pull everything in database indiscriminately
            String generalEndpoint = "https://cashmobil.co.ke/nectar/search.php";

        }
        if (queryWord.isEmpty()) {//get every thing in the shop
            Log.i("fetch", "Empty");
        } else if (queryWord.contains("search")) {//get the query word and use it to search against database
            Log.i("fetch", queryWord);
        }
        //To reduce time,,,,,
        // can get items list on app login
        // then save all the items locally in shared prefrences
        // so that no network requests is made only via loaing icon items
        // locally
        list.clear();//Clear te list from previous oncreates and ondestroys
        //Id need to use a key to acces jon array
        //use a for loop to the Json array elements
        //Store the elements in strings
        //Fill up the model with the elements
        //add the list with the model in each iteration
        //Initialise the adapter with context.list etc.
        //ModeL_Shop_Items model=new ModeL_Shop_Items("","",)
        adapter = new Adapter_Shop(context, list, this);

    }


    @Override
    public void communicateBack(String string) {
        Log.i("communicateBack: ", string);
    }


    @Override
    public void onClicked(String string) {
    fetch(true,string);
    }
}