package admin.complaintchef.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    public static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();

    public static final String ACTION = "admin.complaintchef.services.GeofenceTransitionsIntentService.ACTION";

    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    public GeofenceTransitionsIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) return;
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            sendBroadcast(new Intent(ACTION));
        }
    }

}
