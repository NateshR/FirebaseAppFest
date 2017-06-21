package user.complaintchef;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.complaintcheflib.util.BaseAppCompatActivity;
import common.complaintcheflib.util.ThreadExecutor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import user.complaintchef.core.MyApplication;
import user.complaintchef.net.APIService;

/**
 * Created by nateshrelhan on 6/21/17.
 */

public class Tracker extends BaseAppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private ThreadExecutor threadExecutor;
    private Double officerLat, officerLong, userLat, userLong;
    private GoogleMap myMap;

    private APIService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        threadExecutor = ThreadExecutor.get();
        apiService = MyApplication.getAPIService();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        final LatLng user = new LatLng(28.621899, 77.087838);
        final LatLng officer = new LatLng(22.244197, 68.968456);
        BitmapDescriptor userIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pointer);
        BitmapDescriptor officerIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pointer);
        MarkerOptions userMarker = new MarkerOptions().position(user)
                .title("Complaint Location")
                .icon(userIcon);
        MarkerOptions officerMarker = new MarkerOptions().position(officer)
                .title("Officer Location")
                .icon(officerIcon);
        myMap.addMarker(officerMarker);
        myMap.addMarker(userMarker);
        myMap.animateCamera(CameraUpdateFactory.newLatLng(officer));
        String origin = "origin=" + officer.latitude + "," + officer.longitude;
        String destination = "destination=" + user.latitude + "," + user.longitude;
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
                        lineOptions.width(12);
                        lineOptions.color(Color.GRAY);
                        lineOptions.geodesic(true);

                    }
                    if (lineOptions != null)
                        myMap.addPolyline(lineOptions);
                }
            });
        }

    }
}
