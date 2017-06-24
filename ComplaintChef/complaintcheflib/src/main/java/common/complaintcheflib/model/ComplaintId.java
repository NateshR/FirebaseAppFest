package common.complaintcheflib.model;

import com.google.firebase.database.PropertyName;

/**
 * Created by nateshrelhan on 6/24/17.
 */

public class ComplaintId {
    @PropertyName("complaintId")
    public String complaintId;

    public ComplaintId() {
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }
}
