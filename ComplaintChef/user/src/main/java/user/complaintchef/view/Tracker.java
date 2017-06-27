package user.complaintchef.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.complaintcheflib.firebase.FirebaseDataStoreFactory;
import common.complaintcheflib.model.User;
import common.complaintcheflib.net.Directions;
import common.complaintcheflib.util.ThreadExecutor;
import common.complaintcheflib.view.BaseAppCompatActivity;
import common.complaintcheflib.view.ListFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import user.complaintchef.R;
import user.complaintchef.core.MyApplication;
import user.complaintchef.core.PolyParser;

/**
 * Created by nateshrelhan on 6/21/17.
 */

public class Tracker extends BaseAppCompatActivity implements OnMapReadyCallback, FirebaseDataStoreFactory.DataListCallBack<User> {

    public static final String KEY_ADMIN_ID = "KEY_ADMIN_ID", KEY_USER_LAT = "user_lat", KEY_USER_LONG = "user_long", KEY_OFFICER_LAT = "officer_lat", KEY_OFFICER_LONG = "officer_long";
    boolean tIsLessThan1 = true;
    private SupportMapFragment mapFragment;
    private ThreadExecutor threadExecutor;
    private GoogleMap myMap;
    private FirebaseDataStoreFactory<User> firebaseDataStoreFactory;
    private ArrayList<LatLng> previousPointsList = null;
    private LatLng user, previousOfficer;
    private boolean markAdded = false;
    private Marker officerMaker;

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        threadExecutor = ThreadExecutor.get();
        Bundle gotBundle = getIntent().getExtras();
        try {
            user = new LatLng(gotBundle.getDouble(KEY_USER_LAT), gotBundle.getDouble(KEY_USER_LONG));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        firebaseDataStoreFactory = new FirebaseDataStoreFactory<>();
        firebaseDataStoreFactory.data(FirebaseDataStoreFactory.ListenerType.NODE, User.class, getmDatabaseReference(gotBundle.getString(KEY_ADMIN_ID)), this);
        mapFragment.getMapAsync(this);
    }

    private DatabaseReference getmDatabaseReference(String adminId) {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(ListFragment.KEY_USER).child(adminId);
        mDatabaseReference.keepSynced(true);
        return mDatabaseReference;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
    }

    @Override
    public void onDataChange(List<User> dataList) {

    }

    @Override
    public void onSingleDataChange(User data) {
        LatLng currentOfficer = new LatLng(data.getLastLocationLat(), data.getLastLocationLong());
        if (!markAdded)
            mark(currentOfficer);
        else {
            route(currentOfficer);
            if (previousOfficer != null) {
                float[] distance = new float[1];
                Location.distanceBetween(previousOfficer.latitude, previousOfficer.longitude, currentOfficer.latitude, currentOfficer.longitude, distance);
                // distance[0] is now the distance between these lat/lons in meters
                if (distance[0] > 1.0) {
                    moveOfficerToNewPosition(previousOfficer, currentOfficer);
                }
            }
            previousOfficer = new LatLng(data.getLastLocationLat(), data.getLastLocationLong());
        }

    }

    @Override
    public void onCancelled() {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    private void mark(final LatLng officer) {
        BitmapDescriptor userIcon = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this, R.drawable.ic_location_pointer));
        BitmapDescriptor officerIcon = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this, R.drawable.ic_user));

        MarkerOptions userMarker = new MarkerOptions().position(user)
                .title("Complaint Location")
                .icon(userIcon);
        myMap.addMarker(userMarker);

        if (officer != null) {
            markAdded = true;
            MarkerOptions officerMarker = new MarkerOptions().position(officer)
                    .title("Officer Location")
                    .icon(officerIcon);
            officerMaker = myMap.addMarker(officerMarker);
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(officer, 16));

            route(officer);
        }
    }

    private void route(final LatLng officer) {
        String origin = officer.latitude + "," + officer.longitude;
        String destination = user.latitude + "," + user.longitude;
        String mode = "driving";
        Call<String> call = Directions.findRoute(MyApplication.getAPIService(), origin, destination, mode, getString(R.string.google_api_key_manual));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                threadExecutor.execute(new ParserTask(response.body()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Tracker.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveOfficerToNewPosition(final LatLng officer, final LatLng updatedOfficer) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 20000;
        final boolean hideMarker = false;
        handler.post(new Runnable() {
            long elapsed;
            float t, v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                Log.d("Tracker", "Start - " + start);
                elapsed = SystemClock.uptimeMillis() - start;
                Log.d("Tracker", "Elapsed - " + elapsed);
                Log.d("Tracker", "divide - " + elapsed
                        / durationInMs);
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation((float) elapsed
                        / durationInMs);
                Log.d("Tracker", "T - " + t + "___" + v);
                LatLng currentPosition = new LatLng(
                        officer.latitude * (1 - v) + updatedOfficer.latitude * v,
                        officer.longitude * (1 - v) + updatedOfficer.longitude * v);
                Log.d("Tracker", "Pos - " + currentPosition.latitude + "," + currentPosition.longitude);
                officerMaker.setPosition(currentPosition);

                // Repeat till progress is complete.
                if (t < 1)
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
            }
        });
    }

    private class ParserTask implements Runnable {
        String result;
        List<List<HashMap<String, String>>> routes = null;

        public ParserTask(String result) {
            this.result = result;
        }

        @Override
        public void run() {
            JSONObject jObject;
            try {
                jObject = new JSONObject(result);
                PolyParser parser = new PolyParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Tracker.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PolylineOptions lineOptions = null;
                    for (int i = 0; i < routes.size(); i++) {
                        previousPointsList = new ArrayList();
                        lineOptions = new PolylineOptions();

                        List<HashMap<String, String>> path = routes.get(i);

                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            previousPointsList.add(position);
                        }

                        lineOptions.addAll(previousPointsList);
                        lineOptions.width(8);
                        lineOptions.color(ContextCompat.getColor(Tracker.this, R.color.colorAccent));
                        lineOptions.geodesic(true);

                    }
                    if (lineOptions != null) {
                        myMap.addPolyline(lineOptions);
                    }
                }
            });
        }
    }
}
