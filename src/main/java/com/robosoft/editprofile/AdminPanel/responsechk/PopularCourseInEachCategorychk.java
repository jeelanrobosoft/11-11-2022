package com.robosoft.editprofile.AdminPanel.responsechk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopularCourseInEachCategorychk
{
    private String courseName;
    private Integer chapterCount;
    private String courseDuration;
    private String previewVideo;
}
