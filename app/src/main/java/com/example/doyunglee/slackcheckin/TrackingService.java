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

/**
 * Created by doyunglee on 6/16/15.
 */
public class TrackingService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    static final String LOG_TAG = TrackingService.class.getSimpleName();
    static final double INTREPID_LAT = 42.367050;
    static final double INTREPID_LONG = -71.080558;

    static final double BOSTON_LAT = 42.364506;
    static final double BOSTON_LONG = -71.038887;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        Log.i(LOG_TAG, "Servicing!");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

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

    }

    @Override
    public void onLocationChanged(Location location) {
        double xDistRaw;
        double yDistRaw;
        double xDistMeters;
        double yDistMeters;
        double distFinal;

        Log.i(LOG_TAG, "LOCATION LAT " + location.getLatitude());
        Log.i(LOG_TAG, "LOCATION LONG " + location.getLongitude());

        xDistRaw = Math.abs(location.getLatitude() - INTREPID_LAT);
        yDistRaw = Math.abs(location.getLongitude() - INTREPID_LONG);
        xDistMeters = 111080.386284 * xDistRaw;
        yDistMeters = 82372.898177 * yDistRaw;

        distFinal = Math.sqrt(Math.pow(xDistMeters,2) + Math.pow(yDistMeters,2));
        Log.i(LOG_TAG, "distFinal " + distFinal);

        if (distFinal < 50.0) {
            //Call Notification.
            Log.i(LOG_TAG, "SEND NOTIFICATION!");
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("My notification")
                            .setContentText("Hello World!");
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(0, mBuilder.build());

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}