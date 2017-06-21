package admin.complaintchef.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

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
    protected void onHandleIntent(@Nullable Intent intent) {

    }

}
