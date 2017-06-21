package user.complaintchef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<Integer, Integer> iconsMap = new HashMap<>();

        public CategoryAdapter(List<Category> categoryList) {
            this.categoryList = categoryList;
            iconsMap.put(1, R.drawable.ic_electrivity);
            iconsMap.put(2, R.drawable.ic_water);
            iconsMap.put(3, R.drawable.ic_infra);
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {
            Category category = categoryList.get(position);
            holder.nameTV.setText(category.getCategoryName());
            holder.iconIV.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, iconsMap.get(category.getCategoryId())));
        }

        @Override
        public int getItemCount() {
            return categoryList == null ? 0 : categoryList.size();
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.iv_icon)
            ImageView iconIV;
            @BindView(R.id.tv_name)
            TextView nameTV;

            CategoryViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Category category = categoryList.get(getAdapterPosition());
                        new ComplaintFormDialog(MainActivity.this, category).show();
                    }
                });
            }
        }
    }
}