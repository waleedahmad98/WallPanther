package com.waleed.wallpanther;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class BroadcastNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("wpWallpaper", "Wallpaper Download", importance);
            channel.setDescription("Notification for Wallpaper Download");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "wpWallpaper");
        builder.setContentTitle("Fresh Wallpapers have arrived..")
                .setContentText("Download Complete")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        Intent new_intent = new Intent(Intent.ACTION_VIEW);
        new_intent.setDataAndType(Uri.parse(intent.getStringExtra("path")), "image/*");

        PendingIntent p = PendingIntent.getActivity(context, 0, new_intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(p);
        builder.setAutoCancel(true);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, builder.build());
    }
}
