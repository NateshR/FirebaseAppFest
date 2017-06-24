package common.complaintcheflib.model;

import com.google.firebase.database.PropertyName;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class Category {
    @PropertyName("id")
    private String id;
    @PropertyName("name")
    private String name;

    public Category() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
