package com.example.meplusplus.WaterReminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.meplusplus.R;

public class ReminderBroadcast extends BroadcastReceiver {
    public static int id = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

//Detaliile Notificarii
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyMe")
                .setSmallIcon(R.drawable.icon_app_mobile)
                .setContentTitle("MePlusPlus")
                .setContentText(" Sweet reminder to drink some water ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(id++, builder.build());
    }
}
