package com.nectar.nectaronline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }


    public void facebook(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.facebook_adress)));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Please install a web browser like opera mini or google chrome to proceed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public void web(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.website_adress)));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Please install a web browser like opera mini or google chrome to proceed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
    public void twitter(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.twitter_adress)));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Please install a web browser like opera mini or google chrome to proceed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public void call(View view) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:+254792517691"));
            startActivity(callIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Please install a calling app like a dialler to proceed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}