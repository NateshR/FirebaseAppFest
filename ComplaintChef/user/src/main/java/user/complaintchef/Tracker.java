package user.complaintchef;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import common.complaintcheflib.util.BaseAppCompatActivity;
import common.complaintcheflib.util.ThreadExecutor;

/**
 * Created by nateshrelhan on 6/21/17.
 */

public class Tracker extends BaseAppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.map)
    SupportMapFragment mapFragment;
    private ThreadExecutor threadExecutor;
    private Double officerLat, officerLong, userLat, userLong;
    private GoogleMap myMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker);
        threadExecutor = ThreadExecutor.get();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        LatLng user = new LatLng(userLat, userLong);
        LatLng officer = new LatLng(officerLat, officerLong);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(officer, 11));
        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {

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
                        lineOptions.color(Color.RED);
                        lineOptions.geodesic(true);

                    }
                    myMap.addPolyline(lineOptions);
                }
            });
        }

    }
}
