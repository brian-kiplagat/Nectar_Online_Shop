package com.nectar.nectaronline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.text.NumberFormat;

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
    TextView name;
    TextView email;
    TextView address;
    TextView desc;
    Chip number;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.badge. ARG_ITEM_NUMBER
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
        name = v.findViewById(R.id.NAME);
        email = v.findViewById(R.id.EMAIL);
        address = v.findViewById(R.id.ADDRESS);
        desc = v.findViewById(R.id.DESC);
        number = v.findViewById(R.id.NUMBER);

        edit.setOnClickListener(this);
        finish.setOnClickListener(this);

        return v;
    }

    public void updateDeliveryDetails(String price) {
        Preferences preferences = new Preferences(context);
        name.setText(preferences.getName());
        email.setText(preferences.getEmail());
        address.setText(preferences.getAddress());
        desc.setText(preferences.getDescription());
        number.setText("+" + preferences.getNumber());
        String location = new Preferences(context).getAddress();
        int shippingFee = getShipping(location);
        if (shippingFee == 0) {
            shipping.setText("FREE");
        } else {
            shipping.setText(getString(R.string.cashUnit) + " " + String.valueOf(shippingFee));
        }
        int totalPrice = Integer.parseInt(price) + shippingFee;

        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true); // this will also round numbers, 3
        subtotal.setText(getString(R.string.cashUnit) + " " + myFormat.format(Integer.parseInt(price)));
        total.setText(getString(R.string.cashUnit) + " " + String.valueOf(myFormat.format(totalPrice)));
        SharedPreferences pref = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("total", String.valueOf(totalPrice));
        editor.apply();
    }

    private int getShipping(String location) {
        if (location.contentEquals("Nairobi")||location.contentEquals("Kiambu")) {
            return 0;
        } else {
            return 280;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("price")) {
            String price = intent.getStringExtra("price");
            Log.i("PRICE", price);
            updateDeliveryDetails(price);

        }
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
                intent.putExtra("payload", "edit");
                startActivity(intent);
                toPayment.onclick("price");
                break;
        }
    }

    public interface ToPayment {
        void onclick(String finalPrice);
    }


}