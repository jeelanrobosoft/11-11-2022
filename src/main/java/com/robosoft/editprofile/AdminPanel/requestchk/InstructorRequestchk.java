package com.robosoft.editprofile.AdminPanel.requestchk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstructorRequestchk
{
    private int instructorId;
    private String instructorName;
    private String url;
    private String description;
    private MultipartFile profilePhoto;
}
