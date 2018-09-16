package com.mantra.ionnews.fcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mantra.ionnews.utils.AppConstants;

import static com.mantra.ionnews.utils.ConstantClass.FCM_SERVICE;

/**
 * Created by TaNMay on 18/04/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private String TAG = FCM_SERVICE;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(AppConstants.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {

    }
}
