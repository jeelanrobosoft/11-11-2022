package com.robosoft.editprofile.AdminPanelS.model;


import lombok.Data;

@Data
public class Chapter {
    private Integer chapterId;
    private Integer courseId;
    private Integer chapterNumber;
    private String chapterName;
    private Boolean chapterCompletedStatus;
    private Integer chapterTestPercentage;
    private String chapterDuration;
}
