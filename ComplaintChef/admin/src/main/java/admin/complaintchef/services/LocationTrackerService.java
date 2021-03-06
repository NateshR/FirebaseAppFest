package admin.complaintchef.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import common.complaintcheflib.util.LocationUtils;
import common.complaintcheflib.util.Sessions;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class LocationTrackerService extends Service implements OnCompleteListener<Void> {

    public static final String TAG = LocationTrackerService.class.getSimpleName();
    private static final String KEY_LAST_LATITUDE = "lastLocationLat", KEY_LAST_LONGITUDE = "lastLocationLong";
    private static boolean instanceRunning = false;
    private DatabaseReference mDatabaseReference;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private GeofencingClient geofencingClient;
    private BroadcastReceiver geofenceExitReceiver;

    public LocationTrackerService() {
    }

    public static boolean isInstanceRunning() {
        return instanceRunning;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) throws SecurityException {
        instanceRunning = true;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceExitReceiver = new GeofenceExitReceiver();
        registerReceiver(geofenceExitReceiver, new IntentFilter(GeofenceTransitionsIntentService.ACTION));

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    setUpGeoFence(location);
                    saveLocation(location);
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(LocationUtils.createLocationRequest(), locationCallback, null);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        unregisterReceiver(geofenceExitReceiver);
        instanceRunning = false;
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setUpGeoFence(Location location) throws SecurityException {
        geofencingClient.addGeofences(LocationUtils.createGeofencingRequest(location), getGeofencePendingIntent())
                .addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {

    }

    private void saveLocation(Location location) {
        if (location == null)
            return;
        Log.d(TAG, "saveLocation: " + location.toString());
        getmDatabaseReference().child(KEY_LAST_LATITUDE).setValue(location.getLatitude());
        getmDatabaseReference().child(KEY_LAST_LONGITUDE).setValue(location.getLongitude());
    }

    private DatabaseReference getmDatabaseReference() {
        if (mDatabaseReference == null) {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(Sessions.loadUsername(this));
            mDatabaseReference.keepSynced(true);
        }
        return mDatabaseReference;
    }

    private void sendCurrentLocation() {
        LocationUtils.getCurrentLocation(this, new LocationUtils.LocationReceivedCallback() {
            @Override
            public void onLocationReceived(@Nullable Location location) {
                saveLocation(location);
            }
        });
    }

    private class GeofenceExitReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            sendCurrentLocation();
        }
    }
}
