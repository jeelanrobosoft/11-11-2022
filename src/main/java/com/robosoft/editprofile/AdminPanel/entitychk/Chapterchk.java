package com.robosoft.editprofile.AdminPanel.entitychk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chapterchk
{
    private int chapterId;
    private int courseId;
    private int chapterNumber;
    private String chapterName;
    private String chapterDuration;

    public Chapterchk(String chapterDuration)
    {
        this.chapterDuration = chapterDuration;
    }
}
