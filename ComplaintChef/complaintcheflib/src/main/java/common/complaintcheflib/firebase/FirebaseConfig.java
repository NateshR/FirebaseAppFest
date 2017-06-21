package common.complaintcheflib.firebase;

import android.content.Context;
import android.database.Observable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import common.complaintcheflib.util.Sessions;


/**
 * Created by nateshrelhan on 16/09/16.
 */
public class FirebaseConfig {

    private String TAG = FirebaseConfig.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context context;

    public FirebaseConfig(@NonNull Context context) {
        this.context = context;
    }

    /**
     * @return {@link Observable} of common.complaintcheflib.firebase auth listener.
     */
    public void firebaseSignInStatusListenerObservable() {
        Log.d(TAG, TAG + " init auth");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, TAG + " onAuthStateChanged:signed_in:" + user.getUid());
                    Sessions.setFirebaseUserExists(context, 1);
                } else {
                    // User is signed out
                    Log.d(TAG, TAG + "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    /**
     * start common.complaintcheflib.firebase auth
     */
    public void startAuthState() {
        if (mAuth != null && mAuthListener != null) {
            Log.d(TAG, TAG + " start auth");
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    /**
     * stop common.complaintcheflib.firebase auth
     */
    public void stopAuthState() {
        if (mAuthListener != null && mAuth != null) {
            Log.d(TAG, TAG + " stop auth");
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * @param token used in signing in to common.complaintcheflib.firebase
     */
    public void startSignInWithToken(String token) {
        if (token != null && !token.isEmpty()) {
            if (mAuth != null) {
                Log.d(TAG, TAG + " sign in with token - " + token);
                mAuth.signInWithCustomToken(token)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, TAG + "signInWithCustomToken:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.d(TAG, TAG + "signInWithCustomToken", task.getException());
                                }
                            }
                        });
            }
        }
    }
}
