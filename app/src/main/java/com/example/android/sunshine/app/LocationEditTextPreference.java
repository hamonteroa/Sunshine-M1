package com.example.android.sunshine.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by hamonteroa on 9/28/16.
 */

public class LocationEditTextPreference extends EditTextPreference {
    private final String LOG_TAG = LocationEditTextPreference.class.getName();
    static final private int DEFAULT_MINIMUM_LOCATION_LENGTH = 2;
    private int mMinLength;

    public LocationEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LocationEditTextPreference,
                0, 0);
        try {
            mMinLength = a.getInteger(R.styleable.LocationEditTextPreference_minLength, DEFAULT_MINIMUM_LOCATION_LENGTH);
        } finally {
            a.recycle();
        }

        // Check to see if Google Play services is available. The Place Picker API is available
        // through Google Play services, so if this is false, we'll just carry on as though this
        // feature does not exist. If it is true, however, we can add a widget to our preference.
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getContext());
        if (resultCode == ConnectionResult.SUCCESS) {
            // Add the get current location widget to our location preference
            setWidgetLayoutResource(R.layout.pref_current_location);
        }
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        EditText et = getEditText();
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Dialog d = getDialog();

                if (d instanceof AlertDialog) {
                    AlertDialog dialog = (AlertDialog) d;
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                    if (s.length() < mMinLength) {
                        positiveButton.setEnabled(false);
                    } else {
                        positiveButton.setEnabled(true);
                    }
                }
            }
        });
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);
        View currentLocation = view.findViewById(R.id.current_location);
        currentLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Context context = getContext();

                // Launch the Place Picker so that the user can specify their location, and then
                // return the result to SettingsActivity.
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

                // We are in a view right now, not an activity. So we need to get ourselves
                // an activity that we can use to start Place Picker intent. By using
                // Settings Activity in this way, we can ensure the result of the Place Picker
                // intent comes to the right place for us to process it.
                Activity settingsActivity = (SettingsActivity) context;

                try {
                    //Intent intent = intentBuilder.build(settingsActivity);
                    settingsActivity.startActivityForResult(
                            intentBuilder.build(settingsActivity), SettingsActivity.PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException ex) {
                    // What did you do?? this is why we check Google Play Services
                    // before calling!!!
                    Log.e(LOG_TAG, "exception: " + ex);
                }
            }
        });
        return view;
    }
}
