package user.complaintchef.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nateshrelhan on 6/21/17.
 */

public class Sessions {
    public static final String FIREBASE_USER_EXISTS = "_firebase_user_exists";
    private static final String KEY = "IDvalue";

    public static boolean setFirebaseUserExists(Context context, int userExists) {
        if (context == null) {
            return false;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putInt(FIREBASE_USER_EXISTS, userExists);
        return editor.commit();
    }

    public static int getFirebaseUserExists(Context context) {
        if (context == null) {
            return 0;
        }
        SharedPreferences preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preferences.getInt(FIREBASE_USER_EXISTS, 0);
    }
}
