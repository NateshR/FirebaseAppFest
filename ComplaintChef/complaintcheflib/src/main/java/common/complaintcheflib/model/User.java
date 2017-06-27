package common.complaintcheflib.model;

import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nateshrelhan on 6/21/17.
 */

public class User {
    @PropertyName("uid")
    public String uid;
    @PropertyName("name")
    public String name;
    @PropertyName("isAdmin")
    public Boolean isAdmin;
    @PropertyName("mobileNo")
    public String mobileNo;
    @PropertyName("isAvailable")
    public Boolean isAvailable;
    @PropertyName("lastLocationLat")
    public Double lastLocationLat;
    @PropertyName("lastLocationLong")
    public Double lastLocationLong;
    public List<Category> categoryList = new ArrayList<>();
    @PropertyName("complaints")
    public List<ComplaintId> complaintList = new ArrayList<>();
    @PropertyName("pending")
    public List<ComplaintId> pendingComplaintList = new ArrayList<>();
    @PropertyName("accepted")
    public List<ComplaintId> acceptedComplaintList = new ArrayList<>();
    @PropertyName("declined")
    public List<ComplaintId> declinedComplaintList = new ArrayList<>();

    public User() {
    }

    public User(String uid, String name, Boolean isAdmin, String mobileNo, List<Category> categoryList) {
        this.uid = uid;
        this.name = name;
        this.isAdmin = isAdmin;
        this.mobileNo = mobileNo;
        this.categoryList = categoryList;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Double getLastLocationLat() {
        return lastLocationLat;
    }

    public void setLastLocationLat(Double lastLocationLat) {
        this.lastLocationLat = lastLocationLat;
    }

    public Double getLastLocationLong() {
        return lastLocationLong;
    }

    public void setLastLocationLong(Double lastLocationLong) {
        this.lastLocationLong = lastLocationLong;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<ComplaintId> getComplaintList() {
        return complaintList;
    }

    public void setComplaintList(List<ComplaintId> complaintList) {
        this.complaintList = complaintList;
    }

    public List<ComplaintId> getPendingComplaintList() {
        return pendingComplaintList;
    }

    public void setPendingComplaintList(List<ComplaintId> pendingComplaintList) {
        this.pendingComplaintList = pendingComplaintList;
    }

    public List<ComplaintId> getAcceptedComplaintList() {
        return acceptedComplaintList;
    }

    public void setAcceptedComplaintList(List<ComplaintId> acceptedComplaintList) {
        this.acceptedComplaintList = acceptedComplaintList;
    }

    public List<ComplaintId> getDeclinedComplaintList() {
        return declinedComplaintList;
    }

    public void setDeclinedComplaintList(List<ComplaintId> declinedComplaintList) {
        this.declinedComplaintList = declinedComplaintList;
    }
}
