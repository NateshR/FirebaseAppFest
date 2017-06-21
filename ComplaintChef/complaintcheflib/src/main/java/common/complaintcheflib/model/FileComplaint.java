package common.complaintcheflib.model;

import com.google.firebase.database.PropertyName;

/**
 * Created by nateshrelhan on 6/21/17.
 */

public class FileComplaint {
    @PropertyName("complained_by")
    public String complainedBy;
    @PropertyName("accepted_by")
    public String acceptedBy;
    @PropertyName("latitude")
    public Double latitude;
    @PropertyName("longitude")
    public Double longitude;
    @PropertyName("category_id")
    public String categoryId;
    @PropertyName("phone_number")
    public String phoneNumber;
    @PropertyName("details")
    public String details;

    public FileComplaint(String complainedBy, String acceptedBy, Double latitude, Double longitude, String categoryId, String phoneNumber, String details) {
        this.complainedBy = complainedBy;
        this.acceptedBy = acceptedBy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.categoryId = categoryId;
        this.phoneNumber = phoneNumber;
        this.details = details;
    }

    public String getComplainedBy() {
        return complainedBy;
    }

    public void setComplainedBy(String complainedBy) {
        this.complainedBy = complainedBy;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
