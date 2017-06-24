package user.complaintchef.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.complaintcheflib.firebase.FirebaseDataStoreFactory;
import common.complaintcheflib.model.Category;
import common.complaintcheflib.view.BaseAppCompatActivity;
import common.complaintcheflib.view.ListFragment;
import user.complaintchef.R;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class MainActivity extends BaseAppCompatActivity implements FirebaseDataStoreFactory.DataListCallBack<Category> {

    public static final String KEY_CATEGORY = "categories";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private CategoryAdapter categoryAdapter;
    private FirebaseDataStoreFactory<Category> categoryFirebaseDataStoreFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        categoryAdapter = new CategoryAdapter();
        recyclerView.setAdapter(categoryAdapter);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        categoryFirebaseDataStoreFactory = new FirebaseDataStoreFactory<>();
        categoryFirebaseDataStoreFactory.dataList(FirebaseDataStoreFactory.ListenerType.NODE, Category.class, getmDatabaseReference(), this);
        loading(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_history:
                startActivity(new Intent(this, ListFragment.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loading(boolean toShow) {
        progressBar.setVisibility(toShow ? View.VISIBLE : View.GONE);
    }

    private DatabaseReference getmDatabaseReference() {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(KEY_CATEGORY);
        mDatabaseReference.keepSynced(true);
        return mDatabaseReference;
    }

    @Override
    public void onDataChange(List<Category> dataList) {
        loading(false);
        categoryAdapter.setCategoryList(dataList);
    }

    @Override
    public void onSingleDataChange(Category data) {

    }

    @Override
    public void onCancelled() {
        loading(false);
        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG);
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

        Map<String, Integer> iconsMap = new HashMap<>();
        private List<Category> categoryList;

        public CategoryAdapter() {
            this.categoryList = new ArrayList<>();
        }

        public void setCategoryList(List<Category> categoryList) {
            this.categoryList = categoryList;
            for (Category category : categoryList) {
                switch (category.getName()) {
                    case "electricity":
                        iconsMap.put(category.getId(), R.drawable.ic_electrivity);
                        break;
                    case "water":
                        iconsMap.put(category.getId(), R.drawable.ic_water);
                        break;
                    default:
                        iconsMap.put(category.getId(), R.drawable.ic_infra);
                        break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {
            Category category = categoryList.get(position);
            holder.nameTV.setText(category.getName());
            holder.iconIV.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, iconsMap.get(category.getId())));
        }

        @Override
        public int getItemCount() {
            return categoryList == null ? 0 : categoryList.size();
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder {
            ImageView iconIV;
            TextView nameTV;

            CategoryViewHolder(View itemView) {
                super(itemView);
                nameTV = (TextView) itemView.findViewById(R.id.tv_name);
                iconIV = (ImageView) itemView.findViewById(R.id.iv_icon);
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
