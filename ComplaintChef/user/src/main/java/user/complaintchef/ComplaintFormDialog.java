package user.complaintchef;

import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import common.complaintcheflib.model.Category;
import common.complaintcheflib.model.FileComplaint;
import common.complaintcheflib.util.LocationUtils;
import common.complaintcheflib.util.Sessions;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class ComplaintFormDialog extends Dialog {
    private static final String COMPLAINTS = "complaints";
    private static DatabaseReference mDatabaseReference;
    EditText phoneET;
    EditText detailsET;
    AppCompatButton submitB;
    private Category category;
    private Context context;

    public ComplaintFormDialog(@NonNull Context context, Category category) {
        super(context);
        setContentView(R.layout.activity_complaint_form);
        this.context = context;
        phoneET = (EditText) findViewById(R.id.et_phone);
        detailsET = (EditText) findViewById(R.id.et_details);
        submitB = (AppCompatButton) findViewById(R.id.b_submit);
        this.category = category;
        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClicked();
            }
        });
    }

    private void onSubmitClicked() {
        boolean validated = true;
        String phone = phoneET.getText().toString();
        if (phone.isEmpty()) {
            phoneET.setError("Enter Phone No.");
            validated = false;
        } else if (phone.length() != 10) {
            phoneET.setError("Enter Valid Phone No.");
            validated = false;
        }
        String details = detailsET.getText().toString();
        if (details.isEmpty()) {
            detailsET.setError("Enter Details");
            validated = false;
        }

        if (validated) {
            registerComplaint(phone, details);
        }
    }

    private void registerComplaint(final String phone, final String details) {
        LocationUtils.getCurrentLocation(this.context, new LocationUtils.LocationReceivedCallback() {
            @Override
            public void onLocationReceived(@Nullable Location location) {
                //Send details now
                FileComplaint fileComplaint = new FileComplaint(Sessions.loadUsername(ComplaintFormDialog.this.context), "", location.getLatitude(), location.getLongitude(), ComplaintFormDialog.this.category.getCategoryId(), phone, details);
                getmDatabaseReference().setValue(fileComplaint);
            }
        });
        dismiss();
    }

    private DatabaseReference getmDatabaseReference() {
        if (mDatabaseReference == null) {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(COMPLAINTS).child("-" + System.currentTimeMillis());
            mDatabaseReference.keepSynced(true);
        }
        return mDatabaseReference;
    }
}
