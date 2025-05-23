package com.example.report.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.report.MainActivity;
import com.example.report.R;

public class NotificationHelper {
    private static final String CHANNEL_ID_REPORTS = "city_report_channel";
    private static final String CHANNEL_NAME_REPORTS = "Reportes";
    private static final String CHANNEL_DESC_REPORTS = "Notificações sobre reportes de problemas urbanos";
    
    private static final int NOTIFICATION_ID_NEW_REPORT = 1;
    private static final int NOTIFICATION_ID_STATUS_UPDATE = 2;

    private final Context context;
    private final NotificationManagerCompat notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = NotificationManagerCompat.from(context);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID_REPORTS,
                CHANNEL_NAME_REPORTS,
                NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC_REPORTS);
            
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showReportCreatedNotification(String title, String description) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_REPORTS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Reporte Criado: " + title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID_NEW_REPORT, builder.build());
    }

    public void showStatusUpdateNotification(String title, String description) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_REPORTS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Status Atualizado: " + title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID_STATUS_UPDATE, builder.build());
    }

    public void cancelAllNotifications() {
        notificationManager.cancelAll();
    }

    public void cancelNotification(int notificationId) {
        notificationManager.cancel(notificationId);
    }
}
