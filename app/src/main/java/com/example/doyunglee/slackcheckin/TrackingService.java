package com.example.doyunglee.slackcheckin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TrackingService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    static final String LOG_TAG = TrackingService.class.getSimpleName();
    static final double INTREPID_LAT = 42.367050;
    static final double INTREPID_LONG = -71.080558;
    static final long INTERVAL_SET = 10 * 1000; // 10 seconds, in milliseconds
    static final long INTERVAL_FASTEST = 1 * 1000; // 1 second, in milliseconds
    static final double LAT_TO_METER = 111080.386284; //meters per latitude degree
    static final double LONG_TO_METER = 82372.898177; //meters per longitude degree
    static final double RADIUS_OF_NOTIF = 50.0; //meters

    static final double BOSTON_LAT = 42.364506;
    static final double BOSTON_LONG = -71.038887;



    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "Servicing!");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(INTERVAL_SET)
                .setFastestInterval(INTERVAL_FASTEST);

        mGoogleApiClient.connect();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG_TAG, "Connected.");

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.i(LOG_TAG, "the coordinates are: " + location.toString());
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "Connection Suspended in TrackingService");
    }

    @Override
    public void onLocationChanged(Location location) {
        double xDistMeters;
        double yDistMeters;
        double distFinal;

        Log.i(LOG_TAG, "LOCATION LAT " + location.getLatitude());
        Log.i(LOG_TAG, "LOCATION LONG " + location.getLongitude());

        xDistMeters = convertLatToMeters(location.getLatitude());
        yDistMeters = convertLongToMeters(location.getLongitude());

        distFinal = Math.sqrt(Math.pow(xDistMeters, 2) + Math.pow(yDistMeters, 2));
        Log.i(LOG_TAG, "distFinal " + distFinal);

        if (distFinal < RADIUS_OF_NOTIF) {
            callNotification();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "Connection Failed in TrackingService");
    }

    public double convertLatToMeters(double latitude) {
        double xDistRaw = Math.abs(latitude - INTREPID_LAT);
        return LAT_TO_METER * xDistRaw;
    }

    public double convertLongToMeters(double longitude) {
        double yDistRaw = Math.abs(longitude - INTREPID_LONG);
        return LONG_TO_METER * yDistRaw;
    }

    public void callNotification() {
        Log.i(LOG_TAG, "SEND NOTIFICATION!");
        PendingIntent contentIntent =
                PendingIntent.getBroadcast(
                        this, 0, new Intent(this, NotificationReceiver.class), 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentIntent(contentIntent)
                        .setContentText(getString(R.string.notification_text));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}