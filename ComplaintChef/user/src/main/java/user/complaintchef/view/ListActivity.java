package user.complaintchef.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import common.complaintcheflib.model.Complaint;
import common.complaintcheflib.util.ListClickCallback;
import common.complaintcheflib.view.BaseAppCompatActivity;
import common.complaintcheflib.view.ListFragment;
import user.complaintchef.R;

/**
 * Created by nateshrelhan on 6/25/17.
 */

public class ListActivity extends BaseAppCompatActivity implements ListClickCallback {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        FragmentManager fragmentManager = getSupportFragmentManager();
        ListFragment listFragment = ListFragment.newInstance(ListFragment.LIST_TYPE.COMPLAINT, ListFragment.FROM_ACTIVITY.USER);
        if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, listFragment).commit();
        }
    }

    @Override
    public void itemClicked(Complaint complaint) {
        Intent intent = new Intent(this, Tracker.class);
        intent.putExtra(Tracker.KEY_ADMIN_ID, complaint.getAdmin());
        intent.putExtra(Tracker.KEY_USER_LAT, complaint.getLocationLat());
        intent.putExtra(Tracker.KEY_USER_LONG, complaint.getLocationLong());
        startActivity(intent);
    }
}
