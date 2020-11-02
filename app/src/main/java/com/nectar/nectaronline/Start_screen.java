package com.nectar.nectaronline;

import android.animation.Animator;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Start_screen extends AppCompatActivity {
    private static final String CHANNEL_ID = "MAINACTIVITY";
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        lottieAnimationView = findViewById(R.id.lotie);
        lottieAnimationView.setRepeatCount(Animation.INFINITE);
        checkForStoredPassword();
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        setDecor();


    }

    private void checkForStoredPassword() {
        SharedPreferences preferences = getSharedPreferences("nectar", MODE_PRIVATE);
        if (preferences.contains("password")) {//preference contains password,,,so it contains email also since both are put at same time
            String mail = preferences.getString("email", "");
            String pass = preferences.getString("password", "");
            loginNow(mail, pass);
            Log.i("checkForPassword: ", "EXISTS");
        } else {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
            Log.i("checkForPassword: ", "DOES NOT EXISTS");
        }
    }

    private void loginNow(final String mail, final String pass) {
        //inflate the dialog
        //Make a post request to te admin with the user name and password
        //get a Json response back with 2 elements...
        //RESPONSE CODE
        //REASON
        //1.SUCCESS---------The login was sucessfull.....REASON...USER IS IN DATABASE
        //2.FAILURE---------The login was unsucessfull.....REASON...USER IS IN NOT DATABASE
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
                            break;
                        }
                        String url = getString(R.string.website_adress) + "/nectar/buy/login.php";
                        RequestBody formBody = new FormBody.Builder()
                                .add("email", mail)
                                .add("password", pass)
                                .build();

                        OkHttpClient client = new OkHttpClient();
                        client.newBuilder().readTimeout(60000, TimeUnit.MILLISECONDS);

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
                            String NAME = object.getString("name");
                            String EMAIL = object.getString("email");
                            String PASSWORD = object.getString("password");
                            String NUMBER = object.getString("number");
                            String ADDRESS = object.getString("address");
                            String DESCRIPTION = object.getString("description");
                            Log.i("name", NAME);
                            Log.i("email", EMAIL);
                            Log.i("password", PASSWORD);
                            Log.i("number", NUMBER);
                            Log.i("address", ADDRESS);
                            Log.i("desc", DESCRIPTION);
                            SharedPreferences preferences = getSharedPreferences("nectar", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("email", EMAIL);
                            editor.putString("password", PASSWORD);
                            editor.putString("number", NUMBER);
                            editor.putString("address", ADDRESS);
                            editor.putString("desc", DESCRIPTION);
                            editor.putString("name", NAME);
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            toast(desc);
                        }
                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            String versionName = pInfo.versionName;
                            int verCode = pInfo.versionCode;
                            if (verCode < Integer.parseInt(version)) {
                                showVersionNotification();
                                Log.i("SHOWING DIALOG", "TRUE");
                            } else {
                                Log.i("NOT SHOWING DIALOG", "VERSION CODE IS " + verCode + " SERVER VERSION IS " + version);

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
                .setContentTitle("Hey " + new Preferences(getApplicationContext()).getName())
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

    private void setDecor() {
        View decorView = getWindow().getDecorView();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.splash_nav_bar));
        }
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void toast(String s) {
        Snackbar.make(lottieAnimationView, s, Snackbar.LENGTH_LONG).show();
    }
}