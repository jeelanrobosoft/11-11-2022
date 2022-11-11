package com.robosoft.editprofile.AdminPanel.requestchk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequestchk
{
    private String userName;
    private Integer courseId;
    private Date joinDate;   //Front end should take in the format of yyyy-mm-dd
}
