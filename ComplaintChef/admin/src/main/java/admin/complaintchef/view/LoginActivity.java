package admin.complaintchef.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import admin.complaintchef.R;
import admin.complaintchef.core.MyApplication;
import common.complaintcheflib.model.User;
import common.complaintcheflib.net.Login;
import common.complaintcheflib.util.Sessions;
import common.complaintcheflib.view.BaseAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class LoginActivity extends BaseAppCompatActivity {

    private EditText nameET;
    private EditText phoneET;
    private AppCompatButton loginB;
    private CheckBox electricityCB;
    private CheckBox waterCB;
    private CheckBox infraCB;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameET = (EditText) findViewById(R.id.et_name);
        phoneET = (EditText) findViewById(R.id.et_phone);
        loginB = (AppCompatButton) findViewById(R.id.b_login);
        electricityCB = (CheckBox) findViewById(R.id.cb_electricity);
        waterCB = (CheckBox) findViewById(R.id.cb_water);
        infraCB = (CheckBox) findViewById(R.id.cb_electricity);
        progressBar = (ProgressBar) findViewById(R.id.progress);

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
        if (Sessions.loadUsername(this) != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
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

        boolean checked = electricityCB.isChecked() || waterCB.isChecked() || infraCB.isChecked();

        if (!checked) {
            Toast.makeText(this, "Please Select At Least One Category", Toast.LENGTH_SHORT).show();
            validated = false;
        }

        if (validated) {
            loading(true);
            String categories = "";
            if (electricityCB.isChecked())
                categories += "1";
            if (waterCB.isChecked())
                categories += ",2";
            if (infraCB.isChecked())
                categories += ",3";
            String uid = name + "_" + System.currentTimeMillis();
            User user = new User(uid, name, true, phone);
            login(user, categories);
        }
    }

    private void login(final User user, String categories) {
        Login.login(MyApplication.getAPIService(), user.getUid(), user.getName(), user.getMobileNo(), String.valueOf(user.getIsAdmin()), categories, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loading(false);
                String token = response.body();
                Sessions.setToken(LoginActivity.this, token);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                Sessions.setUsername(LoginActivity.this, user.getUid());
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                loading(false);
                Toast.makeText(LoginActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                Sessions.setUsername(LoginActivity.this, null);
            }
        });

    }

    private void loading(boolean toShow) {
        progressBar.setVisibility(toShow ? View.VISIBLE : View.GONE);
    }
}
