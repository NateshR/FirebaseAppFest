package user.complaintchef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.complaintcheflib.util.BaseAppCompatActivity;
import user.complaintchef.firebase.FirebaseConfig;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class LoginActivity extends BaseAppCompatActivity {

    @BindView(R.id.et_name)
    EditText nameET;
    @BindView(R.id.et_phone)
    EditText phoneET;
    @BindView(R.id.b_login)
    AppCompatButton loginB;
    private FirebaseConfig firebaseConfig;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        firebaseConfig = new FirebaseConfig(this);
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClicked();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseConfig.firebaseSignInStatusListenerObservable();
        firebaseConfig.startAuthState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseConfig.stopAuthState();
    }

    private void onLoginClicked() {
        boolean validated = true;
        String phone = phoneET.getText().toString();
        if (phone.isEmpty()) {
            phoneET.setError("Enter Phone No.");
            validated = false;
        } else if (phone.length() != 10) {
            phoneET.setError("Enter Valid Phone No.");
            validated = false;
        }
        String name = nameET.getText().toString();
        if (name.isEmpty()) {
            nameET.setError("Enter Name");
            validated = false;
        }

        if (validated) {
            login();
        }
    }

    private void login() {

    }

    private void startSignInWithToken(String token) {
        firebaseConfig.startSignInWithToken(token);
    }
}
