package com.nectar.nectaronline;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class BootReceived extends BroadcastReceiver {
    private static final String CHANNEL_ID = "1";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.contentEquals(intent.getAction())) {
            Toast.makeText(context, "BOOT COMPLETED", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "NOT RECEIVED BOOT COMPLETED", Toast.LENGTH_SHORT).show();
        }
        showVersionNotification(context);
    }

    private void showVersionNotification(Context ctx) {
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(ctx, test.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        String longtext = "The Nectar shopping app is rising to be one of the leading online shop for goods. Search, find and discover a wide variety of accessories. This we bring you new exciting features in online shopping with same day delivery. Shop now";
        Bitmap largeIcon = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_updated);
        Notification notification = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Hey " + new Preferences(ctx).getName())
                .setContentText("We updated the app")
                .setLargeIcon(largeIcon)
                .setContentIntent(resultPendingIntent).setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(longtext)).build();
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ctx.getString(R.string.channel_name);
            String description = ctx.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "name", importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);

// notificationId is a unique int for each notification that you must define
        int notificationId = 2;
        notificationManager.notify(notificationId, notification);
    }

}
