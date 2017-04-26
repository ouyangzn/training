package com.ouyangzn.module.testEMUINotification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.ouyangzn.MainActivity;
import com.ouyangzn.R;

public class NotificationActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notification);
  }

  public void notify(View view) {
    Context context = getApplicationContext();
    //TaskStackBuilder pendingBuilder = TaskStackBuilder.create(context);
    //pendingBuilder.addParentStack(MainActivity.class);
    //pendingBuilder.addNextIntent(getIntent(context, MainActivity.class));
    //PendingIntent contentIntent = pendingBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    Intent intent = getIntent(context, MainActivity.class);
    PendingIntent contentIntent =
        PendingIntent.getActivity(context, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    NotificationCompat.Builder builder =
        getNotification(context, "demo通知", "你有一条通知", contentIntent);
    NotificationManager manager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    manager.notify("tag", 1, builder.build());
  }

  @NonNull private Intent getIntent(Context context, Class<? extends Activity> clazz) {
    Intent intent = new Intent(context, clazz);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    return intent;
  }

  private NotificationCompat.Builder getNotification(Context context, String title, String content,
      PendingIntent contentIntent) {
    Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
    return new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(
        R.mipmap.ic_launcher)
        .setLargeIcon(largeIcon)
        .setDefaults(Notification.DEFAULT_ALL)
        .setAutoCancel(true)
        .setContentTitle(title)
        .setContentText(content)
        .setTicker(content)
        .setWhen(System.currentTimeMillis())
        .setContentIntent(contentIntent);
  }
}
