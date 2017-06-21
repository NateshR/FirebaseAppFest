package admin.complaintchef;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import admin.complaintchef.services.LocationTrackerService;
import common.complaintcheflib.util.BaseAppCompatActivity;
import common.complaintcheflib.util.Login;
import common.complaintcheflib.util.Sessions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class LoginActivity extends BaseAppCompatActivity {

    EditText nameET;
    EditText phoneET;
    AppCompatButton loginB;
    CheckBox electricityCB;
    CheckBox waterCB;
    CheckBox infraCB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        startService(new Intent(this, LocationTrackerService.class));

        nameET = (EditText) findViewById(R.id.et_name);
        phoneET = (EditText) findViewById(R.id.et_phone);
        loginB = (AppCompatButton) findViewById(R.id.b_login);
        electricityCB = (CheckBox) findViewById(R.id.cb_electricity);
        waterCB = (CheckBox) findViewById(R.id.cb_water);
        infraCB = (CheckBox) findViewById(R.id.cb_electricity);

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClicked();
            }
        });
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

        boolean checked = electricityCB.isChecked() || waterCB.isChecked() || infraCB.isChecked();

        if (!checked) {
            Toast.makeText(this, "Please Select At Least One Category", Toast.LENGTH_SHORT).show();
            validated = false;
        }

        if (validated) {
            String categories = "";
            if (electricityCB.isChecked())
                categories += "1";
            if (waterCB.isChecked())
                categories += ",2";
            if (infraCB.isChecked())
                categories += ",3";
            login(name, phone, categories);
        }
    }

    private void login(String name, String phone, String categories) {
        String username = Login.login(MyApplication.getAPIService(), name, phone, "1", categories, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String token = response.body();
                Sessions.setToken(LoginActivity.this, token);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        Sessions.setUsername(this, username);
    }
}
