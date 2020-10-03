package com.nectar.nectaronline;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    TextInputLayout email;
    TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }
        checkForStoredPassword();
        Preferences preferences = new Preferences(getApplicationContext());
        Log.i("name", preferences.getName());
        Log.i("email", preferences.getEmail());
        Log.i("password",preferences.getPassword());
        Log.i("number", preferences.getNumber());
        Log.i("address", preferences.getAddress());
        Log.i("desc", preferences.getEmail());

    }

    private void checkForStoredPassword() {
        SharedPreferences preferences = getSharedPreferences("nectar", MODE_PRIVATE);

        if (preferences.contains("password")) {//preference contains password,,,so it contains email also since both are put at same time
            String mail = preferences.getString("email", "");
            String pass = preferences.getString("password", "");
            email.getEditText().setText(mail);
            password.getEditText().setText(pass);
            loginNow(mail, pass);

        }
    }

    private boolean validateEmail() {
        String mail = email.getEditText().getText().toString().trim();
        if (mail.isEmpty()) {
            email.setError("field cannot be empty");
            return false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                email.setError("please enter a valid email address");
                return false;
            } else {
                email.setError(null);
                return true;
            }
        }
    }

    private boolean validatePassword() {
        String Upass = password.getEditText().getText().toString().trim();
        if (Upass.isEmpty()) {
            password.setError("field cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void login(View view) {

        if (!validatePassword() | !validateEmail()) {
            return;
        }
        String mail = email.getEditText().getText().toString().trim();
        String pass = password.getEditText().getText().toString().trim();
        loginNow(mail, pass);

    }

    private void loginNow(final String mail, final String pass) {
        //inflate the dialog
        //Make a post request to te admin with the user name and password
        //get a Json response back with 2 elements...
        //RESPONSE CODE
        //REASON
        //1.SUCCESS---------The login was sucessfull.....REASON...USER IS IN DATABASE
        //2.FAILURE---------The login was unsucessfull.....REASON...USER IS IN NOT DATABASE
        final AlertDialog dialogBuilder = new AlertDialog.Builder(Login.this).create();
        LayoutInflater layoutInflater = Login.this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_shimmer_with_logo, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        dialogBuilder.show();
        //Use a for loop to iterate via failed trials
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 3; i++) {
                    try {
                        if (i == 1) {
                            toast("Could not connect, trying again");
                        }
                        if (i == 2) {
                            toast("This taking too long check your internet connection");
                        }
                        if (i == 3) {
                            toast("Check your internet connection, then try again");
                            dialogBuilder.dismiss();
                            break;
                        }
                        String url = getString(R.string.website_adress) + "/nectar/login.php";
                        RequestBody formBody = new FormBody.Builder()
                                .add("email", mail)
                                .add("password", pass)
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
                        String desc = obj.getString("RESPONSE_DESC");
                        //
                        if (code.contentEquals("SUCCESS")) {
                            JSONArray array = obj.getJSONArray("DETAILS");
                            JSONObject object = array.getJSONObject(0);
                            String NAME=object.getString("name");
                            String EMAIL=object.getString("email");
                            String PASSWORD=object.getString("password");
                            String NUMBER=object.getString("number");
                            String ADDRESS=object.getString("address");
                            String DESCRIPTION=object.getString("description");
                            Log.i("name",NAME);
                            Log.i("email",EMAIL);
                            Log.i("password",PASSWORD);
                            Log.i("number",NUMBER);
                            Log.i("address",ADDRESS);
                            Log.i("desc",DESCRIPTION);
                            SharedPreferences preferences = getSharedPreferences("nectar", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("email", EMAIL);
                            editor.putString("password", PASSWORD);
                            editor.putString("number", NUMBER);
                            editor.putString("address", ADDRESS);
                            editor.putString("desc", DESCRIPTION);
                            editor.putString("name", NAME);
                            toast("Hello " + NAME);
                            editor.apply();
                            dismissDialoge(dialogBuilder);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            toast(desc);
                        }
                        dismissDialoge(dialogBuilder);
                        break;

                    } catch (Exception e) {
                        Log.i("ERROR", e.getLocalizedMessage());
                    }

                }
            }
        });
        thread.start();


    }

    private void dismissDialoge(final AlertDialog dialogBuilder) {
        Login.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.dismiss();
            }
        });
    }

    private void toast(final String s) {
        Login.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

            }
        });
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

    public void signIn(View view) {
        startActivity(new Intent(getApplicationContext(), SignUp.class));

    }

    public void forgotPassword(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}