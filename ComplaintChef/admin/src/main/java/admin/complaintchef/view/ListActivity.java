package admin.complaintchef.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import admin.complaintchef.R;
import common.complaintcheflib.model.Complaint;
import common.complaintcheflib.util.ListClickCallback;
import common.complaintcheflib.view.BaseAppCompatActivity;
import common.complaintcheflib.view.ListFragment;


/**
 * Created by nateshrelhan on 6/25/17.
 */

public class ListActivity extends BaseAppCompatActivity implements ListClickCallback, DialogInterface.OnClickListener {
    private final int ACCEPT_CODE = 0, DECLINE_CODE = 1, DISMISS_CODE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        FragmentManager fragmentManager = getSupportFragmentManager();
        ListFragment listFragment = ListFragment.newInstance(ListFragment.LIST_TYPE.COMPLAINT);
        if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, listFragment).commit();
        }
    }

    @Override
    public void itemClicked(Complaint complaint) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Update the status of complaint");
        alertDialog.setMessage(complaint.getDescription());
        alertDialog.setButton(ACCEPT_CODE, "Accept", this);
        alertDialog.setButton(DECLINE_CODE, "Decline", this);
        alertDialog.setButton(DISMISS_CODE, "Cancel", this);
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case ACCEPT_CODE:
                break;
            case DECLINE_CODE:
                break;
            case DISMISS_CODE:
                break;
        }
    }
}
