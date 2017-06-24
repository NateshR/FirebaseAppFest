package common.complaintcheflib.util.permissions;

import android.os.Build;
import android.support.annotation.NonNull;

import common.complaintcheflib.view.BaseAppCompatActivity;

/**
 * Created by Simar Arora on 03/01/17.
 */

public class PermissionManager {

    public static void performTaskWithPermission(@NonNull BaseAppCompatActivity activity, final @NonNull PermissionTask task, Permission permission) {
        if (activity.hasPermission(permission))
            task.doTask();
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity.canRequestPermission(permission)) {
                    activity.requestPermission(permission, new PermissionCallback() {
                        @Override
                        public void onGrant() {
                            task.doTask();
                        }

                        @Override
                        public void onDeny() {

                        }
                    });
                }
            }
        }
    }
}
