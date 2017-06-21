package common.complaintcheflib.model;

import com.google.firebase.database.PropertyName;

/**
 * Created by nateshrelhan on 6/21/17.
 */

public class User {
    @PropertyName("latitude")
    public Double latitude;
    @PropertyName("longitude")
    public Double longitude;


    public User() {
    }

    public User(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
