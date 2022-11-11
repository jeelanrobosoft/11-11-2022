package com.robosoft.editprofile.AdminPanelS.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponseRequest {
    private Integer chapterId;
    private String userName;
}
