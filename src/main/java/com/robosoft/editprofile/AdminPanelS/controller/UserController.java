package com.robosoft.editprofile.AdminPanelS.controller;

import com.robosoft.editprofile.AdminPanelS.entity.Course;
import com.robosoft.editprofile.AdminPanelS.model.Category;
import com.robosoft.editprofile.AdminPanelS.request.CourseChapterRequest;
import com.robosoft.editprofile.AdminPanelS.request.LessonResponseRequest;
import com.robosoft.editprofile.AdminPanelS.response.*;
import com.robosoft.editprofile.AdminPanelS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/CourseOverView")
    public ResponseEntity<?> getCourseOverview(@RequestBody Course course) {
        try {
            OverviewResponse overviewResponse = userService.getOverviewOfCourse(course.getCourseId());
            if (overviewResponse != null) {
                return ResponseEntity.of(Optional.of(overviewResponse));
            }
           return new ResponseEntity("Overview For the Course is not Available", HttpStatus.NOT_FOUND);
        }catch (Exception e)
        {
            return new ResponseEntity("Invalid Input", HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/BasicCourses")
    public ResponseEntity<?> getBeginnerCourses(@RequestBody Category category)
    {
        try {
            List<CourseResponse> courseResponses = userService.getBasicCourses(category.getCategoryId());

            if(courseResponses.isEmpty())
            {
                return new ResponseEntity("Currently No Courses Avaialable in this Category",HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.of(Optional.of(courseResponses));

        }catch (Exception e)
        {
            return new ResponseEntity("Invalid Input",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/AdvanceCourses")
    public ResponseEntity<?> getAdvancedCourses(@RequestBody Category category)
    {
        try {
            List<CourseResponse> courseResponses = userService.getAdvanceCourses(category.getCategoryId());

            if(courseResponses.isEmpty())
            {
                return new ResponseEntity("Currently No Courses Avaialable in this Category",HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.of(Optional.of(courseResponses));

        }catch (Exception e)
        {
            return new ResponseEntity("Invalid Input",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/AllCoursesOfCategory")
    public ResponseEntity<?> getAllCourses(@RequestBody Category category)
    {
        try {
            List<AllCoursesResponse> allCourseResponses = userService.getAllCoursesOf(category.getCategoryId());

            if(allCourseResponses.isEmpty())
            {
                return new ResponseEntity("Currently No Courses Avaialable in this Category",HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.of(Optional.of(allCourseResponses));

        }catch (Exception e)
        {
            return new ResponseEntity("Invalid Input",HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/privacyPolicy")
    public ResponseEntity<?> getPrivacyPolicy() {
        try {
            String privacyPolicy = userService.getPolicy();
                return ResponseEntity.of(Optional.of(privacyPolicy));
        }catch (Exception e)
        {
            return new ResponseEntity("Privacy Policy Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/TermsAndConditions")
    public ResponseEntity<?> getTermsAndConditions() {
        try {
            String termsAndConditions = userService.getTermsAndConditions();
                return ResponseEntity.of(Optional.of(termsAndConditions));
        }catch (Exception e)
        {
            return new ResponseEntity("Terms and Conditions Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/OngoingCourses")
    public ResponseEntity<?> getOngoingCourses(@RequestBody String userName)
    {
        try {
            List<OngoingResponse> ongoingResponses = userService.getOngoingCourses(userName);
            if(ongoingResponses.isEmpty()){
                return new ResponseEntity<>("No Ongoing Courses or The Course You Enrolled has No Chapters yet.",HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.of(Optional.of(ongoingResponses));
        }catch (Exception e) {
            return new ResponseEntity<>("Invalid Input ", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/CompletedCourses/{userName}")
    public ResponseEntity<?> getCompletedCourses(@PathVariable String userName)
    {
        List<CompletedResponse> completedResponse = userService.getCourseCompletedResponse(userName);
        if(completedResponse.isEmpty())
            return new ResponseEntity<>("No Completed Courses",HttpStatus.NOT_FOUND);
        return ResponseEntity.of(Optional.of(completedResponse));

    }

    @GetMapping("/CourseChapterResponse")
    public ResponseEntity<?> getCourseChapterResponse(@RequestBody CourseChapterRequest courseChapterRequest)
    {
        try {
            CourseChapterResponse  courseChapterResponse =userService.getCourseChapterResponse(courseChapterRequest.getCourseId(),courseChapterRequest.getUserName());
            if(courseChapterResponse != null)
            {
                return ResponseEntity.of(Optional.of(courseChapterRequest));
            }
            return new ResponseEntity<>("There are No Chapters Available at the Course yet",HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Invalid Input",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/LessonResponse")
    public ResponseEntity<?> getLessonResponse(@RequestBody LessonResponseRequest lessonResponseRequest){
        List<LessonResponse> lessonResponses= userService.getLessonResponses(lessonResponseRequest.getChapterId(),lessonResponseRequest.getUserName());
        if(lessonResponses.isEmpty())
        {
            return new ResponseEntity<>("Currently No Lessons Available in the Course",HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.of(Optional.of(lessonResponses));
    }

    @GetMapping("/Continue")
    public ResponseEntity<?> getLastPlayed(@RequestBody CourseChapterRequest courseChapterRequest)
    {
        Continue c = userService.getLastPlayed(courseChapterRequest.getCourseId(),courseChapterRequest.getUserName());

        if(c != null) {
            return ResponseEntity.of(Optional.of(c));
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
