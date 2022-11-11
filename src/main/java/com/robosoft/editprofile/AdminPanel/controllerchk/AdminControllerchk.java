package com.robosoft.editprofile.AdminPanel.controllerchk;

import com.robosoft.editprofile.AdminPanel.entitychk.Questionchk;
import com.robosoft.editprofile.AdminPanel.requestchk.CourseRequestchk;
import com.robosoft.editprofile.AdminPanel.requestchk.InstructorRequestchk;
import com.robosoft.editprofile.AdminPanel.requestchk.LessonRequestchk;
import com.robosoft.editprofile.AdminPanel.requestchk.TestRequestchk;
import com.robosoft.editprofile.AdminPanel.servicechk.AdminServicechk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
public class AdminControllerchk
{
    @Autowired
    private AdminServicechk adminService;

    @PostMapping("/course")
    public ResponseEntity<String> saveCourse(@ModelAttribute CourseRequestchk courseRequest) throws IOException
    {
        String courseResponse = adminService.addCourse(courseRequest);
        if(courseResponse == null)
        {
            return new ResponseEntity<String>("course addition is unsuccessful", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<String>(courseResponse, HttpStatus.OK);
    }

    @PostMapping("/lesson")
    public ResponseEntity<String> saveLesson(@ModelAttribute LessonRequestchk lessonRequest) throws IOException, ParseException {
        String lessonResponse= adminService.addLesson(lessonRequest);
        if(lessonResponse == null)
        {
            return new ResponseEntity<String>("lesson addition is unsuccessful", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(lessonResponse, HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity<String> saveTest(@RequestBody TestRequestchk testRequest)
    {
        String testResponse = adminService.addTest(testRequest);
        if(testResponse == null)
        {
            return new ResponseEntity<String>("test addition is unsuccessful", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(testResponse,HttpStatus.OK);
    }

    @PostMapping("/question")
    public ResponseEntity<String> saveQuestion(@RequestBody Questionchk question)
    {
        String questionResponse = adminService.addQuestion(question);
        if(questionResponse == null)
        {
            return new ResponseEntity<String>("question addition is unsuccessful", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(questionResponse,HttpStatus.OK);
    }
    @PostMapping("/instructor")
    public ResponseEntity<String> saveInstructor(@ModelAttribute InstructorRequestchk instructor) throws IOException {
        String instructorResponse = adminService.addInstructor(instructor);
        if(instructorResponse == null)
        {
            return new ResponseEntity<String>("instructor addition is unsuccessful", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(instructorResponse,HttpStatus.OK);
    }

}
