package user.complaintchef;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import common.complaintcheflib.model.Complaint;
import common.complaintcheflib.util.BaseAppCompatActivity;
import common.complaintcheflib.util.Sessions;
import common.complaintcheflib.firebase.FirebaseDataStoreFactory;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class ListActivity extends BaseAppCompatActivity implements FirebaseDataStoreFactory.DataListCallBack<Complaint> {
    private static final String KEY_USER = "users", KEY_COMPLAINTS = "complaints";
    private static DatabaseReference mDatabaseReference;
    RecyclerView recyclerView;
    FirebaseDataStoreFactory<Complaint> firebaseDataStoreFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        firebaseDataStoreFactory = new FirebaseDataStoreFactory<>();
        firebaseDataStoreFactory.dataList(FirebaseDataStoreFactory.ListenerType.NODE, Complaint.class, getmDatabaseReference(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private DatabaseReference getmDatabaseReference() {
        if (mDatabaseReference == null) {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(KEY_USER).child(Sessions.loadUsername(ListActivity.this)).child(KEY_COMPLAINTS);
            mDatabaseReference.keepSynced(true);
        }
        return mDatabaseReference;
    }

    @Override
    public void onDataChange(List<Complaint> dataList) {
        recyclerView.setAdapter(new ListAdapter(dataList));
    }

    @Override
    public void onCancelled() {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT);
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

        private List<Complaint> complaintList;

        public ListAdapter(List<Complaint> complaintList) {
            this.complaintList = complaintList;
        }

        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(ListHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return complaintList == null ? 0 : complaintList.size();
        }

        class ListHolder extends RecyclerView.ViewHolder {

            TextView categoryTV;
            TextView detailsTV;
            TextView statusTV;


            ListHolder(View itemView) {
                super(itemView);

                categoryTV = (TextView) itemView.findViewById(R.id.tv_category);
                detailsTV = (TextView) itemView.findViewById(R.id.tv_details);
                statusTV = (TextView) itemView.findViewById(R.id.tv_status);

                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (getAdapterPosition() == -1) return;
                        Complaint complaint = ListAdapter.this.complaintList.get(getAdapterPosition());
                        Intent intent = new Intent(ListActivity.this, Tracker.class);
                        intent.putExtra("admin_id", complaint.getAdminId());
                        intent.putExtra("user_lat", complaint.getLatitude());
                        intent.putExtra("user_long", complaint.getLongitude());
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
