package user.complaintchef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.complaintcheflib.model.Complaint;
import common.complaintcheflib.util.BaseAppCompatActivity;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class ListActivity extends BaseAppCompatActivity {

    @BindView(R.id.rv_list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ListAdapter(null));
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

                    }
                });
            }
        }
    }
}
