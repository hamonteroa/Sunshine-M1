package com.example.android.sunshine.app.muzei;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;
import com.example.android.sunshine.app.sync.SunshineSyncAdapter;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.MuzeiArtSource;

/**
 * Created by hamonteroa on 10/19/16.
 */

public class WeatherMuzeiSource extends MuzeiArtSource {


    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC
    };

    // these indices must match the projection
    private static final int INDEX_WEATHER_ID = 0;
    private static final int INDEX_SHORT_DESC = 1;

    /**
     * Remember to call this constructor from an empty constructor!
     *
     * @param name Should be an ID-style name for your source, usually just the class name. This is
     *             not user-visible and is only used for {@linkplain #getSharedPreferences()
     *             storing preferences} and in system log output.
     */
    public WeatherMuzeiSource(String name) {
        super(name);
    }

    public WeatherMuzeiSource() {
        super("WeatherMuzeiSource");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        boolean dataUpdated = intent != null
                && SunshineSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction());
        if(dataUpdated && isEnabled()) {
            onUpdate(UPDATE_REASON_OTHER);
        }
    }

    @Override
    protected void onUpdate(int reason) {
        String locationQuery = Utility.getPreferredLocation(this);
        Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(locationQuery, System.currentTimeMillis());

        Cursor cursor = getContentResolver().query(
                weatherUri,
                FORECAST_COLUMNS,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()) {
            int weatherId = cursor.getInt(INDEX_WEATHER_ID);
            String desc = cursor.getString(INDEX_SHORT_DESC);

            String imageUrl = Utility.getImageUrlForWeatherCondition(weatherId);
            if (imageUrl != null) {
                publishArtwork(new Artwork.Builder()
                        .imageUri(Uri.parse(imageUrl))
                        .title(desc)
                        .byline(locationQuery)
                        .viewIntent(new Intent(this, MainActivity.class))
                        .build()
                );
            }
        }
        cursor.close();
    }
}
