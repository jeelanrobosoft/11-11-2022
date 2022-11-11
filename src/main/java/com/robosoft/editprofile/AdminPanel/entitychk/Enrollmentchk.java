package com.robosoft.editprofile.AdminPanel.entitychk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrollmentchk
{
    private String userName;
    private Integer courseId;
    private Date joinDate;
    private Date completedDate;
    private Integer courseScore;

    public Enrollmentchk(int courseId)
    {
        this.courseId = courseId;
    }
}
