package com.example.y.study;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class MyWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //RemoteView 初始化
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);

        ComponentName componentName=new ComponentName(context,MyWidget.class);
        appWidgetManager.updateAppWidget(componentName,views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}

