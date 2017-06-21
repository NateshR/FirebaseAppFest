package user.complaintchef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.complaintcheflib.model.Category;
import common.complaintcheflib.util.BaseAppCompatActivity;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class MainActivity extends BaseAppCompatActivity {

    @BindView(R.id.rv_main)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new CategoryAdapter(null));
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

        private List<Category> categoryList;

        public CategoryAdapter(List<Category> categoryList) {
            this.categoryList = categoryList;
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent, false));
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return categoryList == null ? 0 : categoryList.size();
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder {

            public CategoryViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Category category = categoryList.get(getAdapterPosition());

                    }
                });
            }
        }
    }
}
