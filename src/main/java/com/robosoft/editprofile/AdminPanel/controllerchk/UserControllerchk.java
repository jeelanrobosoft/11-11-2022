package com.robosoft.editprofile.AdminPanel.controllerchk;

import com.robosoft.editprofile.AdminPanel.requestchk.EnrollmentRequestchk;
import com.robosoft.editprofile.AdminPanel.requestchk.HomeHeaderRequestchk;
import com.robosoft.editprofile.AdminPanel.requestchk.VideoPauseRequestchk;
import com.robosoft.editprofile.AdminPanel.responsechk.HomeAllCoursechk;
import com.robosoft.editprofile.AdminPanel.responsechk.HomeResponseTopHeaderchk;
import com.robosoft.editprofile.AdminPanel.responsechk.PopularCourseInEachCategorychk;
import com.robosoft.editprofile.AdminPanel.servicechk.UserServicechk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserControllerchk
{
    @Autowired
    private UserServicechk userService;

    @GetMapping("/home/course")
    public ResponseEntity<?> homeTopBarData(@RequestBody HomeHeaderRequestchk homeHeaderRequest)
    {
        List<HomeResponseTopHeaderchk> coursesList= userService.HomePageTopBar(homeHeaderRequest);
        if(coursesList.size() ==0)
        {
            return new ResponseEntity<String>("courses are not available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(coursesList,HttpStatus.OK);
    }


    @GetMapping("/home/course/all")
    public ResponseEntity<?> homeAllCourses()
    {
        List<HomeAllCoursechk> allCourses= userService.getAllCourses();
        if(allCourses.size() ==0)
        {
            return new ResponseEntity<String>("courses are not available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allCourses,HttpStatus.OK);
    }


    @GetMapping("/home/course/popular")
    public ResponseEntity<?> homePopularCourses()
    {
        List<HomeAllCoursechk> popularCourses= userService.getPopularCourses();
        if(popularCourses.size() ==0)
        {
            return new ResponseEntity<String>("courses are not available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(popularCourses,HttpStatus.OK);
    }

    @GetMapping("/home/course/newest")
    public ResponseEntity<?> homeNewestCourses() {
        List<HomeAllCoursechk> newestCourses = userService.getNewCourses();
        if (newestCourses.size() == 0) {
            return new ResponseEntity<String>("courses are not available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newestCourses, HttpStatus.OK);
    }

       //*********************10-11-20222*****************************************
       @GetMapping("/home/course/category")
       public ResponseEntity<?> homeGetPopularCoursesOfCategory() {
           Map<Integer,List<PopularCourseInEachCategorychk>> coursesOfCategory = userService.popularCoursesInCategory();
           if (coursesOfCategory == null) {
               return new ResponseEntity<String>("courses are not available", HttpStatus.NOT_FOUND);
           }
           return new ResponseEntity<>(coursesOfCategory, HttpStatus.OK);
       }


        //***********************10-11-2022***************************************
    @PostMapping("/enroll")
    public ResponseEntity<String> userEnrollment(@RequestBody EnrollmentRequestchk enrollmentRequest)
    {
        String enrolResponse = userService.enrollment(enrollmentRequest);
        return new ResponseEntity<>(enrolResponse, HttpStatus.OK);
    }

    //****************************11-11-2022*********************************************
    @PutMapping("/pauseTime")
    public ResponseEntity<String> updateLessonCompletionStatus(@RequestBody VideoPauseRequestchk videoPauseRequest)
    {
        userService.updateVideoPauseTime(videoPauseRequest);
        return new ResponseEntity<>("Updated SuccessFully",HttpStatus.OK);
    }


}


