package common.complaintcheflib.model;

import com.google.firebase.database.PropertyName;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class Category {
    @PropertyName("id")
    private int categoryId;

    @PropertyName("name")
    private String categoryName;

    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
