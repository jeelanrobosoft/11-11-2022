package com.robosoft.editprofile.AdminPanel.responsechk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeAllCoursechk
{
    private int courseId;
    private String coursePhoto;
    private String courseName;
    private int categoryId;
    private int chapterCount;

    public HomeAllCoursechk(String coursePhoto, String courseName, int categoryId, int chapterCount)
    {
        this.coursePhoto = coursePhoto;
        this.courseName = courseName;
        this.categoryId = categoryId;
        this.chapterCount = chapterCount;
    }

    
}
