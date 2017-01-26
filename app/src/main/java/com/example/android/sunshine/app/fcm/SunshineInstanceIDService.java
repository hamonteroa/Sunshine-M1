package com.example.android.sunshine.app.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by hamonteroa on 10/3/16.
 */

public class SunshineInstanceIDService extends FirebaseInstanceIdService {

    public static final String LOG_TAG = SunshineInstanceIDService.class.getName();

    @Override
    public void onTokenRefresh() {
        Log.d(LOG_TAG, "token: " + FirebaseInstanceId.getInstance().getToken());
    }
}
