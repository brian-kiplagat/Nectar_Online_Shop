package com.nectar.nectaronline;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Payment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Payment extends Fragment implements View.OnClickListener {
    MaterialRadioButton popup;
    MaterialRadioButton till;
    MaterialRadioButton payondelivery;
    Chip chip_till;
    Chip chip_pod;
    Button finish;
    Context context;
    String price;
    MaterialButton totalPrice;
    TextView pay;
    TextView paybill;
    TextView accno;
    TextView pay1;
    TextView paybill1;
    TextView accno1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.badge. ARG_ITEM_NUMBER
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
        popup = v.findViewById(R.id.popup);
        till = v.findViewById(R.id.till);
        totalPrice = v.findViewById(R.id.total);
        payondelivery = v.findViewById(R.id.payondelivery);
        chip_pod = v.findViewById(R.id.till_copy);
        chip_till = v.findViewById(R.id.till_copy_mpesa_on_delivery);
        pay = v.findViewById(R.id.pay);
        paybill = v.findViewById(R.id.paybillNO);
        accno = v.findViewById(R.id.accountNO);
        pay1 = v.findViewById(R.id.pay1);
        paybill1 = v.findViewById(R.id.paybillNO1);
        accno1 = v.findViewById(R.id.accountNO1);
        finish = v.findViewById(R.id.finish);
        finish.setOnClickListener(this);
        chip_till.setOnClickListener(this);
        chip_pod.setOnClickListener(this);


        return v;

    }

    public void updateDetails() {
        pay.setText("Pay exactly " + context.getString(R.string.cashUnit) + " " + price);
        paybill.setText("Paybill: " + context.getString(R.string.paybill_number));
        accno.setText("Account number: +" + new Preferences(context).getNumber());
        pay1.setText("Pay exactly " + context.getString(R.string.cashUnit) + " " + price);
        paybill1.setText("Paybill: " + context.getString(R.string.paybill_number));
        accno1.setText("Account number: +" + new Preferences(context).getNumber());
        SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        if (preferences.contains("total")) {
            price = preferences.getString("total", "");
            totalPrice.setText(getString(R.string.cashUnit) + " " + price);
        } else {
            toast("Ops, an error happened, please cancel this page and try again");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish:
                if (popup.isChecked()) {
                    payNow(price);
                    toastLong("Standby to enter pin");
                } else if (till.isChecked()) {
                    orderWithTill();
                   copyTill();
                } else if (payondelivery.isChecked()) {
                    orderWithPOD();
                    copyTill();

                } else {
                    toast("please select a payment method");
                }
                break;
            case R.id.till_copy:
            case R.id.till_copy_mpesa_on_delivery:
                copyTill();
                break;


        }
    }

    private void orderWithPOD() {
        String url = getString(R.string.website_adress) + "/nectar/buy/orderviapod.php";
        Log.i("PHONE", new Preferences(context).getNumber().substring(1));

        RequestBody formBody = new FormBody.Builder()
                .add("email", new Preferences(context).getEmail())//then from server can check if to search or not the return an appropriate response
                .add("amount", price)
                .add("phone", new Preferences(context).getNumber())

                .build();

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                toast("Ops! Please Try again");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Log.i("POD RES", res);
                    try {
                        JSONObject obj = new JSONObject(res);
                        String code = obj.getString("RESPONSE_CODE");
                        if (code.contentEquals("SUCCESS")) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    showPayOnDeliveryDialog();

                                }
                            });
                        } else {
                            toast("Ops, we could not process your payment try again later");
                        }
                    } catch (Exception e) {
                        Log.i("ERROR", e.getLocalizedMessage());
                    }
                } else {
                    toast("Ops! Try again");
                }
            }
        });


    }

    private void orderWithTill() {
        String url = getString(R.string.website_adress) + "/nectar/buy/orderviatill.php";
        Log.i("PHONE", new Preferences(context).getNumber().substring(1));

        RequestBody formBody = new FormBody.Builder()
                .add("email", new Preferences(context).getEmail())//then from server can check if to search or not the return an appropriate respons
                .add("amount", price)
                .add("phone", new Preferences(context).getNumber())

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
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Log.i("TILL RES", res);
                    try {
                        JSONObject obj = new JSONObject(res);
                        String code = obj.getString("RESPONSE_CODE");
                        if (code.contentEquals("SUCCESS")) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showTillDialog();

                                }
                            });
                        } else {
                            toast("Ops, we could not process your payment try again later");
                        }
                    } catch (Exception e) {
                        Log.i("ERROR", e.getLocalizedMessage());
                    }
                } else {
                    toast("Ops! Try again");
                }
            }
        });


    }

    private void showTillDialog() {

        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_till_dark, null);
        TextView main = dialogView.findViewById(R.id.shop);
        TextView primary = dialogView.findViewById(R.id.exp);
        TextView secondary = dialogView.findViewById(R.id.secondary);
        TextView pay = dialogView.findViewById(R.id.payExact);
        pay.setText("Pay exacty Ksh "+price);
        MaterialButton button = dialogView.findViewById(R.id.continueAdding);
        button.setText("CONTINUE SHOPPING");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();

            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        dialogBuilder.show();
    }

    private void showPayOnDeliveryDialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_pay_on_delivery, null);
        TextView main = dialogView.findViewById(R.id.shop);
        TextView primary = dialogView.findViewById(R.id.exp);
        TextView secondary = dialogView.findViewById(R.id.secondary);
        TextView pay = dialogView.findViewById(R.id.payExact);
        pay.setText("Pay exacty Ksh "+price);
        MaterialButton button = dialogView.findViewById(R.id.continueAdding);
        button.setText("CONTINUE SHOPPING");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();

            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        dialogBuilder.show();
    }

    private void payNow(final String price) {
        String url = getString(R.string.website_adress) + "/nectar/payment/stk.php";
        Log.i("PHONE", new Preferences(context).getNumber());

        RequestBody formBody = new FormBody.Builder()
                .add("email", new Preferences(context).getEmail())//then from server can check if to search or not the return an appropriate respons
                .add("amount", price)
                .add("phone", new Preferences(context).getNumber())
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
                if (response.isSuccessful()){
                    String res = response.body().string();
                    Log.i("PAYMENT RESPONSE", res);
                    try {
                        JSONObject obj = new JSONObject(res);
                        String code = obj.getString("RESPONSE_CODE");
                        String requestID = obj.getString("REQUEST_ID");
                        //
                        if (code.contentEquals("SUCCESS")) {
                            toast("We are processing your payment");
                            //Show alert
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
                                    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                                    View dialogView = layoutInflater.inflate(R.layout.dialog_empty_cart, null);
                                    TextView main=dialogView.findViewById(R.id.shop);
                                    TextView primary=dialogView.findViewById(R.id.exp);
                                    TextView secondary=dialogView.findViewById(R.id.secondary);
                                    main.setText("PROCESSING");
                                    primary.setText("Checking your payment");
                                    secondary.setText(R.string.checking_payment);
                                    MaterialButton button = dialogView.findViewById(R.id.continueAdding);
                                    button.setText("CONTINUE SHOPPING");
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogBuilder.dismiss();
                                            Intent intent = new Intent(context, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            getActivity().finish();

                                        }
                                    });
                                    dialogBuilder.setView(dialogView);
                                    dialogBuilder.setCancelable(false);
                                    dialogBuilder.show();

                                }
                            });

                        } else {
                            toast("Ops, we could not process your payment try again later");
                        }
                    } catch (Exception e) {

                    }
                }else {
                    toast("An error happened please try again");
                }


            }
        });
    }

    private void copyTill() {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("saved to clip", getString(R.string.paybill_number));
        clipboard.setPrimaryClip(clip);
        toast("Paybill copied to clipboard");

    }

    private void toast(String s) {
        Snackbar.make(finish, s, Snackbar.LENGTH_SHORT).show();
    }

    private void toastLong(String s) {
        Snackbar.make(finish, s, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDetails();
    }
}