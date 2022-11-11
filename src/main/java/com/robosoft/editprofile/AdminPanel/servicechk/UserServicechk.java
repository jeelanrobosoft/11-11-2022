package com.robosoft.editprofile.AdminPanel.servicechk;

import com.robosoft.editprofile.AdminPanel.entitychk.*;
import com.robosoft.editprofile.AdminPanel.requestchk.EnrollmentRequestchk;
import com.robosoft.editprofile.AdminPanel.requestchk.HomeHeaderRequestchk;
import com.robosoft.editprofile.AdminPanel.requestchk.VideoPauseRequestchk;
import com.robosoft.editprofile.AdminPanel.responsechk.HomeAllCoursechk;
import com.robosoft.editprofile.AdminPanel.responsechk.HomeResponseTopHeaderchk;
import com.robosoft.editprofile.AdminPanel.responsechk.PopularCourseInEachCategorychk;
import com.robosoft.editprofile.AdminPanel.responsechk.VideoPauseResponsechk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServicechk
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // for home top header
    private String GET_OCCUPATION = "SELECT occupation FROM user WHERE userName = ?";
    private String HOME_TOP_BAR = "SELECT coursePhoto, courseName FROM course";
    private  String COURSE_EXISTANCE_IN_SUBCATEGORY = "SELECT coursePhoto, courseName FROM course WHERE subCategoryId= ?";
    private String GET_CATEGORY_FROM_SUBCATEGORY = "Select categoryId from subCategory WHERE subcategoryId = ?";
    private String GET_COURSES = "SELECT * FROM course WHERE categoryId = ?";

    // "All" in home page
    private String GET_ALL_COURSES = "SELECT coursePhoto, courseName,categoryId, chapterCount FROM course,overView WHERE course.courseId = overView.courseId";
   //"popular" in home page
    private String GET_ALL_ENROLLED_COURSES = "SELECT distinct courseId FROM enrollment";
    private String GET_ENROLLMENT_COUNT = "SELECT count(courseId) FROM enrollment WHERE courseId= ?";
    private String GET_ALL_POPULAR_COURSES = "SELECT c.coursePhoto, c.courseName,c.categoryId, o.chapterCount FROM course c,overView o WHERE c.courseId=? and c.courseId = o.courseId";




    // "newest" in home
    private String GET_ALL_NEW_COURSES = "SELECT course.courseId, coursePhoto, courseName,categoryId, chapterCount FROM course,overview WHERE course.courseId = overview.courseId";

    //enrollment(10-11-200)
    private String ALREADY_ENROLLED_STATUS = "SELECT * FROM enrollment WHERE userName= ? and courseId = ?";
    private String GET_ENROLLED = "INSERT INTO enrollment(userName,courseId,joinDate) values(?,?,?)";
    private String ADD_TO_COURSE_PROGRESS = "INSERT INTO courseProgress(userName,courseId) values(?,?)";
    private String GET_ALL_CHAPTERS_UNDER_COURSE = "SELECT * FROM chapter WHERE courseId = ?";
    private String ADD_CHAPTER_PROGRESS = "INSERT INTO chapterProgress(userName,courseId,chapterId,testId) values(?,?,?,?)";
    private String GET_TEST_OF_CHAPTER = "SELECT testId FROM test WHERE chapterId = ?";
    private String GET_ALL_LESSONS_OF_CHAPTER = "SELECT * FROM lesson WHERE chapterId = ?";
    private String ADD_LESSON_PROGRESS = "INSERT INTO lessonProgress(userName,chapterId,lessonId) values(?,?,?)";

    //home page "popular courses in category" part
    private String GET_CATEGORIES = "SELECT * FROM category";
    private String GET_CATEGORY_ENROLLED_COUNT = "SELECT count(c.courseId) FROM enrollment e, course c , category ct WHERE  ct.categoryId = ? and ct.categoryId = c.categoryId and c.courseId = e.courseId";
    private String GET_COURSES_UNDER_EACH_CATEGORY = "SELECT c.courseName,o.chapterCount, c.courseDuration,c.previewVideo from course c, overview o , category ct WHERE ct.categoryId = ? and  ct.categoryId = c.categoryId and c.courseId = o.courseId";


    //**************************11-11-2022*********************************************************************************************************
    private String UPDATE_PAUSE_TIME = "UPDATE lessonProgress SET pauseTime=? WHERE lessonId=? and userName=? and chapterId=?";
    private String GET_PAUSE_TIME = "SELECT  userName,  pauseTime,  lessonId, chapterId FROM lessonProgress  WHERE lessonId=? and userName=? and chapterId=?";
    private String GET_LESSON_DURATION = "SELECT lessonDuration FROM lesson WHERe lessonId=?";
    private String UPDATE_LESSON_COMPLETION_STATUS = "UPDATE lessonProgress SET lessonCompletedStatus=? WHERe lessonId = ?";
    private String GET_CHAPTER_ID = "SELECT chapterId FROm lESSOn WHERE lessonId= ?";
    private String GET_CHAPTER_COMPLETION_STATUS = "SELECT chapterCompletedStatus FROM chapterProgress WHERE chapterId = ? and userName=? and courseId=? and testId=(SELECT testId FROM chapterProgress WHERE chapterId = ? and userName=? and courseId=?)";
    private String GET_ALL_LESSONS_UNDER_CHAPTER= "SELECT l.lessonId,l.lessonNumber,l.chapterId,l.lessonName,l.lessonDuration,l.videoLink FROM lesson l, chapter ch WHERE l.chapterId = ch.chapterId and ch.chapterId=?";
    private String GET_LESSON_COMPLETION_STATUS = "SELECT lessonCompletedStatus FROM lessonProgress WHERE lessonId=? and chapterId=? and username=?";
    private String UPDATE_CHAPTER_COMPLETION_STATUS = "UPDATE chapterProgress SET chapterCompletedStatus=? WHERE chapterId = ? and courseId=? and userName=?";
    private String GET_ALL_CHAPTERS_UNDER_A_COURSE = "SELECT * FROM chapterProgress WHERE courseId = ? and chapterId = ? and userName = ?";
    private String CHAPTER_COMPLETION_STATUS = "SELECT chapterCompletedStatus FROM chapterProgress WHERE chapterId=? and courseId=? and userName=?";
    private String UPDATE_COURSE_COMPLETED_STATUS = "UPDATE courseProgress SET courseCompletedStatus = ? WHERE courseId=? and userName=?";
    private String UPDATE_COURSE_COMPLETION_DATE= "UPDATE enrollment SET completedDate=? WHERE courseId=? and userName=?";
    //if user occupation is student display all courses, if not, based on subcategory matching occupation display courses of subcategory ,if subcategory is not having any courses then tahke
    // category of the sub category and display courses under that category
    public List<HomeResponseTopHeaderchk> HomePageTopBar(HomeHeaderRequestchk homeHeaderRequest)   // front end should send username when ever they call home api as a response
    {
        User user= jdbcTemplate.queryForObject(GET_OCCUPATION,(rs, rowNum) -> {
            return new User(rs.getInt("occupation"));
        }, homeHeaderRequest.getUserName());
        System.out.println(user.getOccupation());
        if(user.getOccupation() == 0)
        {
            List<HomeResponseTopHeaderchk> courseListForStudent = jdbcTemplate.query(HOME_TOP_BAR,(rs, rowNum) -> {
                return new HomeResponseTopHeaderchk(rs.getString("coursePhoto"), rs.getString("courseName"));
            });
            return courseListForStudent;
        }
        else
        {
            try
            {
                List<HomeResponseTopHeaderchk> course = jdbcTemplate.query(COURSE_EXISTANCE_IN_SUBCATEGORY,(rs, rowNum) -> {
                    return new HomeResponseTopHeaderchk(rs.getString("coursePhoto"), rs.getString("courseName"));
                }, user.getOccupation());
                if(course.size() !=0)
                {
                    return course;
                }
            }
            catch(NullPointerException e)
            {
                int categoryId = jdbcTemplate.queryForObject(GET_CATEGORY_FROM_SUBCATEGORY, new Object[] {user.getOccupation()}, Integer.class);
                List<HomeResponseTopHeaderchk> courses = jdbcTemplate.query(GET_COURSES,(rs, rowNum) -> {
                    return new HomeResponseTopHeaderchk(rs.getString("coursePhoto"), rs.getString("courseName"));
                }, categoryId);
                return courses;
            }
        }
        return null;
    }

    //"All" in home page display all the courses
    public List<HomeAllCoursechk> getAllCourses()
    {
        List<HomeAllCoursechk> allCourses = jdbcTemplate.query(GET_ALL_COURSES,(rs, rowNum) -> {
            return new HomeAllCoursechk(rs.getString("coursePhoto"), rs.getString("courseName"), rs.getInt("categoryId"),rs.getInt("chapterCount"));
        });
        return allCourses;
    }

    //"popular" in home page , filter all the courses with maximum enrollments more than 5
    public List<HomeAllCoursechk> getPopularCourses()
    {
        List<HomeAllCoursechk> popularCourseList = new ArrayList<>();
        List<Enrollmentchk> allEnrolledCourses = jdbcTemplate.query(GET_ALL_ENROLLED_COURSES,(rs, rowNum) -> {
            return new Enrollmentchk(rs.getInt("courseId"));
        });
        System.out.println(allEnrolledCourses);
        for(int i=0;i<allEnrolledCourses.size();i++)
        {
                int enrolmentCount = jdbcTemplate.queryForObject(GET_ENROLLMENT_COUNT, new Object[] {allEnrolledCourses.get(i).getCourseId()}, Integer.class);
                if(enrolmentCount >1)
                {
                    HomeAllCoursechk homeAllCourse= jdbcTemplate.queryForObject(GET_ALL_POPULAR_COURSES,(rs, rowNum) -> {
                        return new HomeAllCoursechk(rs.getString("coursePhoto"), rs.getString("courseName"), rs.getInt("categoryId"),rs.getInt("chapterCount"));
                    },allEnrolledCourses.get(i).getCourseId());
                    popularCourseList.add(homeAllCourse);
                }
        }
        System.out.println(popularCourseList);
     return popularCourseList;
     }


     public List<HomeAllCoursechk> getNewCourses()
     {
         List<HomeAllCoursechk> newCourseList = new ArrayList<>();
         List<HomeAllCoursechk> allNewCourses = jdbcTemplate.query(GET_ALL_NEW_COURSES,(rs, rowNum) -> {
             return new HomeAllCoursechk(rs.getInt("courseId"),rs.getString("coursePhoto"), rs.getString("courseName"), rs.getInt("categoryId"),rs.getInt("chapterCount"));
         });
         //System.out.println(allNewCourses.size());
         int size = allNewCourses.size()-1;
         for(int i=size;i>0;i--)
         {
             //System.out.println(allNewCourses.get(i).getCourseId());
             HomeAllCoursechk homeAllCourse= jdbcTemplate.queryForObject(GET_ALL_POPULAR_COURSES,(rs, rowNum) -> {
                 return new HomeAllCoursechk(rs.getString("coursePhoto"), rs.getString("courseName"), rs.getInt("categoryId"),rs.getInt("chapterCount"));
             },allNewCourses.get(i).getCourseId());
             newCourseList.add(homeAllCourse);
         }
         return newCourseList;
     }



     //10-11-2022**********************************************************************************

    public Map<Integer,List<PopularCourseInEachCategorychk>> popularCoursesInCategory()
    {
        Map<Integer,List<PopularCourseInEachCategorychk>> topCoursesList  = new HashMap<>();
        List<Categorychk> categoriesList = jdbcTemplate.query(GET_CATEGORIES,(rs, rowNum) -> {
            return new Categorychk(rs.getInt("categoryId"), rs.getString("categoryName"), rs.getString("categoryPhoto"));
        });
        if(categoriesList.size() == 0)
        {
            return null;
        }
        for(int i=0;i<categoriesList.size();i++)
        {
           int enrollmentCount = jdbcTemplate.queryForObject(GET_CATEGORY_ENROLLED_COUNT, new Object[] {categoriesList.get(i).getCategoryId()}, Integer.class);
           if(enrollmentCount >2)
           {
               try
               {
                   List<PopularCourseInEachCategorychk> popularCourseInEachCategory = jdbcTemplate.query(GET_COURSES_UNDER_EACH_CATEGORY,(rs, rowNum) -> {
                       return new PopularCourseInEachCategorychk(rs.getString("courseName"), rs.getInt("chapterCount"), rs.getString("courseDuration"),rs.getString("previewVideo"));
                   },categoriesList.get(i).getCategoryId());
                   System.out.println(popularCourseInEachCategory);
                   topCoursesList.put(categoriesList.get(i).getCategoryId(),popularCourseInEachCategory);

               }
               catch(EmptyResultDataAccessException expn)
               {
                   return null;
               }
           }
        }
        return topCoursesList;
    }


    public String enrollment(EnrollmentRequestchk enrollmentRequest)
    {
        try
        {
            Enrollmentchk enrollmentRequest1 = jdbcTemplate.queryForObject(ALREADY_ENROLLED_STATUS,(rs, rowNum) -> {
                return new Enrollmentchk(rs.getString("userName"), rs.getInt("courseId"), rs.getDate("joinDate"),rs.getDate("completedDate"),rs.getInt("courseScore"));
            },enrollmentRequest.getUserName(),enrollmentRequest.getCourseId());
            return "You have already enrolled for this course";
        }
        catch(EmptyResultDataAccessException e)
        {
           jdbcTemplate.update(GET_ENROLLED,enrollmentRequest.getUserName(), enrollmentRequest.getCourseId(), enrollmentRequest.getJoinDate());
           jdbcTemplate.update(ADD_TO_COURSE_PROGRESS, enrollmentRequest.getUserName(), enrollmentRequest.getCourseId());
           List<Chapterchk> chaptersOfCourse = jdbcTemplate.query(GET_ALL_CHAPTERS_UNDER_COURSE,(rs, rowNum) -> {
               return new Chapterchk(rs.getInt("chapterId"), rs.getInt("courseId"), rs.getInt("chapterNumber"),rs.getString("chapterName"),rs.getString("chapterDuration"));
           },enrollmentRequest.getCourseId());

           if(chaptersOfCourse.size() == 0)
           {
               return "Chapters are not yet added for this course";
           }
           else
           {
               for(int i=0;i<chaptersOfCourse.size();i++)
               {
                   try
                   {
                       int testIdOfChapter = jdbcTemplate.queryForObject(GET_TEST_OF_CHAPTER, new Object[] {chaptersOfCourse.get(i).getChapterId()}, Integer.class);
                       jdbcTemplate.update(ADD_CHAPTER_PROGRESS, enrollmentRequest.getUserName(), enrollmentRequest.getCourseId(),chaptersOfCourse.get(i).getChapterId(),testIdOfChapter);
                   }
                   catch(EmptyResultDataAccessException ex)
                   {
                       return "Test for this course is not yet been added";
                   }
                   List<Lessonchk> lessonsOfChapter = jdbcTemplate.query(GET_ALL_LESSONS_OF_CHAPTER,(rs, rowNum) -> {
                       return new Lessonchk(rs.getInt("lessonId"), rs.getInt("lessonNumber"), rs.getInt("chapterId"),rs.getString("lessonName"), rs.getString("lessonduration"),rs.getString("videoLink"));
                   },chaptersOfCourse.get(i).getChapterId());

                   if(lessonsOfChapter.size() == 0)
                   {
                       return "Lessons are not yet added for this course";
                   }
                   else
                   {
                      for(int j=0;j<lessonsOfChapter.size();j++)
                      {
                         jdbcTemplate.update(ADD_LESSON_PROGRESS, enrollmentRequest.getUserName(),chaptersOfCourse.get(i).getChapterId(),lessonsOfChapter.get(j).getLessonId());
                      }
                   }
               }
           }
        }
        return "You have enrolled to the course successfully";
    }




    //*******************************11-11-2022*************************************************************************
    public void updateVideoPauseTime(VideoPauseRequestchk videoPauseRequest)
    {
        boolean status=false;
        boolean chapterStatus= false;
        System.out.println(videoPauseRequest.getPauseTime());
        Time pauseTime = videoPauseRequest.getPauseTime();
        String videoPauseTime =pauseTime.toString();

        jdbcTemplate.update(UPDATE_PAUSE_TIME,videoPauseTime, videoPauseRequest.getLessonId(), videoPauseRequest.getUserName(),videoPauseRequest.getChapterId());
        VideoPauseResponsechk videoPauseResponse1 = jdbcTemplate.queryForObject(GET_PAUSE_TIME,(rs, rowNum) -> {
            return new VideoPauseResponsechk(rs.getString("userName"),rs.getString("pauseTime"), rs.getInt("lessonId"), rs.getInt("chapterId"));
        },videoPauseRequest.getLessonId(),videoPauseRequest.getUserName(),videoPauseRequest.getChapterId());


        String lessonDuration = jdbcTemplate.queryForObject(GET_LESSON_DURATION, new Object[] {videoPauseRequest.getLessonId()}, String.class);

        if(lessonDuration.equals(videoPauseResponse1.getPauseTime()))
        {
            jdbcTemplate.update(UPDATE_LESSON_COMPLETION_STATUS, true,videoPauseRequest.getLessonId());
        }

        Integer chapterId = jdbcTemplate.queryForObject(GET_CHAPTER_ID, new Object[] {videoPauseRequest.getLessonId()}, Integer.class);
        Boolean chapterCompletedStatus = jdbcTemplate.queryForObject(GET_CHAPTER_COMPLETION_STATUS, new Object[] {chapterId,videoPauseRequest.getUserName(),videoPauseRequest.getCourseId(), videoPauseRequest.getChapterId(),videoPauseRequest.getUserName(), videoPauseRequest.getCourseId()}, Boolean.class);

        if(chapterCompletedStatus == false)
        {
            List<Lessonchk> lessons = jdbcTemplate.query(GET_ALL_LESSONS_UNDER_CHAPTER,(rs, rowNum) -> {
                return new Lessonchk(rs.getInt("lessonId"), rs.getInt("lessonNumber"),rs.getInt("chapterId"),rs.getString("lessonName"),rs.getString("lessonDuration"),rs.getString("videoLink"));
            }, chapterId);

            for(int i=0;i<lessons.size();i++)
            {
               boolean  lessonCompletedStatus = jdbcTemplate.queryForObject(GET_LESSON_COMPLETION_STATUS,new Object[] {lessons.get(i).getLessonId(), videoPauseRequest.getChapterId(),videoPauseRequest.getUserName()}, Boolean.class);
               if(lessonCompletedStatus == true)
               {
                   status = true;
               }
            }

            if(status == true)
            {
                 jdbcTemplate.update(UPDATE_CHAPTER_COMPLETION_STATUS,true,chapterId,videoPauseRequest.getCourseId(), videoPauseRequest.getUserName());
            }
        }
        List<ChapterProgresschk> chaptersList = jdbcTemplate.query(GET_ALL_CHAPTERS_UNDER_A_COURSE, new BeanPropertyRowMapper<>(ChapterProgresschk.class),videoPauseRequest.getCourseId(), videoPauseRequest.getChapterId(), videoPauseRequest.getUserName());
        for(int i=0;i<chaptersList.size();i++)
        {
            boolean  chapterCompletionStatus = jdbcTemplate.queryForObject(CHAPTER_COMPLETION_STATUS,new Object[] {videoPauseRequest.getChapterId(),videoPauseRequest.getCourseId(),videoPauseRequest.getUserName()}, Boolean.class);
            if(chapterCompletedStatus == true)
            {
               chapterStatus = true;
            }
        }

        if(chapterStatus == true)
        {
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            jdbcTemplate.update(UPDATE_COURSE_COMPLETED_STATUS,true,videoPauseRequest.getCourseId(),videoPauseRequest.getUserName());
            jdbcTemplate.update(UPDATE_COURSE_COMPLETION_DATE,date,videoPauseRequest.getCourseId(), videoPauseRequest.getUserName());
        }
    }
}
