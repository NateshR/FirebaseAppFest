package admin.complaintchef.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import admin.complaintchef.R;
import admin.complaintchef.core.MyApplication;
import common.complaintcheflib.firebase.FirebaseDataStoreFactory;
import common.complaintcheflib.model.Category;
import common.complaintcheflib.model.User;
import common.complaintcheflib.net.Login;
import common.complaintcheflib.util.Constant;
import common.complaintcheflib.util.Sessions;
import common.complaintcheflib.view.BaseAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class LoginActivity extends BaseAppCompatActivity implements FirebaseDataStoreFactory.DataListCallBack<Category> {

    public static final String KEY_CATEGORY = "categories";
    private EditText nameET;
    private EditText phoneET;
    private AppCompatButton loginB;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FirebaseDataStoreFactory<Category> categoryFirebaseDataStoreFactory;
    private List<Category> selectedCategoryList;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameET = (EditText) findViewById(R.id.et_name);
        phoneET = (EditText) findViewById(R.id.et_phone);
        loginB = (AppCompatButton) findViewById(R.id.b_login);
        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        this.selectedCategoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter();
        recyclerView.setAdapter(categoryAdapter);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        categoryFirebaseDataStoreFactory = new FirebaseDataStoreFactory<>();
        categoryFirebaseDataStoreFactory.dataList(FirebaseDataStoreFactory.ListenerType.NODE, Category.class, getmDatabaseReference(), this);
        loading(true);

        loginB.setEnabled(false);
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClicked();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Sessions.loadUsername(this) != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void onLoginClicked() {
        boolean validated = true;
        String phone = phoneET.getText().toString();
        if (phone.isEmpty()) {
            phoneET.setError("Enter Phone No.");
            validated = false;
        } /*else if (phone.length() != 10) {
            phoneET.setError("Enter Valid Phone No.");
            validated = false;
        }*/
        String name = nameET.getText().toString();
        if (name.isEmpty()) {
            nameET.setError("Enter Name");
            validated = false;
        }

        boolean checked = this.selectedCategoryList.size() > 0;

        if (!checked) {
            if (categoryAdapter.categoryList == null || categoryAdapter.categoryList.size() == 0)
                Toast.makeText(this, Constant.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Please Select At Least One Category", Toast.LENGTH_SHORT).show();
            validated = false;
        }

        if (validated) {
            loading(true);
            String uid = name + "_" + System.currentTimeMillis();
            User user = new User(uid, name, true, phone, this.selectedCategoryList);
            login(user);
        }
    }

    private void login(final User user) {
        JsonArray categoriesArray = new JsonArray();
        for (Category categories : user.getCategoryList())
            categoriesArray.add(categories.getId());
        Login.login(MyApplication.getAPIService(), user.getUid(), user.getName(), user.getMobileNo(), String.valueOf(user.getIsAdmin()), categoriesArray.toString(), new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loading(false);
                String token = response.body();
                Sessions.setUsername(LoginActivity.this, user.getUid());
                Sessions.setToken(LoginActivity.this, token);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                loading(false);
                Toast.makeText(LoginActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                Sessions.setUsername(LoginActivity.this, null);
            }
        });

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
        loginB.setEnabled(true);
        loading(false);
        categoryAdapter.setCategoryList(dataList);
    }

    @Override
    public void onSingleDataChange(Category data) {

    }

    @Override
    public void onCancelled() {

    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

        private List<Category> categoryList;

        public CategoryAdapter() {
            this.categoryList = new ArrayList<>();
        }

        public void setCategoryList(List<Category> categoryList) {
            this.categoryList = categoryList;
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
        }

        @Override
        public int getItemCount() {
            return categoryList == null ? 0 : categoryList.size();
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder {
            CheckBox nameTV;

            CategoryViewHolder(View itemView) {
                super(itemView);
                nameTV = (CheckBox) itemView.findViewById(R.id.cb_category_name);
                nameTV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        checkCategoryItem();
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkCategoryItem();
                    }
                });
            }

            private void checkCategoryItem() {
                if (getAdapterPosition() == -1) return;
                Category selectCategory = CategoryAdapter.this.categoryList.get(getAdapterPosition());
                selectCategory.setChecked(!selectCategory.isChecked());
                if (selectCategory.isChecked())
                    LoginActivity.this.selectedCategoryList.add(selectCategory);
                else if (LoginActivity.this.selectedCategoryList.contains(selectCategory))
                    LoginActivity.this.selectedCategoryList.remove(selectCategory);
            }
        }
    }
}
