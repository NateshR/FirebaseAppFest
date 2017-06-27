package common.complaintcheflib.model;

import com.google.firebase.database.PropertyName;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class Category {
    @PropertyName("id")
    private Integer id;
    @PropertyName("name")
    private String name;
    private boolean isChecked = false;

    public Category() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
