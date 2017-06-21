package common.complaintcheflib.util.permissions;

import android.Manifest;
import android.support.annotation.DrawableRes;


/**
 * Created by Simar Arora on 03/01/17.
 */

public class Permission {

    private static final String CAMERA = Manifest.permission.CAMERA;
    private static final int REQUEST_CAMERA = 10101;

    private static final String STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int REQUEST_STORAGE = 10102;

    private static final String SMS = Manifest.permission.READ_SMS;
    private static final int REQUEST_SMS = 10103;


    private static final String CONTACTS = Manifest.permission.READ_CONTACTS;
    private static final int REQUEST_CONTACTS = 10104;

    private static final String PHONE = Manifest.permission.CALL_PHONE;
    private static final int REQUEST_PHONE = 10105;

    private static final String PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    private static final int REQUEST_PHONE_STATE = 10106;


    private String permissionName;
    private int requestCode;


    private Permission(String permissionName, int requestCode) {
        this.permissionName = permissionName;
        this.requestCode = requestCode;
    }

    public static Permission CAMERA() {
        return new Permission(CAMERA,
                REQUEST_CAMERA);
    }

    public static Permission STORAGE() {
        return new Permission(STORAGE,
                REQUEST_STORAGE);
    }

    public static Permission SMS() {
        return new Permission(SMS,
                REQUEST_SMS);
    }

    public static Permission CONTACTS() {
        return new Permission(CONTACTS,
                REQUEST_CONTACTS);
    }

    public static Permission PHONE() {
        return new Permission(PHONE,
                REQUEST_PHONE);
    }

    public static Permission PHONE_STATE() {
        return new Permission(PHONE_STATE,
                REQUEST_PHONE_STATE);
    }

    public String getPermissionName() {
        return permissionName;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
