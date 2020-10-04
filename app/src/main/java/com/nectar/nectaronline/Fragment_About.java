package com.nectar.nectaronline;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_About#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_About extends Fragment implements View.OnClickListener {
    MaterialButton phone;
    MaterialButton instagram;
    MaterialButton email;
    MaterialButton facebook;
    MaterialButton whatsapp;
    MaterialButton web;
    MaterialButton youtube;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.badge. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_About() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_About.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_About newInstance(String param1, String param2) {
        Fragment_About fragment = new Fragment_About();
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
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        phone=v.findViewById(R.id.phone);
        instagram=v.findViewById(R.id.instagram);
        email=v.findViewById(R.id.email);
        facebook=v.findViewById(R.id.facebook);
        whatsapp=v.findViewById(R.id.whatsapp);
        web=v.findViewById(R.id.web);
        youtube=v.findViewById(R.id.youtube);
        phone.setOnClickListener(this);
        instagram.setOnClickListener(this);
        email.setOnClickListener(this);
        facebook.setOnClickListener(this);
        whatsapp.setOnClickListener(this);
        web.setOnClickListener(this);
        youtube.setOnClickListener(this);

        return v;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone:
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+" + getString(R.string.phone_adress)));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext().getApplicationContext(), "Please install a calling app like a dialler to proceed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            case R.id.instagram:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.instagram)));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please install a web browser like opera mini or google chrome to proceed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;

            case R.id.email:
                Toast.makeText(getContext().getApplicationContext(), "nectarmobile2019@gmail.com", Toast.LENGTH_LONG).show();

                break;
            case R.id.facebook:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.facebook_adress)));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please install a web browser like opera mini or google chrome to proceed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                break;
            case R.id.youtube:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube)));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please install a web browser like opera mini or google chrome to proceed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                break;
            case R.id.whatsapp:
                try {
                    String string="Welcome to our whatsapp page";
                    String url = "https://api.whatsapp.com/send?phone=" + getString(R.string.phone_adress) + "&text=" + URLEncoder.encode(string, "UTF-8");
                    Toast.makeText(getActivity().getApplicationContext(), "Taking you to Whatsapp", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent2);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "OPs! An error occurred, try again", Toast.LENGTH_SHORT).show();

                }


                break;
            case R.id.web:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.website_adress)));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please install a web browser like opera mini or google chrome to proceed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                break;

        }
    }
}