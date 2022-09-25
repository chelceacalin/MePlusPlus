package com.example.meplusplus.Widgets;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.meplusplus.FoodTracking.CaloriesActivity;
import com.example.meplusplus.R;

public class WidgetMain extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, CaloriesActivity.class);
            @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);


            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widgetaddfooditem);
            views.setOnClickPendingIntent(R.id.widget_add_food_text, pendingIntent);
            views.setOnClickPendingIntent(R.id.widget_add_food_button, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
