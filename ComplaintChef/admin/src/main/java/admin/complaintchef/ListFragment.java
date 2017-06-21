package admin.complaintchef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.complaintcheflib.firebase.FirebaseDataStoreFactory;
import common.complaintcheflib.model.Complaint;
import common.complaintcheflib.model.User;
import common.complaintcheflib.util.Sessions;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class ListFragment extends Fragment implements FirebaseDataStoreFactory.ChildCallBack<User> {

    private static DatabaseReference mDatabaseReference;
    RecyclerView recyclerView;
    FirebaseDataStoreFactory<User> firebaseDataStoreFactory;
    private int position;
    ListAdapter listAdapter = new ListAdapter(null);

    public static ListFragment newInstance(int position) {
        ListFragment fragment = new ListFragment();
        fragment.position = position;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgment_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listAdapter);
        firebaseDataStoreFactory = new FirebaseDataStoreFactory<>();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        firebaseDataStoreFactory.data( User.class, getmDatabaseReference(), this);

    }

    private DatabaseReference getmDatabaseReference() {
        if (mDatabaseReference == null) {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(Sessions.loadUsername(ListFragment.this.getActivity()));
            mDatabaseReference.keepSynced(true);
        }
        return mDatabaseReference;
    }

    @Override
    public void onChildAdded(User child) {

        List<Integer> ids;
        if (position == 0){
            ids = child.getPendingList();
        }else if (position == 1)
            ids = child.getAcceptedList();
        else ids =child.getDeclinedList();

        final List<Complaint> complaints = new ArrayList<>();

        for (Integer id : ids){
            FirebaseDataStoreFactory<Complaint> firebaseDataStoreFactory = new FirebaseDataStoreFactory<>();
            firebaseDataStoreFactory.data(Complaint.class, getComplainttReference(id), new FirebaseDataStoreFactory.ChildCallBack<Complaint>() {
                @Override
                public void onChildAdded(Complaint child) {
                    complaints.add(child);
                    listAdapter.refresh(complaints);

                }

                @Override
                public void onChildChanged(Complaint child) {

                }

                @Override
                public void onChildRemoved(Complaint child) {

                }

                @Override
                public void onChildMoved(Complaint child) {

                }

                @Override
                public void onCancelled() {

                }
            });
        }
    }

    private DatabaseReference getComplainttReference(Integer id) {
        DatabaseReference mDatabaseReference = null;
        if (mDatabaseReference == null) {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("complaints").child(String.valueOf(id));
            mDatabaseReference.keepSynced(true);
        }
        return mDatabaseReference;
    }

    @Override
    public void onChildChanged(User child) {

    }

    @Override
    public void onChildRemoved(User child) {

    }

    @Override
    public void onChildMoved(User child) {

    }

    @Override
    public void onCancelled() {
        Toast.makeText(this.getActivity(), "Something went wrong", Toast.LENGTH_SHORT);
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

        private List<Complaint> complaintList;

        public ListAdapter(List<Complaint> complaintList) {
            this.complaintList = complaintList;
        }

        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint, parent, false));
        }

        @Override
        public void onBindViewHolder(ListHolder holder, int position) {
            Complaint complaint = complaintList.get(position);
            if (ListFragment.this.position == 0) {
                holder.actionRL.setVisibility(View.VISIBLE);
            } else {
                holder.actionRL.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return complaintList == null ? 0 : complaintList.size();
        }

        public void refresh(List<Complaint> complaints) {
            this.complaintList = complaints;
            notifyDataSetChanged();
        }

        class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            @BindView(R.id.tv_category)
            TextView categoryTV;
            @BindView(R.id.tv_details)
            TextView detailsTV;
            @BindView(R.id.b_accept)
            AppCompatButton acceptB;
            @BindView(R.id.b_reject)
            AppCompatButton rejectB;
            @BindView(R.id.rl_actions)
            RelativeLayout actionRL;

            public ListHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.b_accept:
                        break;
                    case R.id.b_reject:
                        break;
                }
            }
        }
    }
}
