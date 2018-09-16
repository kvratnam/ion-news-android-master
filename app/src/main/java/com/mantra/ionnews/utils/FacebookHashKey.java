package com.mantra.ionnews.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by TaNMay on 28/09/16.
 */

public class FacebookHashKey {

    public static String TAG = "FacebookHashKey ==>";

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            String packageName = context.getApplicationContext().getPackageName();
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Log.d(TAG, "Package Name: " + context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                Log.e(TAG, "Key Hash: " + key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e(TAG, "Name not found: " + e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such an algorithm: " + e.toString());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.toString());
        }
        return key;
    }
}
