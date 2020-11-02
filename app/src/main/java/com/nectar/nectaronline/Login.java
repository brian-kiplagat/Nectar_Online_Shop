package com.nectar.nectaronline;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    private static final String CHANNEL_ID = "MAINACTIVITY";
    TextInputLayout email;
    TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getNotificationBigPicture();

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
                        String url = getString(R.string.website_adress) + "/nectar/buy/login.php";
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
                        String version = obj.getString("LATEST");
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
                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            String versionName = pInfo.versionName;
                            int verCode = pInfo.versionCode;
                            if (verCode < Integer.parseInt(version)) {
                                showVersionNotification();
                                Log.i("SHOWING DIALOG","TRUE");
                            }else{
                                Log.i("NOT SHOWING DIALOG","VERSION CODE IS "+verCode+" SERVER VERSION IS "+version);

                            }

                        } catch (PackageManager.NameNotFoundException e) {
                            Log.i("PK", e.getLocalizedMessage());
                        }
                        break;

                    } catch (Exception e) {
                        Log.i("LOGIN ERROR", e.getLocalizedMessage());
                    }

                }
            }
        });
        thread.start();


    }

    private void showVersionNotification() {
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(getApplicationContext(), test.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        String longtext = "The Nectar shopping app is rising to be one of the leading online shop for goods. Search, find and discover a wide variety of accessories. This update bring new exciting features. Click to update";
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_updated);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Hey "+new Preferences(getApplicationContext()).getName())
                .setContentText("We updated the app")
                .setLargeIcon(largeIcon)
                .setContentIntent(resultPendingIntent).setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(longtext)).build();
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "name", importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        int notificationId = 2;
        notificationManager.notify(notificationId, notification);
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

    private void getNotificationBigPicture() {
        String url = getString(R.string.website_adress) + "/nectar/buy/getblackfriday.php";
        RequestBody formBody = new FormBody.Builder()
                .add("offer", "")
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
                String res = response.body().string();
                Log.i("BLACK FRIDAY RESPONSE", res);
                //get the first image
                try {
                    JSONObject obj = new JSONObject(res);
                    String code = obj.getString("RESPONSE_CODE");
                    String desc = obj.getString("RESPONSE_DESC");
                    if (code.contentEquals("SUCCESS")) {
                        String STUFF = obj.getString("SHOP_ITEMS");
                        //Log.i("SHOP ITEMS: ", STUFF);
                        JSONObject object = new JSONObject(STUFF);
                        JSONArray array = object.getJSONArray("items");
                        JSONObject obje = array.getJSONObject(0);
                        final String id = obje.getString("id");
                        final String brand = obje.getString("brand");
                        final String name = obje.getString("name");
                        final String newPrice = obje.getString("new");
                        final String old = obje.getString("old");
                        final String description = obje.getString("description");
                        final String specification = obje.getString("specification");
                        final String keyfeatures = obje.getString("keyfeatures");
                        final String size = obje.getString("size");
                        final String color = obje.getString("color");
                        final String instock = obje.getString("instock");
                        final String weight = obje.getString("weight");
                        final String material = obje.getString("material");
                        final String inbox = obje.getString("inbox");
                        final String waranty = obje.getString("waranty");
                        final String state = obje.getString("state");
                        final String images = obje.getString("images");
                        final String tag = obje.getString("tag");
                        final String offerString = "Get " + size + " " + name + " 70% OFF!";
                        try {
                            // JSONObject object = new JSONObject(model.getImages());
                            JSONArray array2 = new JSONArray(images);
                            String prelink = array2.getString(0);
                            Log.i("LINK_IMAGES", prelink);
                            String link = getString(R.string.website_adress) + "/nectar/seller/" + prelink;
                            InputStream inputStream;
                            try {
                                inputStream = new java.net.URL(link).openStream();
                                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Create an Intent for the activity you want to start
                                        Intent resultIntent = new Intent(getApplicationContext(), Login.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
                                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                                        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
                                        PendingIntent resultPendingIntent =
                                                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                .setSmallIcon(R.drawable.ic_logo)
                                                .setContentTitle("THIS BLACK FRIDAY: 3.00 PM - 3.20 PM \uD83D\uDE3D \uD83E\uDD11 \uD83D\uDE2C")
                                                .setContentText(offerString)
                                                .setStyle(new NotificationCompat.BigPictureStyle()
                                                        .bigPicture(bitmap))
                                                .setContentIntent(resultPendingIntent).build();
                                        //.setStyle(new NotificationCompat.BigTextStyle()
                                        //                                                        .bigText(offerString))
                                        // Create the NotificationChannel, but only on API 26+ because
                                        // the NotificationChannel class is new and not in the support library
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            CharSequence name = getString(R.string.channel_name);
                                            String description = getString(R.string.channel_description);
                                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                                            channel.setDescription(description);
                                            // Register the channel with the system; you can't change the importance
                                            // or other notification behaviors after this
                                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                            notificationManager.createNotificationChannel(channel);
                                        }
                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                                        // notificationId is a unique int for each notification that you must define
                                        int notificationId = 1;
                                        notificationManager.notify(notificationId, notification);

                                    }
                                });


                            } catch (IOException e) {
                                Log.i("EXEP", e.getMessage());
                            }
                        } catch (Exception e) {
                            Log.i("PARSE ERROR", e.getLocalizedMessage());
                        }


                    }


                } catch (Exception e) {
                    Log.i("PARSE ERROR", e.getLocalizedMessage());
                }
            }
        });

    }

    public void forgotPassword(View view) {
        final SharedPreferences preferences = getSharedPreferences("nectar", Context.MODE_PRIVATE);
        if (preferences.contains("password")) {
            String PASSWORD = preferences.getString("password", "");
            final AlertDialog dialogBuilder = new AlertDialog.Builder(Login.this).create();
            final LayoutInflater inflater = Login.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_confirm, null);
            ImageView imageView = dialogView.findViewById(R.id.image);
            MaterialButton okay = dialogView.findViewById(R.id.okay);
            MaterialButton cancel = dialogView.findViewById(R.id.cancel);
            TextView main = dialogView.findViewById(R.id.main);
            TextView primary = dialogView.findViewById(R.id.exp);
            TextView secondary = dialogView.findViewById(R.id.secondary);
            imageView.setImageResource(R.drawable.ic_baseline_help_outline_24);
            okay.setText("OKAY");
            cancel.setText("RESET");
            main.setText("FORGOT PASSWORD ?");
            primary.setText("We saved your previous password for you. Here it is, keep it safe");
            secondary.setText(PASSWORD);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPassword();
                }
            });
            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            });
            dialogBuilder.setView(dialogView);
            dialogBuilder.show();

        } else {//no password stored
            resetPassword();
        }
    }

    private void resetPassword() {
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}