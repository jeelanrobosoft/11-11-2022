package com.robosoft.editprofile.AdminPanel.entitychk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lessonchk
{
    private int lessonId;
    private int lessonNumber;
    private int chapterId;
    private String lessonName;
    private String lessonDuration;
    private String videoLink;
}
