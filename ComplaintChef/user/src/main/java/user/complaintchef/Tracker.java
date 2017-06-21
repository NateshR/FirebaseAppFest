package user.complaintchef;

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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.complaintcheflib.util.BaseAppCompatActivity;
import common.complaintcheflib.util.ThreadExecutor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import user.complaintchef.core.MyApplication;
import common.complaintcheflib.net.APIService;

/**
 * Created by nateshrelhan on 6/21/17.
 */

public class Tracker extends BaseAppCompatActivity implements OnMapReadyCallback {

    private static final long TRIGGER_DELAY_IN_MS = 1000;
    private static int TRIGGER_FETCH = 101;
    private SupportMapFragment mapFragment;
    private ThreadExecutor threadExecutor;
    private Double officerLat, officerLong, userLat, userLong;
    private GoogleMap myMap;
    private APIService apiService;
    private WeakRefHandler weakRefHandler;
    private Bundle gotBundle;


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
        apiService = MyApplication.getAPIService();
        weakRefHandler = new WeakRefHandler(this);
        gotBundle = getIntent().getExtras();
        if (gotBundle == null)
            gotBundle = new Bundle();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        Message handlerMessage = new Message();
        handlerMessage.what = TRIGGER_FETCH;
        gotBundle.putDouble("user_lat", 28.621899);
        gotBundle.putDouble("user_long", 77.087838);
        gotBundle.putDouble("officer_lat", 22.244197);
        gotBundle.putDouble("officer_long", 68.968456);
        handlerMessage.setData(gotBundle);
        weakRefHandler.removeMessages(TRIGGER_FETCH);
        weakRefHandler.sendMessageDelayed(handlerMessage, TRIGGER_DELAY_IN_MS);
    }

    private void markAndRoute(LatLng user, LatLng officer) {
        BitmapDescriptor userIcon = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this, R.drawable.ic_user));
        BitmapDescriptor officerIcon = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this, R.drawable.ic_location_pointer));
        MarkerOptions userMarker = new MarkerOptions().position(user)
                .title("Complaint Location")
                .icon(userIcon);
        MarkerOptions officerMarker = new MarkerOptions().position(officer)
                .title("Officer Location")
                .icon(officerIcon);
        myMap.addMarker(officerMarker);
        myMap.addMarker(userMarker);
        myMap.animateCamera(CameraUpdateFactory.newLatLng(officer));
        String origin = officer.latitude + "," + officer.longitude;
        String destination = user.latitude + "," + user.longitude;
        String mode = "driving";
        Call<String> call = apiService.directionsApi(origin, destination, mode, getString(R.string.google_api_key));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                threadExecutor.execute(new ParserTask(response.body()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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
                LatLng userLatLng = new LatLng(receivedBundle.getDouble("user_lat"), receivedBundle.getDouble("user_long"));
                LatLng officerLatLng = new LatLng(receivedBundle.getDouble("officer_lat"), receivedBundle.getDouble("officer_long"));
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
