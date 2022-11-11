package com.robosoft.editprofile.AdminPanelS.controller;

import com.robosoft.editprofile.AdminPanelS.entity.Overview;
import com.robosoft.editprofile.AdminPanelS.model.Category;
import com.robosoft.editprofile.AdminPanelS.model.Chapter;
import com.robosoft.editprofile.AdminPanelS.model.Policy;
import com.robosoft.editprofile.AdminPanelS.model.SubCategory;
import com.robosoft.editprofile.AdminPanelS.request.CategoryRequest;
import com.robosoft.editprofile.AdminPanelS.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PostMapping("/Category")
    public ResponseEntity<?> addCategory(@ModelAttribute CategoryRequest category) throws IOException {
        int change = adminService.addCategory(category);

        if(change > 0)
            return ResponseEntity.of(Optional.of("Category " + category.getCategoryName()+ " has been Added SuccessFully"));
        else
            return new ResponseEntity<>("Category Type Already Exists",HttpStatus.ALREADY_REPORTED);
    }

    @GetMapping("/Categories")
    public ResponseEntity<?> getCategories()
    {
        List<Category> categories = adminService.getCategories();

        if((categories) != null)
            return ResponseEntity.of(Optional.of(categories));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/SubCategory")
    public ResponseEntity<?> addSubCategory(@RequestBody SubCategory subcategory)
    {
        int change = adminService.addSubCategory(subcategory);

        if(change > 0)
            return ResponseEntity.of(Optional.of("SubCategory " + subcategory.getSubCategoryName()+ " has been Added SuccessFully"));
        else
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
    }

    @GetMapping("/SubCategories")
    public ResponseEntity<?> getSubcategory(@RequestBody Category category)
    {
        List<SubCategory> subCategories = adminService.getSubCategories(category);

        if((subCategories) != null)
            return ResponseEntity.of(Optional.of(subCategories));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/Chapter")
    public ResponseEntity<?> addChapter(@RequestBody Chapter chapter)
    {
        int change = adminService.addChapter(chapter);

        if(change > 0)
            return ResponseEntity.of(Optional.of("Chapter " + chapter.getChapterName()+ " has been Added SuccessFully"));
        else
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
    }


    @PostMapping("/Overview")
    public ResponseEntity<?> addOverview(@RequestBody Overview overview)
    {
        if(adminService.addOverView(overview) != 0)
            return ResponseEntity.of(Optional.of("OverView for the Course" + overview.getCourseId() + " has been Added SuccessFully"));
        else
            return new ResponseEntity<>("Overview For the Course "+overview.getCourseId()+"has already is Already Present",HttpStatus.ALREADY_REPORTED);
    }

    @PostMapping("/Policy")
    public ResponseEntity<?> addPolicy(@RequestBody Policy policy){
        if(adminService.addPolicy(policy) != 0){
            return new ResponseEntity<>("Privacy Policy and Terms and Condition Updated",HttpStatus.OK);
        }
        return new ResponseEntity<>("You Didn't changed the Privacy Policy and Terms and Conditions or Failed to Update the Privacy Policy and Terms and Condition",HttpStatus.ALREADY_REPORTED);
    }


}
