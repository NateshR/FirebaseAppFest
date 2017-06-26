package user.complaintchef.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.complaintcheflib.firebase.FirebaseDataStoreFactory;
import common.complaintcheflib.model.User;
import common.complaintcheflib.net.Directions;
import common.complaintcheflib.view.BaseAppCompatActivity;
import common.complaintcheflib.util.ThreadExecutor;
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
    private static final long TRIGGER_DELAY_IN_MS = 1000;
    private static int TRIGGER_FETCH = 101;
    private SupportMapFragment mapFragment;
    private ThreadExecutor threadExecutor;
    private GoogleMap myMap;
    private WeakRefHandler weakRefHandler;
    private Bundle gotBundle;
    private FirebaseDataStoreFactory<User> firebaseDataStoreFactory;


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
        weakRefHandler = new WeakRefHandler(this);
        gotBundle = getIntent().getExtras();
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
        initiateHandler();
    }

    @Override
    public void onDataChange(List<User> dataList) {

    }

    @Override
    public void onSingleDataChange(User data) {
        gotBundle.putDouble(KEY_OFFICER_LAT, data.getLastLocationLat());
        gotBundle.putDouble(KEY_OFFICER_LONG, data.getLastLocationLong());
        initiateHandler();
    }

    @Override
    public void onCancelled() {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT);
    }

    private void initiateHandler() {
        Message handlerMessage = new Message();
        handlerMessage.what = TRIGGER_FETCH;
        handlerMessage.setData(gotBundle);
        weakRefHandler.removeMessages(TRIGGER_FETCH);
        weakRefHandler.sendMessageDelayed(handlerMessage, TRIGGER_DELAY_IN_MS);
    }

    private void markAndRoute(LatLng user, LatLng officer) {
        BitmapDescriptor userIcon = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this, R.drawable.ic_user));
        BitmapDescriptor officerIcon = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this, R.drawable.ic_location_pointer));
        if (user != null) {
            MarkerOptions userMarker = new MarkerOptions().position(user)
                    .title("Complaint Location")
                    .icon(userIcon);
            myMap.addMarker(userMarker);
        }
        if (officer != null) {
            MarkerOptions officerMarker = new MarkerOptions().position(officer)
                    .title("Officer Location")
                    .icon(officerIcon);
            myMap.addMarker(officerMarker);
            myMap.animateCamera(CameraUpdateFactory.newLatLng(officer));
        }
        if (user == null && officer == null) return;
        String origin = officer.latitude + "," + officer.longitude;
        String destination = user.latitude + "," + user.longitude;
        String mode = "driving";
        Call<String> call = Directions.findRoute(MyApplication.getAPIService(), origin, destination, mode, getString(R.string.google_api_key));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                threadExecutor.execute(new ParserTask(response.body()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Tracker.this, "Something went wrong!", Toast.LENGTH_SHORT);
            }
        });
    }

    private static final class WeakRefHandler extends Handler {
        private final WeakReference<Tracker> trackerWeakReference;

        WeakRefHandler(Tracker tracker) {
            trackerWeakReference = new WeakReference<>(tracker);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TRIGGER_FETCH && msg.getData() != null) {
                Tracker tracker = trackerWeakReference.get();
                Bundle receivedBundle = msg.getData();
                LatLng userLatLng = null, officerLatLng = null;
                if (receivedBundle.getDouble(KEY_USER_LAT) != 0.0d && receivedBundle.getDouble(KEY_USER_LONG) != 0.0d)
                    userLatLng = new LatLng(receivedBundle.getDouble(KEY_USER_LAT), receivedBundle.getDouble(KEY_USER_LONG));
                if (receivedBundle.getDouble(KEY_OFFICER_LAT) != 0.0d && receivedBundle.getDouble(KEY_OFFICER_LONG) != 0.0d)
                    officerLatLng = new LatLng(receivedBundle.getDouble(KEY_OFFICER_LAT), receivedBundle.getDouble(KEY_OFFICER_LONG));
                if (tracker != null) {
                    tracker.markAndRoute(userLatLng, officerLatLng);
                }
            }
        }
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
                    ArrayList points = null;
                    PolylineOptions lineOptions = null;
                    for (int i = 0; i < routes.size(); i++) {
                        points = new ArrayList();
                        lineOptions = new PolylineOptions();

                        List<HashMap<String, String>> path = routes.get(i);

                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            points.add(position);
                        }

                        lineOptions.addAll(points);
                        lineOptions.width(8);
                        lineOptions.color(ContextCompat.getColor(Tracker.this, R.color.colorAccent));
                        lineOptions.geodesic(true);

                    }
                    if (lineOptions != null)
                        myMap.addPolyline(lineOptions);
                }
            });
        }
    }
}