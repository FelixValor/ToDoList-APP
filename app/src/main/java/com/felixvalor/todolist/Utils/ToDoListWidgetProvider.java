package com.felixvalor.todolist.Utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.felixvalor.todolist.R;
import com.felixvalor.todolist.Task_PopActivity;
import com.felixvalor.todolist.Utils.DatabaseHandler;

import net.sqlcipher.database.SQLiteDatabase;

public class ToDoListWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
                Intent intent = new Intent(context, Task_PopActivity.class);
                intent.putExtra("isUpdate",false);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_add_task_button);
                views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

                appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
