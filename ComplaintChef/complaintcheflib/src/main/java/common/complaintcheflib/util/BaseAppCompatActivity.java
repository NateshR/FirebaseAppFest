package common.complaintcheflib.util;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import common.complaintcheflib.util.permissions.Permission;
import common.complaintcheflib.util.permissions.PermissionCallback;
import common.complaintcheflib.util.permissions.SessionPermission;

/**
 * Created by natesh on 30/12/15.
 */
public class BaseAppCompatActivity extends AppCompatActivity {
    private static final String TAG = BaseAppCompatActivity.class.getSimpleName();

    private SparseArray<PermissionCallback> permissionCallbackSparseArray = new SparseArray<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public ActionBar setUpActionBar(String title) {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        return actionBar;
    }

    public boolean hasPermission(Permission permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        } else {
            return checkSelfPermission(permission.getPermissionName()) == PackageManager.PERMISSION_GRANTED;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission(Permission permission, PermissionCallback permissionCallback) {
        permissionCallbackSparseArray.put(permission.getRequestCode(), permissionCallback);
        SessionPermission.setPermissionRequest(this, permission);
        requestPermissions(new String[]{permission.getPermissionName()}, permission.getRequestCode());
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean canRequestPermission(Permission permission) {
        return !wasRequestedBefore(permission) || shouldShowRequestPermissionRationale(permission.getPermissionName());
    }

    private boolean wasRequestedBefore(Permission permission) {
        return SessionPermission.wasPermissionRequestedBefore(this, permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionCallback callback = permissionCallbackSparseArray.get(requestCode, null);
        if (callback == null) {
            return;
        }
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callback.onGrant();
            } else {
                callback.onDeny();
            }
        } else {
            callback.onDeny();
        }
    }
}
