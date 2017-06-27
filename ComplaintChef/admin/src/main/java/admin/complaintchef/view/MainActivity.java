package admin.complaintchef.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import admin.complaintchef.R;
import admin.complaintchef.services.LocationTrackerService;
import common.complaintcheflib.firebase.FirebaseDataStoreFactory;
import common.complaintcheflib.model.Complaint;
import common.complaintcheflib.model.ComplaintId;
import common.complaintcheflib.model.User;
import common.complaintcheflib.util.ListClickCallback;
import common.complaintcheflib.util.LocationUtils;
import common.complaintcheflib.util.Sessions;
import common.complaintcheflib.util.permissions.Permission;
import common.complaintcheflib.util.permissions.PermissionDeniedCallback;
import common.complaintcheflib.util.permissions.PermissionManager;
import common.complaintcheflib.util.permissions.PermissionTask;
import common.complaintcheflib.view.BaseAppCompatActivity;
import common.complaintcheflib.view.ListFragment;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class MainActivity extends BaseAppCompatActivity implements ListClickCallback, DialogInterface.OnClickListener, FirebaseDataStoreFactory.DataListCallBack<User> {

    public static final String KEY_USER = "users", KEY_COMPLAINTS = "complaints", KEY_ADMIN = "admin", KEY_STATUS = "status", KEY_PENDING_LIST = "pending", KEY_ACCEPTED_LIST = "accepted", KEY_DECLINED_LIST = "declined";
    private static final int REQUEST_CHECK_SETTINGS = 101;
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private User currentUser;
    private Complaint selectedComplaint;
    private int taskToDo = 0;
    private FirebaseDataStoreFactory<User> firebaseDataStoreFactoryUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = (TabLayout) findViewById(R.id.tl_main);
        viewPager = (ViewPager) findViewById(R.id.vp_main);
        tableLayout.setupWithViewPager(viewPager);
        PermissionManager.performTaskWithPermission(this, new PermissionTask() {
            @Override
            public void doTask() {
                viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
                checkLocationSettingsAndStartService();
            }
        }, Permission.LOCATION(), new PermissionDeniedCallback() {
            @Override
            public void onDeny() {
                finish();
            }
        });
        injectFirebaseDataStoreFactoryUser();
    }

    private void injectFirebaseDataStoreFactoryUser() {
        firebaseDataStoreFactoryUser = new FirebaseDataStoreFactory<>();
        firebaseDataStoreFactoryUser.data(FirebaseDataStoreFactory.ListenerType.NODE, User.class, getUserDatabaseReference(), this);
    }

    private void checkLocationSettingsAndStartService() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(LocationUtils.createLocationRequest());
        SettingsClient client = LocationServices.getSettingsClient(MainActivity.this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startService(new Intent(MainActivity.this, LocationTrackerService.class));
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(MainActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                checkLocationSettingsAndStartService();
                break;
        }
    }

    @Override
    public void itemClicked(Complaint complaint) {
        this.selectedComplaint = complaint;
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        switch (complaint.getStatus()) {
            case Complaint.STATUS_PENDING:
                alertDialog.setTitle("Update the status of complaint!");
                alertDialog.setMessage(complaint.getDescription());
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Accept", this);
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel", this);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Decline", this);
                break;
            case Complaint.STATUS_ACCEPTED:
                alertDialog.setTitle("Do you want to decline this complaint?");
                alertDialog.setMessage(complaint.getDescription());
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel", this);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Decline", this);
                break;
            case Complaint.STATUS_DECLINED:
                alertDialog.setTitle("Do you want to accept this complaint?");
                alertDialog.setMessage(complaint.getDescription());
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Accept", this);
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel", this);
                break;
        }
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (this.currentUser == null) {
            taskToDo = i;
            injectFirebaseDataStoreFactoryUser();
            return;
        }
        this.selectedComplaint.setAdmin(this.currentUser.getUid());
        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:
                this.selectedComplaint.setStatus(Complaint.STATUS_ACCEPTED);
                removeFromPendingComplaintIdList();
                removeFromDeclinedComplaintIdList();
                addToAcceptedComplaintIdList();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                this.selectedComplaint.setStatus(Complaint.STATUS_DECLINED);
                removeFromPendingComplaintIdList();
                removeFromAcceptedComplaintIdList();
                addToDeclinedComplaintIdList();
                break;
            default:
                break;
        }
        updateComplaint();
        this.selectedComplaint = null;
    }

    private void updateComplaint() {
        getComplaintDatabaseReference().child(this.selectedComplaint.getComplaintId()).child(KEY_ADMIN).setValue(this.selectedComplaint.getAdmin());
        getComplaintDatabaseReference().child(this.selectedComplaint.getComplaintId()).child(KEY_STATUS).setValue(this.selectedComplaint.getStatus());
    }

    private void removeFromPendingComplaintIdList() {
        List<ComplaintId> pendingComplaintIdList = this.currentUser.getPendingComplaintList();
        if (pendingComplaintIdList != null) {
            for (ComplaintId complaintId : pendingComplaintIdList) {
                if (this.selectedComplaint.getComplaintId().equals(complaintId.getComplaintId())) {
                    pendingComplaintIdList.remove(complaintId);
                    break;
                }
            }
            getUserDatabaseReference().child(KEY_PENDING_LIST).setValue(pendingComplaintIdList);
        }
    }

    private void removeFromDeclinedComplaintIdList() {
        List<ComplaintId> declinedComplaintList = this.currentUser.getDeclinedComplaintList();
        if (declinedComplaintList != null) {
            for (ComplaintId complaintId : declinedComplaintList) {
                if (this.selectedComplaint.getComplaintId().equals(complaintId.getComplaintId())) {
                    declinedComplaintList.remove(complaintId);
                    break;
                }
            }
            getUserDatabaseReference().child(KEY_DECLINED_LIST).setValue(declinedComplaintList);
        }
    }

    private void removeFromAcceptedComplaintIdList() {
        List<ComplaintId> acceptedComplaintList = this.currentUser.getAcceptedComplaintList();
        if (acceptedComplaintList != null) {
            for (ComplaintId complaintId : acceptedComplaintList) {
                if (this.selectedComplaint.getComplaintId().equals(complaintId.getComplaintId())) {
                    acceptedComplaintList.remove(complaintId);
                    break;
                }
            }
            getUserDatabaseReference().child(KEY_ACCEPTED_LIST).setValue(acceptedComplaintList);
        }
    }

    private void addToDeclinedComplaintIdList() {
        List<ComplaintId> declinedComplaintList = this.currentUser.getDeclinedComplaintList();
        declinedComplaintList.add(new ComplaintId(this.selectedComplaint.getComplaintId()));
        getUserDatabaseReference().child(KEY_DECLINED_LIST).setValue(declinedComplaintList);
    }

    private void addToAcceptedComplaintIdList() {
        List<ComplaintId> acceptedComplaintList = this.currentUser.getAcceptedComplaintList();
        acceptedComplaintList.add(new ComplaintId(this.selectedComplaint.getComplaintId()));
        getUserDatabaseReference().child(KEY_ACCEPTED_LIST).setValue(acceptedComplaintList);
    }

    private DatabaseReference getUserDatabaseReference() {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(KEY_USER).child(Sessions.loadUsername(this));
        mDatabaseReference.keepSynced(true);
        return mDatabaseReference;
    }

    private DatabaseReference getComplaintDatabaseReference() {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(KEY_COMPLAINTS);
        mDatabaseReference.keepSynced(true);
        return mDatabaseReference;
    }

    @Override
    public void onDataChange(List<User> dataList) {

    }

    @Override
    public void onSingleDataChange(User data) {
        if (data == null) return;
        this.currentUser = data;
        if (taskToDo != 0)
            onClick(null, taskToDo);
    }

    @Override
    public void onCancelled() {

    }

    private class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ListFragment.newInstance(ListFragment.LIST_TYPE.PENDING, ListFragment.FROM_ACTIVITY.ADMIN);
                case 1:
                    return ListFragment.newInstance(ListFragment.LIST_TYPE.ACCEPTED, ListFragment.FROM_ACTIVITY.ADMIN);
                case 2:
                    return ListFragment.newInstance(ListFragment.LIST_TYPE.DECLINED, ListFragment.FROM_ACTIVITY.ADMIN);
                default:
                    throw new RuntimeException("Type Not Supported");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "On-GOING";
                case 1:
                    return "ACCEPTED";
                case 2:
                    return "REJECTED";
            }
            return null;
        }
    }
}
