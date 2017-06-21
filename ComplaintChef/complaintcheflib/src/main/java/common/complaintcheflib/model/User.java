package common.complaintcheflib.model;

import com.google.firebase.database.PropertyName;

import java.util.List;

/**
 * Created by nateshrelhan on 6/21/17.
 */

public class User {
    @PropertyName("latitude")
    public Double latitude;
    @PropertyName("longitude")
    public Double longitude;
    @PropertyName("pending")
    public List<Integer> pendingList;
    @PropertyName("accepted")
    public List<Integer> acceptedList;
    @PropertyName("declined")
    public List<Integer> declinedList;

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

    public List<Integer> getPendingList() {
        return pendingList;
    }

    public void setPendingList(List<Integer> pendingList) {
        this.pendingList = pendingList;
    }

    public List<Integer> getAcceptedList() {
        return acceptedList;
    }

    public void setAcceptedList(List<Integer> acceptedList) {
        this.acceptedList = acceptedList;
    }

    public List<Integer> getDeclinedList() {
        return declinedList;
    }

    public void setDeclinedList(List<Integer> declinedList) {
        this.declinedList = declinedList;
    }
}
