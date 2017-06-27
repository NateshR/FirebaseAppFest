package user.complaintchef.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import common.complaintcheflib.firebase.FirebaseConfig;
import common.complaintcheflib.model.User;
import common.complaintcheflib.net.Login;
import common.complaintcheflib.util.Sessions;
import common.complaintcheflib.view.BaseAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import user.complaintchef.R;
import user.complaintchef.core.MyApplication;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class LoginActivity extends BaseAppCompatActivity {

    private EditText nameET;
    private EditText phoneET;
    private AppCompatButton loginB;
    private FirebaseConfig firebaseConfig;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameET = (EditText) findViewById(R.id.et_name);
        phoneET = (EditText) findViewById(R.id.et_phone);
        loginB = (AppCompatButton) findViewById(R.id.b_login);
        progressBar = (ProgressBar) findViewById(R.id.progress);

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
        if (Sessions.loadUsername(this) != null)
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
        } /*else if (phone.length() != 10) {
            phoneET.setError("Enter Valid Phone No.");
            validated = false;
        }*/
        String name = nameET.getText().toString();
        if (name.isEmpty()) {
            nameET.setError("Enter Name");
            validated = false;
        }

        if (validated) {
            loading(true);
            String uid = name + "_" + System.currentTimeMillis();
            User user = new User(uid, name, false, phone);
            login(user);
        }
    }

    private void login(final User user) {
        Login.login(MyApplication.getAPIService(), user.getUid(), user.getName(), user.getMobileNo(), String.valueOf(user.getIsAdmin()), "", new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loading(false);
                String token = response.body();
                Sessions.setToken(LoginActivity.this, token);
                Sessions.setUsername(LoginActivity.this, user.getUid());
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                loading(false);
                Toast.makeText(LoginActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                Sessions.setUsername(LoginActivity.this, null);
            }
        });

    }

    private void startSignInWithToken(String token) {
        firebaseConfig.startSignInWithToken(token);
    }

    private void loading(boolean toShow) {
        progressBar.setVisibility(toShow ? View.VISIBLE : View.GONE);
    }
}
