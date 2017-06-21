package common.complaintcheflib.util.permissions;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Simar Arora on 04/01/17.
 */

public class SessionPermission {
    private static final String KEY = "curofy_permissions";

    public static void setPermissionRequest(Context context, Permission permission) {
        SharedPreferences preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(permission.getPermissionName(), true);
        editor.apply();
    }

    public static boolean wasPermissionRequestedBefore(Context context, Permission permission) {
        SharedPreferences preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(permission.getPermissionName(), false);
    }
}
