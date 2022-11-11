package com.robosoft.editprofile.AdminPanel.requestchk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoPauseRequestchk
{
    private String userName;
    private Time pauseTime;
    private Integer lessonId;
    private Integer chapterId;
    private Integer courseId;

}
