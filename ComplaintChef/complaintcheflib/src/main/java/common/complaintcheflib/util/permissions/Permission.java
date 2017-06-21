package common.complaintcheflib.util.permissions;

import android.Manifest;
import android.support.annotation.DrawableRes;

import com.curofy.R;
import com.curofy.data.util.RemoteConfigHelper;

/**
 * Created by Simar Arora on 03/01/17.
 */

public class Permission {

    private static final String CAMERA = Manifest.permission.CAMERA;
    private static final int REQUEST_CAMERA = 10101;
    @DrawableRes
    private static final int IMAGE_CAMERA = R.drawable.camera_permission;

    private static final String STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int REQUEST_STORAGE = 10102;
    @DrawableRes
    private static final int IMAGE_STORAGE = R.drawable.storage_permission;

    private static final String SMS = Manifest.permission.READ_SMS;
    private static final int REQUEST_SMS = 10103;
    @DrawableRes
    private static final int IMAGE_SMS = R.drawable.sms_permission;

    private static final String CONTACTS = Manifest.permission.READ_CONTACTS;
    private static final int REQUEST_CONTACTS = 10104;
    @DrawableRes
    private static final int IMAGE_CONTACTS = R.drawable.contacts_permission;

    private static final String PHONE = Manifest.permission.CALL_PHONE;
    private static final int REQUEST_PHONE = 10105;
    @DrawableRes
    private static final int IMAGE_PHONE = R.drawable.phone_permission;

    private static final String PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    private static final int REQUEST_PHONE_STATE = 10106;


    private String permissionName;
    private int requestCode;

    private String preDialogTitle;
    private String preDialogMessage;
    private String requestAginDialogTitle;
    private String requestAgainDialogMessage;
    private String deniedDialogTitle;
    private String deniedDialogMessage;
    @DrawableRes
    private int dialogImage;

    private Permission(String permissionName, int requestCode, String preDialogTitle, String preDialogMessage, String requestAginDialogTitle, String requestAgainDialogMessage, String deniedDialogTitle, String deniedDialogMessage, @DrawableRes int dialogImage) {
        this.permissionName = permissionName;
        this.requestCode = requestCode;
        this.preDialogTitle = preDialogTitle;
        this.preDialogMessage = preDialogMessage;
        this.requestAginDialogTitle = requestAginDialogTitle;
        this.requestAgainDialogMessage = requestAgainDialogMessage;
        this.deniedDialogTitle = deniedDialogTitle;
        this.deniedDialogMessage = deniedDialogMessage;
        this.dialogImage = dialogImage;
    }

    public static Permission CAMERA() {
        return new Permission(CAMERA,
                REQUEST_CAMERA,
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_CAMERA_PERMISSION_TITLE_1),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_CAMERA_PERMISSION_MESSAGE_1),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_CAMERA_PERMISSION_TITLE_2),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_CAMERA_PERMISSION_MESSAGE_2),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_CAMERA_PERMISSION_TITLE_3),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_CAMERA_PERMISSION_MESSAGE_3),
                IMAGE_CAMERA);
    }

    public static Permission STORAGE() {
        return new Permission(STORAGE,
                REQUEST_STORAGE,
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_STORAGE_PERMISSION_TITLE_1),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_STORAGE_PERMISSION_MESSAGE_1),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_STORAGE_PERMISSION_TITLE_2),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_STORAGE_PERMISSION_MESSAGE_2),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_STORAGE_PERMISSION_TITLE_3),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_STORAGE_PERMISSION_MESSAGE_3),
                IMAGE_STORAGE);
    }

    public static Permission SMS() {
        return new Permission(SMS,
                REQUEST_SMS,
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_SMS_PERMISSION_TITLE_1),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_SMS_PERMISSION_MESSAGE_1),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_SMS_PERMISSION_TITLE_2),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_SMS_PERMISSION_MESSAGE_2),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_SMS_PERMISSION_TITLE_3),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_SMS_PERMISSION_MESSAGE_3),
                IMAGE_SMS);
    }

    public static Permission CONTACTS() {
        return new Permission(CONTACTS,
                REQUEST_CONTACTS,
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_CONTACTS_PERMISSION_TITLE_1),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_CONTACTS_PERMISSION_MESSAGE_1),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_CONTACTS_PERMISSION_TITLE_2),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_CONTACTS_PERMISSION_MESSAGE_2),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_CONTACTS_PERMISSION_TITLE_3),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_CONTACTS_PERMISSION_MESSAGE_3),
                IMAGE_CONTACTS);
    }

    public static Permission PHONE() {
        return new Permission(PHONE,
                REQUEST_PHONE,
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_PHONE_PERMISSION_TITLE_1),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_PHONE_PERMISSION_MESSAGE_1),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_PHONE_PERMISSION_TITLE_2),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_PHONE_PERMISSION_MESSAGE_2),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_PHONE_PERMISSION_TITLE_3),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_PHONE_PERMISSION_MESSAGE_3),
                IMAGE_PHONE);
    }

    public static Permission PHONE_STATE() {
        return new Permission(PHONE_STATE,
                REQUEST_PHONE_STATE,
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_PHONE_PERMISSION_TITLE_1),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_PHONE_PERMISSION_MESSAGE_1),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_PHONE_PERMISSION_TITLE_2),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_PHONE_PERMISSION_MESSAGE_2),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_PHONE_PERMISSION_TITLE_3),
                RemoteConfigHelper.getString(RemoteConfigHelper.KEY_PHONE_PERMISSION_MESSAGE_3),
                IMAGE_PHONE);
    }

    public String getPermissionName() {
        return permissionName;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String getPreDialogTitle() {
        return preDialogTitle;
    }

    public String getPreDialogMessage() {
        return preDialogMessage;
    }

    public String getRequestAginDialogTitle() {
        return requestAginDialogTitle;
    }

    public String getRequestAgainDialogMessage() {
        return requestAgainDialogMessage;
    }

    public String getDeniedDialogTitle() {
        return deniedDialogTitle;
    }

    public String getDeniedDialogMessage() {
        return deniedDialogMessage;
    }

    public int getDialogImage() {
        return dialogImage;
    }
}
