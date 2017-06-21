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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.complaintcheflib.model.Complaint;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class ListFragment extends Fragment {

    private int position;
    @BindView(R.id.rv_list)
    RecyclerView recyclerView;

    public static ListFragment newInstance(int position) {
        ListFragment fragment = new ListFragment();
        fragment.position = position;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgment_list, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
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
