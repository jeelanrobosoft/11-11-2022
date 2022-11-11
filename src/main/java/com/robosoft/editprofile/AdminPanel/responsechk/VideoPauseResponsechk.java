package com.robosoft.editprofile.AdminPanel.responsechk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoPauseResponsechk
{
    private String userName;
    private String pauseTime;
    private Integer lessonId;
    private Integer chapterId;
    private Integer courseId;


    public VideoPauseResponsechk(String userName, String pauseTime, Integer lessonId, Integer chapterId)
    {
        this.userName = userName;
        this.pauseTime = pauseTime;
        this.lessonId = lessonId;
        this.chapterId = chapterId;
    }
}
