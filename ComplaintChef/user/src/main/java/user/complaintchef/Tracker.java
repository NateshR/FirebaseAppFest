package user.complaintchef;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import common.complaintcheflib.util.BaseAppCompatActivity;

/**
 * Created by nateshrelhan on 6/21/17.
 */

public class Tracker extends BaseAppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.map)
    SupportMapFragment mapFragment;
    private Double officerLat, officerLong, userLat, userLong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng user = new LatLng(userLat, userLong);
        LatLng officer = new LatLng(officerLat, officerLong);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(officer, 11));


    }
}
