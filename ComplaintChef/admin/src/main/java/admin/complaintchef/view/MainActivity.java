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

import admin.complaintchef.R;
import admin.complaintchef.services.LocationTrackerService;
import common.complaintcheflib.model.Complaint;
import common.complaintcheflib.util.ListClickCallback;
import common.complaintcheflib.util.LocationUtils;
import common.complaintcheflib.util.permissions.Permission;
import common.complaintcheflib.util.permissions.PermissionDeniedCallback;
import common.complaintcheflib.util.permissions.PermissionManager;
import common.complaintcheflib.util.permissions.PermissionTask;
import common.complaintcheflib.view.BaseAppCompatActivity;
import common.complaintcheflib.view.ListFragment;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class MainActivity extends BaseAppCompatActivity implements ListClickCallback, DialogInterface.OnClickListener {

    private static final int REQUEST_CHECK_SETTINGS = 101, ACCEPT_CODE = 0, DECLINE_CODE = 1, DISMISS_CODE = 2;
    TabLayout tableLayout;
    ViewPager viewPager;

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
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Update the status of complaint");
        alertDialog.setMessage(complaint.getDescription());
        alertDialog.setButton(ACCEPT_CODE, "Accept", this);
        alertDialog.setButton(DECLINE_CODE, "Decline", this);
        alertDialog.setButton(DISMISS_CODE, "Cancel", this);
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case ACCEPT_CODE:
                break;
            case DECLINE_CODE:
                break;
            case DISMISS_CODE:
                break;
        }
    }

    private class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ListFragment.newInstance(ListFragment.LIST_TYPE.PENDING);
                case 1:
                    return ListFragment.newInstance(ListFragment.LIST_TYPE.ACCEPTED);
                case 2:
                    return ListFragment.newInstance(ListFragment.LIST_TYPE.DECLINED);
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
