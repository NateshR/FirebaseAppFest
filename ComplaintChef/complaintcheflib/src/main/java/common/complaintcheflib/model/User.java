package common.complaintcheflib.model;

import com.google.firebase.database.PropertyName;

/**
 * Created by nateshrelhan on 6/21/17.
 */

public class User {
    @PropertyName("latitude")
    public String latitude;
    @PropertyName("longitude")
    public String longitude;


    public User() {
    }

    public User(Double latitude, Double longitude) {
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
