package com.robosoft.editprofile.AdminPanel.entitychk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instructorchk
{
    private int instructorId;
    private String instructorName;
    private String url;
    private String description;
    private String profilePhoto;

}
