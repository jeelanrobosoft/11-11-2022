package com.robosoft.editprofile.AdminPanel.entitychk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterProgresschk
{
    private String userName;
    private Integer courseId;
    private Integer chapterId;
    private Integer testId;
    private Boolean chapterCompletedStatus;
    private Float chapterTestPercentage;
    private Float testResult;
}
