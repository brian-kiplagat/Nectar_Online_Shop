package com.nectar.nectaronline;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Payment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Payment extends Fragment implements View.OnClickListener {
    MaterialCheckBox popup;
    MaterialCheckBox till;
    MaterialCheckBox payondelivery;
    Chip chip_till;
    Chip chip_pod;
    MaterialButton finish;
    Context context;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Payment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Payment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Payment newInstance(String param1, String param2) {
        Fragment_Payment fragment = new Fragment_Payment();
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
        View v = inflater.inflate(R.layout.fragment_payment, container, false);
        context = getActivity().getApplicationContext();
        updateDetails("");
        popup = v.findViewById(R.id.popup);
        till = v.findViewById(R.id.till);
        payondelivery = v.findViewById(R.id.payondelivery);
        chip_pod = v.findViewById(R.id.till_copy);
        chip_till = v.findViewById(R.id.till_copy_mpesa_on_delivery);
        finish = v.findViewById(R.id.finish);
        finish.setOnClickListener(this);
        chip_till.setOnClickListener(this);
        chip_pod.setOnClickListener(this);
        return v;

    }

    private void updateDetails(String price) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish:

                break;
            case R.id.till_copy:
            case R.id.till_copy_mpesa_on_delivery:
                copyTill();
                break;


        }
    }

    private void copyTill() {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("saved to clip", getString(R.string.kopokopTill_for_messaging_payment));
        clipboard.setPrimaryClip(clip);
        toast("Till copied to clipboard");
    }

    private void toast(String s) {
        Snackbar.make(finish, s, Snackbar.LENGTH_SHORT).show();
    }
}