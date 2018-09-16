package com.mantra.ionnews.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.mantra.ionnews.R;
import com.mantra.ionnews.models.responses.PushNotificationObject;
import com.mantra.ionnews.ui.activities.DashboardActivity;
import com.mantra.ionnews.ui.activities.WebViewActivity;
import com.mantra.ionnews.utils.AppConstants;

import org.json.JSONObject;

import static com.mantra.ionnews.utils.AppConstants.KEY_PUSH_MESSAGE;
import static com.mantra.ionnews.utils.ConstantClass.FCM_SERVICE;

/**
 * Created by TaNMay on 18/04/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = FCM_SERVICE;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getNotification() != null)
            Log.d(TAG, "Message data payload: " + remoteMessage.getNotification().getBody());

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject dataJson = new JSONObject(remoteMessage.getData());
                handleDataMessage(dataJson);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Firebase Push Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject data) {
        Log.d(TAG, "push json: " + data.toString());

        try {

            Gson gson = new Gson();
            PushNotificationObject pushNotificationObject = gson.fromJson(data.toString(), PushNotificationObject.class);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

                Intent resultIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                resultIntent.putExtra(KEY_PUSH_MESSAGE, pushNotificationObject.getMessage());
                showNotificationMessageWithBigImage(
                        getApplicationContext(),
                        pushNotificationObject.getTitle(),
                        pushNotificationObject.getMessage(),
                        "",
                        resultIntent,
                        pushNotificationObject.getImageUrl(),
                        pushNotificationObject.getCrawlUrl());
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                resultIntent.putExtra(KEY_PUSH_MESSAGE, pushNotificationObject.getMessage());
                showNotificationMessageWithBigImage(
                        getApplicationContext(),
                        pushNotificationObject.getTitle(),
                        pushNotificationObject.getMessage(),
                        "",
                        resultIntent,
                        pushNotificationObject.getImageUrl(),
                        pushNotificationObject.getCrawlUrl());
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message,
                                                     String timeStamp, Intent intent, String imageUrl,
                                                     String crawlUrl) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl, crawlUrl);
    }

    private void showNotificationMessage(Context context, String title, String message,
                                         String timeStamp, Intent intent, String crawlUrl) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, crawlUrl);
    }
}