package common.complaintcheflib.model;

import com.google.firebase.database.PropertyName;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class Complaint {
    public static final String STATUS_PENDING = "pending", STATUS_ACCEPTED = "accepted", STATUS_DECLINED = "declined";
    @PropertyName("complaintId")
    public String complaintId;
    @PropertyName("title")
    public String title;
    @PropertyName("description")
    public String description;
    @PropertyName("status")
    public String status;
    @PropertyName("admin")
    public String admin;
    @PropertyName("user")
    public String user;
    @PropertyName("category")
    public String category;
    @PropertyName("locationLat")
    public Double locationLat;
    @PropertyName("locationLong")
    public Double locationLong;
    @PropertyName("mobileNo")
    public String mobileNo;

    public Complaint() {
    }

    //When user registers a complaint
    public Complaint(String complaintId, String title, String description, String user, String category, Double locationLat, Double locationLong,String mobileNo) {
        this.complaintId = complaintId;
        this.title = title;
        this.description = description;
        this.user = user;
        this.category = category;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.mobileNo = mobileNo;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(Double locationLong) {
        this.locationLong = locationLong;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
