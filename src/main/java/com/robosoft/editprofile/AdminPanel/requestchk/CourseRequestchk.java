package com.robosoft.editprofile.AdminPanel.requestchk;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestchk
{
    private int courseId;
    private MultipartFile coursePhoto;
    private String courseName;
    private MultipartFile previewVideo;
    private int categoryId;
    private int subCategoryId;
    private String courseDuration;
}
