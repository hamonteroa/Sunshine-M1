package com.example.android.sunshine.app.fcm;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by hamonteroa on 10/3/16.
 */

public class SunshineMessagingService extends FirebaseMessagingService {

    private static final String LOG_TAG = SunshineMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(LOG_TAG, "FROM: " + remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Log.d(LOG_TAG, "Message data: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void sendNotification(String body) {
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0,
                (new NotificationCompat.Builder(this))
                        .setSmallIcon(R.drawable.art_clear)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.art_storm))
                        .setContentTitle("Weather Alert!")
                        .setContentText(body)
                        .setContentIntent(PendingIntent.getActivity(
                                this,
                                0,
                                (new Intent(this, MainActivity.class)).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                                PendingIntent.FLAG_ONE_SHOT
                        ))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build()
        );
    }
}
