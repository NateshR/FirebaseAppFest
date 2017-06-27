package common.complaintcheflib.util.permissions;

import android.Manifest;
import android.support.annotation.DrawableRes;


/**
 * Created by Simar Arora on 03/01/17.
 */

public class Permission {

    private static final String LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_LOCATION = 10101;

    private String permissionName;
    private int requestCode;

    private Permission(String permissionName, int requestCode) {
        this.permissionName = permissionName;
        this.requestCode = requestCode;
    }

    public static Permission LOCATION() {
        return new Permission(LOCATION,
                REQUEST_LOCATION);
    }

    public String getPermissionName() {
        return permissionName;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
