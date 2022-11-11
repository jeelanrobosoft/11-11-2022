package com.robosoft.editprofile.AdminPanel.servicechk;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.robosoft.editprofile.AdminPanel.entitychk.Chapterchk;
import com.robosoft.editprofile.AdminPanel.entitychk.Questionchk;
import com.robosoft.editprofile.AdminPanel.requestchk.CourseRequestchk;
import com.robosoft.editprofile.AdminPanel.requestchk.InstructorRequestchk;
import com.robosoft.editprofile.AdminPanel.requestchk.LessonRequestchk;
import com.robosoft.editprofile.AdminPanel.requestchk.TestRequestchk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import static com.robosoft.editprofile.AdminPanel.commonchk.Constantschk.*;

@Service
public class AdminServicechk
{
    @Autowired
    JdbcTemplate jdbcTemplate;
    // to add course
    private String ADD_COURSE = "INSERT INTO course(courseId,coursePhoto,courseName,previewVideo,categoryId,subCategoryId) VALUES(?,?,?,?,?,?)";
    // to add lesson
    private String ADD_LESSON = "INSERT INTO lesson(lessonNumber,courseId,chapterId,lessonName,lessonDuration,videoLink) VALUES(?,?,?,?,?,?)";
    private String GET_CHAPTER_DURATION = "SELECT chapterDuration FROM chapter WHERE chapterId = ?";
    private String UPDATE_CHAPTER_DURATION = "UPDATE chapter SET chapterDuration = ? WHERE chapterId = ?";
    private String GET_COURSE_DURATION = "SELECT courseDuration FROM course WHERE courseId = ?";
    private String GET_COURSES_UNDER_A_CHAPTER = "SELECT chapterDuration FROM chapter WHERE courseId= ?";
    private String UPDATE_COURSE_DURATION = "UPDATE course SET courseDuration = ? WHERE courseId= ?";
    //add test
    private String ADD_TEST = "INSERT INTO test(testId,testName,chapterId,testDuration,passingGrade) values(?,?,?,?,?)";
    private String GET_EXISTING_QUESTIONS_COUNT = "SELECT questionsCount FROM test WHERE testId = ?";
    private String ADD_QUESTION = "INSERT INTO question(questionId,questionName,testId, option_1, option_2,option_3,option_4,correctAnswer) values(?,?,?,?,?,?,?,?)";
    private String UPDATE_QUESTIONS_COUNT = "UPDATE test SET questionsCount = ? where testId = ?";
    private String SET_COURSE_DURATION_TO_ZERO = "UPDATE course SET courseDuration= ? WHERE courseId = ?";
    //add instructor
    private String ADD_INSTRUCTOR = "INSERT INTO instructor(instructorId, instructorName,url,description,profilePhoto) values(?,?,?,?,?)";

    private File convertMultiPartToFile(MultipartFile file) throws IOException
    {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    private String generateFileName(MultipartFile multiPart)
    {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

    public String getFileUrl(MultipartFile multipartFile) throws IOException
    {
        String objectName = generateFileName(multipartFile);
        FileInputStream serviceAccount = new FileInputStream(FIREBASE_SDK_JSON);

        File file = convertMultiPartToFile(multipartFile);
        Path filePath = file.toPath();

        Storage storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).setProjectId(FIREBASE_PROJECT_ID).build().getService();
        BlobId blobId = BlobId.of(FIREBASE_BUCKET, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build();
        storage.create(blobInfo, Files.readAllBytes(filePath));
        Blob blob = storage.create(blobInfo, Files.readAllBytes(filePath));
        System.out.println(String.format(DOWNLOAD_URL, URLEncoder.encode(objectName)));
        return String.format(DOWNLOAD_URL, URLEncoder.encode(objectName));
    }
    // To add courses
    public String addCourse(CourseRequestchk courseRequest) throws IOException
    {
            String coursePhotoLink = getFileUrl(courseRequest.getCoursePhoto());
            String courseVideoLink = getFileUrl(courseRequest.getPreviewVideo());
            jdbcTemplate.update(ADD_COURSE, courseRequest.getCourseId(), coursePhotoLink,courseRequest.getCourseName(),courseVideoLink,courseRequest.getCategoryId(), courseRequest.getSubCategoryId());
            return "Course Added SuccessFully";
    }
    //registering lessons under chapters
    public String addLesson(LessonRequestchk lessonRequest) throws IOException, ParseException {
        String lessonVideoLink = getFileUrl(lessonRequest.getVideoLink());
        String lessonTime = lessonRequest.getLessonDuration().toString();
        jdbcTemplate.update(ADD_LESSON,lessonRequest.getLessonNumber(), lessonRequest.getCourseId(), lessonRequest.getChapterId(),lessonRequest.getLessonName(), lessonTime,lessonVideoLink);

        // after adding lessons get the chapter duration add ,update it
       String chapterTime = jdbcTemplate.queryForObject(GET_CHAPTER_DURATION, new Object[]{lessonRequest.getChapterId()},String.class);
       String lessonTimeInfo = String.valueOf(lessonRequest.getLessonDuration());
       SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
       timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
       Date date1 = timeFormat.parse(chapterTime);
       Date date2 = timeFormat.parse(lessonTimeInfo);
       long sumOfDurations = date1.getTime() + date2.getTime();
       String courseDuration = timeFormat.format(new Date(sumOfDurations));
       jdbcTemplate.update(UPDATE_CHAPTER_DURATION, new Object[]{courseDuration,lessonRequest.getChapterId()});

       //after adding chapter duration get the course duration, add al;l chapters duration under that course , update course duration
        long durationsSum=0;
        String finalDuration="";
        Date durationDate1 = null;
        Date durationDate2;
        jdbcTemplate.update(SET_COURSE_DURATION_TO_ZERO, new Object[] {"00:00:00", lessonRequest.getCourseId()});
        String courseDurationInfo = jdbcTemplate.queryForObject(GET_COURSE_DURATION, new Object[] {lessonRequest.getCourseId()}, String.class);

        List<Chapterchk> chapterList = jdbcTemplate.query(GET_COURSES_UNDER_A_CHAPTER,(rs, rowNum) -> {
           return new Chapterchk(rs.getString("chapterDuration"));
        }, lessonRequest.getCourseId());
        System.out.println(chapterList);

        for(int i=0;i<chapterList.size();i++)
        {
            String chapterDurationInfo = chapterList.get(i).getChapterDuration();
            SimpleDateFormat timeFormatInfo = new SimpleDateFormat("HH:mm:ss");
            timeFormatInfo.setTimeZone(TimeZone.getTimeZone("UTC"));
            durationDate1 = timeFormatInfo.parse(courseDurationInfo);
            durationDate2 = timeFormatInfo.parse(chapterDurationInfo);
            durationsSum = durationDate1.getTime() + durationDate2.getTime();
            finalDuration = timeFormat.format(new Date(durationsSum));
            courseDurationInfo = finalDuration;
        }
        jdbcTemplate.update(UPDATE_COURSE_DURATION, new Object[] {finalDuration,lessonRequest.getCourseId()});
       return "Lesson Added SuccessFully";
    }
    // adding test
    public String addTest(TestRequestchk test)
    {
        String testDuration = test.getTestDuration().toString();
          jdbcTemplate.update(ADD_TEST, test.getTestId(), test.getTestName(), test.getChapterId(),testDuration, test.getPassingGrade());
          return "Test Added Successfully";
    }
    //adding questions under each test
    public String addQuestion(Questionchk question)
    {
         int questionsCount = jdbcTemplate.queryForObject(GET_EXISTING_QUESTIONS_COUNT, new Object[] {question.getTestId()}, Integer.class);
         jdbcTemplate.update(ADD_QUESTION, question.getQuestionId(), question.getQuestionName(), question.getTestId(), question.getOption_1(), question.getOption_2(), question.getOption_3(),question.getOption_4(),question.getCorrectAnswer());
         jdbcTemplate.update(UPDATE_QUESTIONS_COUNT, new Object[] {questionsCount+1, question.getTestId()});
         return "Question added successfully";
    }
  //adding instructor for each course
    public String addInstructor(InstructorRequestchk instructorRequest) throws IOException
    {
        String profilePhotoLink = getFileUrl(instructorRequest.getProfilePhoto());
        jdbcTemplate.update(ADD_INSTRUCTOR,instructorRequest.getInstructorId(),instructorRequest.getInstructorName(), instructorRequest.getUrl(),instructorRequest.getDescription(),profilePhotoLink);
        return "Instructor added successfully";
    }
}
