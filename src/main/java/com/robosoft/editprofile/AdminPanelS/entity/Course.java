package com.robosoft.editprofile.AdminPanelS.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course
{
    private Integer courseId;
    private String coursePhoto;
    private String courseName;
    private String previewVideo;
    private Integer categoryId;
    private Integer subCategoryId;
    private String courseDuration;

    public Course(Integer courseId, String coursePhoto, String courseName, String previewVideo, Integer categoryId, Integer subCategoryId)
    {
        this.courseId = courseId;
        this.coursePhoto = coursePhoto;
        this.courseName = courseName;
        this.previewVideo = previewVideo;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
    }

    public Course(Integer courseId) {
        this.courseId = courseId;
    }
}
