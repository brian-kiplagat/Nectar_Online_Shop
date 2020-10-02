package com.nectar.nectaronline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Delivery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Delivery extends Fragment implements View.OnClickListener {
    MaterialButton subtotal;
    MaterialButton total;
    MaterialButton shipping;
    Context context;
    MaterialButton finish;
    MaterialButton edit;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Delivery() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Delivery.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Delivery newInstance(String param1, String param2) {
        Fragment_Delivery fragment = new Fragment_Delivery();
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
        View v = inflater.inflate(R.layout.fragment_delivery, container, false);
        total = v.findViewById(R.id.total);
        subtotal = v.findViewById(R.id.subtotal);
        shipping = v.findViewById(R.id.shipping);
        finish = v.findViewById(R.id.finish);
        edit = v.findViewById(R.id.edit);
        edit.setOnClickListener(this);
        finish.setOnClickListener(this);
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("price")) {
            String price = intent.getStringExtra("price");
            Log.i("PRICE", price);
            updateDeliveryDetails(getString(R.string.cashUnit) + " " + price);

        }
        return v;
    }

    public void updateDeliveryDetails(String price) {
        subtotal.setText(price);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    ToPayment toPayment;

    public void setToPayment(ToPayment toPayment) {
        this.toPayment = toPayment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish:
                toPayment.onclick("price");
                break;
            case R.id.edit:
                Intent intent=new Intent(context,MainActivity.class);
                intent.putExtra("payload","edit");
                startActivity(intent);
                toPayment.onclick("price");
                break;
        }
    }

    public interface ToPayment {
        void onclick(String finalPrice);
    }
}