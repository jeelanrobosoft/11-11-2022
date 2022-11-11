package com.robosoft.editprofile.AdminPanel.entitychk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Testchk
{
    private int testId;
    private String testName;
    private int chapterId;
    private String testDuration;
    private int questionsCount;
    private int passingGrade;
}
