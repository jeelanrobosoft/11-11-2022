package com.robosoft.editprofile.AdminPanelS.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletedResponse {
    private String courseName;
    private String coursePhoto;
    private Float coursePercentage;
}
