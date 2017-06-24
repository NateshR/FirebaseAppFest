package common.complaintcheflib.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nateshrelhan on 6/21/17.
 */

public class Sessions {
    public static final String FIREBASE_USER_EXISTS = "_firebase_user_exists";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_TOKEN = "token";
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

    public static boolean setUsername(Context context, String username){
        if (context == null) {
            return false;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_USERNAME, username);
        return editor.commit();
    }

    public static String loadUsername(Context context){
        if (context == null) {
            return null;
        }
        SharedPreferences preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preferences.getString(KEY_USERNAME, null);
    }

    public static boolean setToken(Context context, String token){
        if (context == null) {
            return false;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_TOKEN, token);
        return editor.commit();
    }

    public static String loadToken(Context context){
        if (context == null) {
            return null;
        }
        SharedPreferences preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preferences.getString(KEY_TOKEN, null);
    }
}
