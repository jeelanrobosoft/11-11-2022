package com.robosoft.editprofile.AdminPanel.requestchk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestRequestchk
{
    private int testId;
    private String testName;
    private int chapterId;
    private Time testDuration;
    private int passingGrade;
}
