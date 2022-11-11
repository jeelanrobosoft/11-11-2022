package com.robosoft.editprofile.AdminPanel.entitychk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Questionchk
{
    private int questionId;
    private String questionName;
    private int testId;
    private String option_1;
    private String option_2;
    private String option_3;
    private String option_4;
    private String correctAnswer;
}
