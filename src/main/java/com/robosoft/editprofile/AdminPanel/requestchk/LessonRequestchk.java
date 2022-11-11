package com.robosoft.editprofile.AdminPanel.requestchk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonRequestchk
{
    private int lessonNumber;
    private int courseId;
    private int chapterId;
    private String lessonName;
    private Time lessonDuration;
    private MultipartFile videoLink;
}
