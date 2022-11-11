package com.robosoft.editprofile.AdminPanelS.service;

import com.robosoft.editprofile.AdminPanelS.entity.Enrollment;
import com.robosoft.editprofile.AdminPanelS.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public OverviewResponse getOverviewOfCourse(int courseId) {
        try {
            return jdbcTemplate.queryForObject("SELECT courseName,coursePhoto,categoryName,chapterCount,lessonCount,courseTagLine,previewVideo,overview.description,testCount,courseMaterialId,courseDuration,learningOutCome,requirements,instructorName,url,profilePhoto,instructor.description AS instructorDescription FROM overview INNER JOIN instructor ON overview.instructorId = instructor.instructorId  INNER JOIN course ON overview.courseId = course.courseId AND course.courseId = ? INNER JOIN category ON course.categoryId = category.categoryId", new BeanPropertyRowMapper<>(OverviewResponse.class), courseId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<CourseResponse> getBasicCourses(int categoryId) {
        return jdbcTemplate.query("SELECT courseName,previewVideo,chapterCount,courseDuration FROM overView INNER JOIN course ON course.courseId = overview.courseId WHERE categoryId = " + categoryId + " AND difficultyLevel = 'Beginner'", new BeanPropertyRowMapper<>(CourseResponse.class));
    }

    public List<CourseResponse> getAdvanceCourses(int categoryId) {
        return jdbcTemplate.query("SELECT courseName,previewVideo,chapterCount,courseDuration FROM overView INNER JOIN course ON course.courseId = overview.courseId WHERE categoryId = " + categoryId + " AND difficultyLevel = 'Advanced'", new BeanPropertyRowMapper<>(CourseResponse.class));
    }

    public List<AllCoursesResponse> getAllCoursesOf(int categoryId) {
        return jdbcTemplate.query("SELECT coursePhoto,courseName,chapterCount,categoryName FROM overView INNER JOIN course ON course.courseId = overview.courseId INNER JOIN category ON category.categoryId = course.categoryId WHERE category.categoryId = " + categoryId + " ", new BeanPropertyRowMapper<>(AllCoursesResponse.class));
    }

    public String getPolicy() {
        return jdbcTemplate.queryForObject("SELECT privacyPolicy FROM policy WHERE policyId=(SELECT max(policyId) FROM policy)", String.class);
    }

    public String getTermsAndConditions() {
        return jdbcTemplate.queryForObject("SELECT termsAndConditions FROM policy WHERE policyId=(SELECT max(policyId) FROM policy)", String.class);
    }

    public List<OngoingResponse> getOngoingCourses(String userName) {
        List<OngoingResponse> ongoingResponses = new ArrayList<>();

        List<Integer> courseId = jdbcTemplate.queryForList("SELECT courseId FROM enrollment WHERE  userName = ?", Integer.class, userName);

        for (Integer i : courseId) {
            Integer completedChapter = jdbcTemplate.queryForObject("SELECT count(chapterId) FROM chapterProgress WHERE courseId = ? AND userName = ? AND chapterCompletedStatus = true", Integer.class, i, userName);
            Integer totalChapter = jdbcTemplate.queryForObject("SELECT count(chapterId) FROM chapterProgress WHERE courseId = ? AND userName = ?", Integer.class, i, userName);
            if (completedChapter < totalChapter) {
                OngoingResponse ongoingResponse = jdbcTemplate.queryForObject("SELECT courseName,coursePhoto FROM course WHERE courseId = ?", new BeanPropertyRowMapper<>(OngoingResponse.class), i);
                ongoingResponse.setCompletedChapter(completedChapter);
                ongoingResponse.setTotalChapter(totalChapter);
                ongoingResponses.add(ongoingResponse);
            }
        }
        return ongoingResponses;
    }


    public CourseChapterResponse getCourseChapterResponse(Integer courseId, String userName) {
        try {
        List<Integer> chapterIds = jdbcTemplate.queryForList("SELECT chapterId from chapter WHERE courseId = ?", Integer.class, courseId);
        CourseChapterResponse courseChapterResponse = jdbcTemplate.queryForObject("SELECT courseName,categoryName,chapterCount,lessonCount,testCount,courseDuration,courseCompletedStatus FROM overView INNER JOIN course ON Overview.courseId = course.courseId AND course.courseId = ? INNER JOIN category ON course.categoryId = category.categoryId INNER JOIN courseProgress on course.courseId = courseProgress.courseId AND userName = ?", new BeanPropertyRowMapper<>(CourseChapterResponse.class), courseId,userName);
        List<ChapterResponse> chapterResponses = new ArrayList<>();
        for (Integer i : chapterIds) {
            chapterResponses.add(getChapterResponse(userName, i));
        }
        courseChapterResponse.setChapterResponses(chapterResponses);
        return courseChapterResponse;
        }catch (Exception e)
        {
            List<Integer> chapterIds = jdbcTemplate.queryForList("SELECT chapterId from chapter WHERE courseId = ?", Integer.class, courseId);
            CourseChapterResponse courseChapterResponse = jdbcTemplate.queryForObject("SELECT courseName,categoryName,chapterCount,lessonCount,testCount,courseDuration FROM overView INNER JOIN course ON Overview.courseId = course.courseId AND course.courseId = ? INNER JOIN category ON course.categoryId = category.categoryId", new BeanPropertyRowMapper<>(CourseChapterResponse.class), courseId);
            List<ChapterResponse> chapterResponses = new ArrayList<>();
            for (Integer i : chapterIds) {
                chapterResponses.add(getChapterResponse(userName, i));
            }
            courseChapterResponse.setChapterResponses(chapterResponses);
            return courseChapterResponse;
        }
    }

    public ChapterResponse getChapterResponse(String userName, Integer chapterId) {
        try {
        return jdbcTemplate.queryForObject("SELECT chapterNumber,chapterName,chapterCompletedStatus FROM chapter INNER JOIN chapterProgress ON chapter.chapterId = chapterProgress.chapterId WHERE chapter.chapterId = ? AND chapterProgress.userName = ?", new BeanPropertyRowMapper<>(ChapterResponse.class), chapterId, userName);

        } catch (Exception e) {
            return jdbcTemplate.queryForObject("SELECT chapterNumber,chapterName FROM chapter WHERE chapterId = ?", new BeanPropertyRowMapper<>(ChapterResponse.class), chapterId);
        }
    }

    public List<LessonResponse> getLessonResponses(Integer chapterId, String userName) {
        try {
            List<Integer> lessonIds = jdbcTemplate.queryForList("SELECT lessonId from lesson WHERE chapterId = ?", Integer.class, chapterId);
            List<LessonResponse> lessonResponses = new ArrayList<>();
            for (Integer lessonId : lessonIds) {
                try {
                     lessonResponses.add(jdbcTemplate.queryForObject("SELECT lessonNumber,lessonName,lessonDuration,videoLink,lessonCompletedStatus FROM lesson INNER JOIN lessonProgress on lesson.lessonId = lessonProgress.lessonId AND lessonProgress.userName = ? AND lesson.lessonId = ?", new BeanPropertyRowMapper<>(LessonResponse.class), userName, lessonId));
                } catch (Exception e) {
                    String lessonName = jdbcTemplate.queryForObject("SELECT lessonName FROM lesson WHERE lessonId = ?", String.class, lessonId);
                    if (lessonName.equalsIgnoreCase("Introduction")) {
                         lessonResponses.add(jdbcTemplate.queryForObject("SELECT lessonNumber,lessonName,lessonDuration,videoLink FROM lesson WHERE lessonId = ?", new BeanPropertyRowMapper<>(LessonResponse.class), lessonId));
                    }
                    lessonResponses.add(jdbcTemplate.queryForObject("SELECT lessonNumber,lessonName,lessonDuration FROM lesson WHERE lessonId = ?", new BeanPropertyRowMapper<>(LessonResponse.class), lessonId));
                }
            }return lessonResponses;
        }catch(Exception e){
                return null;
        }
    }

    public List<CompletedResponse> getCourseCompletedResponse(String userName)
    {
        try {
            return jdbcTemplate.query("SELECT courseName,coursePercentage,coursePhoto FROM course INNER JOIN courseProgress ON course.courseId = courseProgress.courseId AND courseProgress.userName = ? AND courseProgress.courseCompletedStatus = true",new BeanPropertyRowMapper<>(CompletedResponse.class),userName);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Continue getLastPlayed(Integer courseId , String userName)
    {
        try {
            jdbcTemplate.queryForObject("SELECT userName FROM enrollment WHERE courseId = ? AND userName = ?",new BeanPropertyRowMapper<>(Enrollment.class),courseId,userName);
            List<Integer> chapterIds = jdbcTemplate.queryForList("SELECT chapterId FROM chapter WHERE courseId = ?", Integer.class,courseId);
            int lessonId = 0;
            for(int chapterId :chapterIds)
            {
                System.out.println(chapterId);
                lessonId = jdbcTemplate.queryForObject("SELECT lesson.lessonId FROM lesson INNER JOIN lessonProgress ON lesson.lessonId = lessonProgress.lessonId WHERE lessonProgress.pauseTime < lesson.lessonDuration  AND lessonProgress.pauseTime > '00.00.00' AND lessonProgress.userName = ? AND lesson.chapterId = ?", Integer.class,userName,chapterId);
                break;
            }
            return jdbcTemplate.queryForObject("SELECT chapterNumber,lessonNumber,lesson.lessonId,pauseTime,videoLink FROM lesson INNER JOIN lessonProgress ON lesson.lessonId = lessonProgress.lessonId INNER JOIN chapter ON lesson.chapterId = chapter.chapterId AND lesson.lessonId = ? AND lessonProgress.userName = ?", new BeanPropertyRowMapper<>(Continue.class),lessonId,userName);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}