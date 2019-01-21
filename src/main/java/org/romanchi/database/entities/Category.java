package org.romanchi.database.entities;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private long categoryId;
    private String categoryName;
    public Category(){}

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("categoryId", getCategoryId());
        map.put("categoryName", getCategoryName());
        return map;
    }
}
