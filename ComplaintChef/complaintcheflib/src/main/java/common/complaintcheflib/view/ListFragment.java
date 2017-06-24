package common.complaintcheflib.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import common.complaintcheflib.R;
import common.complaintcheflib.firebase.FirebaseDataStoreFactory;
import common.complaintcheflib.model.Complaint;
import common.complaintcheflib.model.ComplaintId;
import common.complaintcheflib.model.User;
import common.complaintcheflib.util.ListClickCallback;
import common.complaintcheflib.util.Sessions;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class ListFragment extends BaseFragment implements FirebaseDataStoreFactory.DataListCallBack {
    public static final String KEY_USER = "users", KEY_COMPLAINTS = "complaints", KEY_LIST_TYPE = "list_type";
    private RecyclerView recyclerView;
    private FirebaseDataStoreFactory<User> firebaseDataStoreFactoryUser;
    private FirebaseDataStoreFactory<Complaint> firebaseDataStoreFactoryComplaint;
    private ProgressBar progressBar;
    private ListAdapter listAdapter;
    private LIST_TYPE listType;
    private ListClickCallback listClickCallback;

    public static ListFragment newInstance(LIST_TYPE listType) {
        ListFragment listFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_LIST_TYPE, listType.name());
        listFragment.setArguments(bundle);
        return listFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListClickCallback)
            this.listClickCallback = (ListClickCallback) context;
        else
            throw new RuntimeException("Make sure ListFragment context implements ListClickCallback");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listType = LIST_TYPE.valueOf(getArguments().getString(KEY_LIST_TYPE));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listAdapter = new ListAdapter();
        recyclerView.setAdapter(listAdapter);
        firebaseDataStoreFactoryUser = new FirebaseDataStoreFactory<>();
        firebaseDataStoreFactoryUser.data(FirebaseDataStoreFactory.ListenerType.NODE, User.class, getUserDatabaseReference(), this);
        loading(true);
    }

    private DatabaseReference getUserDatabaseReference() {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(KEY_USER).child(Sessions.loadUsername(getActivity()));
        mDatabaseReference.keepSynced(true);
        return mDatabaseReference;
    }

    private DatabaseReference getComplaintDatabaseReference(String complaintId) {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(KEY_COMPLAINTS).child(complaintId);
        mDatabaseReference.keepSynced(true);
        return mDatabaseReference;
    }

    @Override
    public void onDataChange(List dataList) {

    }

    @Override
    public void onSingleDataChange(Object data) {
        if (data == null) return;
        if (data instanceof User) {
            User user = (User) data;
            List<ComplaintId> complaintIdsList = new ArrayList<>();
            switch (this.listType) {
                case COMPLAINT:
                    complaintIdsList.addAll(user.getComplaintList());
                    break;
                case PENDING:
                    complaintIdsList.addAll(user.getPendingComplaintList());
                    break;
                case ACCEPTED:
                    complaintIdsList.addAll(user.getAcceptedComplaintList());
                    break;
                case DECLINED:
                    complaintIdsList.addAll(user.getDeclinedComplaintList());
                    break;
            }
            if (complaintIdsList.size() == 0) {
                loading(false);
                return;
            }
            for (ComplaintId complaintId : complaintIdsList) {
                if (complaintId == null) continue;
                firebaseDataStoreFactoryComplaint = new FirebaseDataStoreFactory<>();
                firebaseDataStoreFactoryComplaint.data(FirebaseDataStoreFactory.ListenerType.NODE, Complaint.class, getComplaintDatabaseReference(complaintId.getComplaintId()), this);
            }
        } else if (data instanceof Complaint) {
            Complaint complaint = (Complaint) data;
            this.listAdapter.addComplaint(complaint);
        }
    }

    @Override
    public void onCancelled() {
        loading(false);
        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    private void loading(boolean toShow) {
        progressBar.setVisibility(toShow ? View.VISIBLE : View.GONE);
    }

    public enum LIST_TYPE {
        COMPLAINT,
        PENDING,
        ACCEPTED,
        DECLINED,
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

        private List<Complaint> complaintList;

        private ListAdapter() {
            this.complaintList = new ArrayList<>();
        }

        private void addComplaint(Complaint complaint) {
            if (this.complaintList.size() == 0) {
                this.complaintList.add(complaint);
                notifyItemInserted(this.complaintList.size() - 1);
            } else {
                for (int i = 0; i < this.complaintList.size(); i++) {
                    Complaint complaint1 = this.complaintList.get(i);
                    if (complaint.getComplaintId().equals(complaint1.getComplaintId())) {
                        this.complaintList.set(i, complaint);
                        notifyItemChanged(i);
                        break;
                    }
                }
            }
        }

        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(ListHolder holder, int position) {
            Complaint complaint = complaintList.get(position);
            holder.categoryTV.setText(complaint.getTitle());
            holder.detailsTV.setText(complaint.getDescription());
            holder.statusTV.setText(complaint.getStatus());
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
                        switch (complaint.getStatus()) {
                            case Complaint.STATUS_ACCEPTED:
                                ListFragment.this.listClickCallback.itemClicked(complaint);
                                break;
                            case Complaint.STATUS_PENDING:
                                Snackbar.make(recyclerView, "Wait till someone accepts your complaint", Snackbar.LENGTH_LONG).show();
                                break;
                            default:
                                Snackbar.make(recyclerView, "Your complaint has been declined by our officers", Snackbar.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }
        }
    }
}
