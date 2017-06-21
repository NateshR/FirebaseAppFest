package user.complaintchef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.complaintcheflib.model.Complaint;
import common.complaintcheflib.util.BaseAppCompatActivity;
import common.complaintcheflib.util.Sessions;
import user.complaintchef.firebase.FirebaseDataStoreFactory;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class ListActivity extends BaseAppCompatActivity implements FirebaseDataStoreFactory.DataListCallBack<Complaint> {
    private static final String KEY_USER = "users", KEY_COMPLAINTS = "complaints";
    private static DatabaseReference mDatabaseReference;
    @BindView(R.id.rv_list)
    RecyclerView recyclerView;
    FirebaseDataStoreFactory<Complaint> firebaseDataStoreFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
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

            @BindView(R.id.tv_category)
            TextView categoryTV;
            @BindView(R.id.tv_details)
            TextView detailsTV;
            @BindView(R.id.tv_status)
            TextView statusTV;


            ListHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (getAdapterPosition() == -1) return;
//                        Complaint complaint = ListAdapter.this.complaintList.get(getAdapterPosition());
//                        Intent intent = new Intent(ListActivity.this,Tracker.class);
//                        intent.putExtra("admin_id",complaint.getAdminId());
//                        intent.putExtra("")
                    }
                });
            }
        }
    }
}
