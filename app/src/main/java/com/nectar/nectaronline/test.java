package com.nectar.nectaronline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class test extends AppCompatActivity {
Button sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        sign=findViewById(R.id.sign);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.playstore_link)));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Please install a web browser like opera mini or google chrome to proceed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.playstore_link)));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Please install a web browser like opera mini or google chrome to proceed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }
}