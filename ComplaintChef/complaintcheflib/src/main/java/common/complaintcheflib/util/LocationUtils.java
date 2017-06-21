package common.complaintcheflib.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class LocationUtils {

    private static final int INTERVAL = 10000;
    private static final int ACCURACY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private static final int GEOFENCE_RADIUS = 100;

    public static LocationRequest createLocationRequest(int interval) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(interval / 2);
        mLocationRequest.setPriority(ACCURACY);
        return mLocationRequest;
    }

    public static LocationRequest createLocationRequest() {
        return createLocationRequest(INTERVAL);
    }

    public static GeofencingRequest createGeofencingRequest(Location location){
        return new GeofencingRequest.Builder().addGeofence(new Geofence.Builder()
                .setRequestId(String.valueOf(System.currentTimeMillis()))
                .setCircularRegion(location.getLatitude(), location.getLongitude(), GEOFENCE_RADIUS)
                .setExpirationDuration(INTERVAL)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT).build()).build();

    }

    public static void getCurrentLocation(Context context, final LocationReceivedCallback locationReceivedCallback){
        if (context == null || locationReceivedCallback == null)
            return;
        if (!checkPermission(context)) {
            return;
        }
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(ThreadExecutor.get(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                locationReceivedCallback.onLocationReceived(null);
            }
        });
    }

    public interface LocationReceivedCallback {
        void onLocationReceived(@Nullable Location location);
    }


    public static boolean checkPermission(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
