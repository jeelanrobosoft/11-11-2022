package com.robosoft.editprofile.AdminPanelS.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Integer categoryId;
    private String categoryName;
    private String categoryPhoto;

    public Category(Integer categoryId) {
        this.categoryId = categoryId;
    }

}
