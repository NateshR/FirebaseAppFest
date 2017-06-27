package user.complaintchef.view;

import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import common.complaintcheflib.model.Category;
import common.complaintcheflib.model.Complaint;
import common.complaintcheflib.util.LocationUtils;
import common.complaintcheflib.util.Sessions;
import common.complaintcheflib.util.permissions.Permission;
import common.complaintcheflib.util.permissions.PermissionDeniedCallback;
import common.complaintcheflib.util.permissions.PermissionManager;
import common.complaintcheflib.util.permissions.PermissionTask;
import common.complaintcheflib.view.BaseAppCompatActivity;
import user.complaintchef.R;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class ComplaintFormDialog extends Dialog {
    private static final String COMPLAINTS = "complaints";
    private EditText phoneET;
    private EditText detailsET;
    private AppCompatButton submitB;
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
        setCanceledOnTouchOutside(false);
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
        } /*else if (phone.length() != 10) {
            phoneET.setError("Enter Valid Phone No.");
            validated = false;
        }*/
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
        PermissionManager.performTaskWithPermission((BaseAppCompatActivity) context, new PermissionTask() {
            @Override
            public void doTask() {
                LocationUtils.getCurrentLocation(ComplaintFormDialog.this.context, new LocationUtils.LocationReceivedCallback() {
                    @Override
                    public void onLocationReceived(@Nullable Location location) {
                        //Send details now
                        if (location == null) return;
                        String complaintId = "-" + System.currentTimeMillis();
                        getmDatabaseReference(complaintId).setValue(new Complaint(complaintId, category.getName(), details, Sessions.loadUsername(ComplaintFormDialog.this.context), category.getId(), location.getLatitude(), location.getLongitude(), phone));
                    }
                });
                dismiss();
            }
        }, Permission.LOCATION(), new PermissionDeniedCallback() {
            @Override
            public void onDeny() {
                Toast.makeText(ComplaintFormDialog.this.context, "Please make sure you location is swtiched on.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private DatabaseReference getmDatabaseReference(String complaintId) {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(COMPLAINTS).child(complaintId);
        mDatabaseReference.keepSynced(true);
        return mDatabaseReference;
    }
}
