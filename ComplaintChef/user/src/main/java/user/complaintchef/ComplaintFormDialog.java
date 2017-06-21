package user.complaintchef;

import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.complaintcheflib.model.Category;
import common.complaintcheflib.util.LocationUtils;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class ComplaintFormDialog extends Dialog {

    @BindView(R.id.et_phone)
    EditText phoneET;
    @BindView(R.id.et_details)
    EditText detailsET;
    @BindView(R.id.b_submit)
    AppCompatButton submitB;

    private Category category;
    private Context context;

    public ComplaintFormDialog(@NonNull Context context, Category category) {
        super(context);
        this.context = context;
        ButterKnife.bind(this);
        setCancelable(false);
        this.category = category;
        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClicked();
            }
        });
    }

    private void onSubmitClicked() {
        boolean validated = true;
        String phone = phoneET.getText().toString();
        if (phone.isEmpty()) {
            phoneET.setError("Enter Phone No.");
            validated = false;
        } else if (phone.length() != 10) {
            phoneET.setError("Enter Valid Phone No.");
            validated = false;
        }
        String details = detailsET.getText().toString();
        if (details.isEmpty()) {
            detailsET.setError("Enter Details");
            validated = false;
        }

        if (validated) {

        }
    }

    private void registerComplaint(String phone, String details) {
        LocationUtils.getCurrentLocation(this.context, new LocationUtils.LocationReceivedCallback() {
            @Override
            public void onLocationReceived(@Nullable Location location) {
                //Send details now
            }
        });
    }
}
