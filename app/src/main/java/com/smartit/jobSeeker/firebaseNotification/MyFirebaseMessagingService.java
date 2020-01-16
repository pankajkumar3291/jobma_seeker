package com.smartit.jobSeeker.firebaseNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smartit.AppConstants;
import com.smartit.jobSeeker.R;

import java.util.Map;

import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static String TAG = MyFirebaseMessagingService.class.getSimpleName();


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
//        Log.d(TAG, "Refreshed token: " + token);
        System.out.println("MyFirebaseMessagingService.onNewToken " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        System.out.println("MyFirebaseMessagingService.onMessageReceived " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            startInForeground(remoteMessage.getData());
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                startInForeground(remoteMessage.getData());
                // Handle message within 10 seconds
                // handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private NotificationManager mNotificationManager;

    private void startInForeground(Map<String, String> data) {

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                new Intent(), // add this
                PendingIntent.FLAG_UPDATE_CURRENT);


        // Intent notificationIntent = new Intent(this, WorkoutActivity.class);
        //PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AppConstants.NOTIFICATION_CHANNEL_ID_CURRENT_LOCATION)
                .setSmallIcon(R.drawable.ic_noti)
                .setContentTitle(data.get("title"))
                .setContentText(data.get("body"))
                .setPriority(Notification.PRIORITY_DEFAULT)
                //.setContentText("HELLO")
                //.setTicker("TICKER")
                .setContentIntent(contentIntent);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(AppConstants.NOTIFICATION_CHANNEL_ID_CURRENT_LOCATION, AppConstants.NOTIFICATION_CHANNEL_NAME_CURRENT_LOCATION, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(AppConstants.NOTIFICATION_CHANNEL_DESC_CURRENT_LOCATION);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        mNotificationManager.notify(1, builder.build());

    }


}
