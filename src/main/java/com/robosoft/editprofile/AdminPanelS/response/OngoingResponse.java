package com.robosoft.editprofile.AdminPanelS.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OngoingResponse {

    private String courseName;
    private String coursePhoto;
    private Integer completedChapter;
    private Integer totalChapter;
}
