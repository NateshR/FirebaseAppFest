package admin.complaintchef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.et_name)
    EditText nameET;
    @BindView(R.id.et_phone)
    EditText phoneET;
    @BindView(R.id.b_login)
    AppCompatButton loginB;
    @BindView(R.id.cb_electricity)
    CheckBox electricityCB;
    @BindView(R.id.cb_water)
    CheckBox waterCB;
    @BindView(R.id.cb_infra)
    CheckBox infraCB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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

        if (!checked){
            Toast.makeText(this, "Please Select At Least One Category", Toast.LENGTH_SHORT).show();
            validated = false;
        }

        if (validated) {
            login(name, phone, null);
        }
    }

    private void login(String name, String phone, String categories) {
        String username = Login.login(MyApplication.getAPIService(), name, phone, "1", categories, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String token = response.body();
               Sessions.setToken(LoginActivity.this, token);

            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        Sessions.setUsername(this, username);
    }
}
