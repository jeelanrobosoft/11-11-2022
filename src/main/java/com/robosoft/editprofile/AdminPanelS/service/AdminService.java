package com.robosoft.editprofile.AdminPanelS.service;

import com.robosoft.editprofile.AdminPanel.servicechk.AdminServicechk;
import com.robosoft.editprofile.AdminPanelS.entity.Overview;
import com.robosoft.editprofile.AdminPanelS.model.Category;
import com.robosoft.editprofile.AdminPanelS.model.Chapter;
import com.robosoft.editprofile.AdminPanelS.model.Policy;
import com.robosoft.editprofile.AdminPanelS.model.SubCategory;
import com.robosoft.editprofile.AdminPanelS.request.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    AdminServicechk adminServicechk;


    public int addCategory(CategoryRequest category) throws IOException {
        try {
            jdbcTemplate.queryForObject("SELECT * FROM category WHERE categoryName = ?", new BeanPropertyRowMapper<>(Category.class),category.getCategoryName());
            return 0;
        }
        catch (Exception e)
        {
//            if(category.getCategoryName().isEmpty())
//                throw new EmptyStackException();
//            else
                String categoryPhoto = adminServicechk.getFileUrl(category.getCategoryPhoto());
                return jdbcTemplate.update("INSERT INTO category(categoryName,categoryPhoto) VALUES(?,?)",category.getCategoryName(),categoryPhoto);
        }
    }

    int pages = 2;
    int lowerLimit = 0;
    int upperLimit = pages;

    public List<Category> getCategories()
    {
        List<Category> categories=  jdbcTemplate.query("SELECT * FROM category limit ?,?",(rs, rowNum) ->
        {
            return new Category(rs.getInt("categoryId"),rs.getString("categoryName"),rs.getString("categoryPhoto"));
        },lowerLimit,upperLimit);
        lowerLimit = lowerLimit+pages;

        if(categories.size()==0)
        {
            lowerLimit=0;
            List <Category> categories1= jdbcTemplate.query("SELECT * FROM category limit ?,?", (rs, rowNum) ->
                    new Category(rs.getInt("categoryId"),rs.getString("categoryName"),rs.getString("categoryPhoto")),lowerLimit,upperLimit);
            return categories1;
        }
        return categories;
    }

    public int addSubCategory(SubCategory subCategory)
    {
        try {
            jdbcTemplate.queryForObject("SELECT * FROM subCategory WHERE subCategoryName = ?", new BeanPropertyRowMapper<>(Category.class),subCategory.getSubCategoryName());
            return 0;
        }
        catch (Exception e)
        {
            return jdbcTemplate.update("INSERT INTO subCategory(categoryId,subCategoryName) VALUES(?,?)",subCategory.getCategoryId(),subCategory.getSubCategoryName());
        }
    }

    public List<SubCategory> getSubCategories(Category category)
    {
        List<SubCategory> subCategories=  jdbcTemplate.query("SELECT * FROM subCategory WHERE categoryId = ? limit ?,?",(rs, rowNum) ->
                new SubCategory(rs.getInt("categoryId"),rs.getInt("subCategoryId"),rs.getString("subCategoryName")),category.getCategoryId(),lowerLimit,upperLimit);
        lowerLimit = lowerLimit+pages;
        if(subCategories.size()==0)
        {
            lowerLimit=0;
            List<SubCategory> subCategories1=  jdbcTemplate.query("SELECT * FROM subCategory WHERE categoryId = ? limit ?,?",(rs, rowNum) ->
            {
                return new SubCategory(rs.getInt("categoryId"),rs.getInt("subCategoryId"),rs.getString("subCategoryName"));
            },category.getCategoryId(),lowerLimit,upperLimit);
            return subCategories1;
        }
        return subCategories;
    }

    public int addChapter(Chapter chapter){
        return jdbcTemplate.update("INSERT INTO chapter(courseId,chapterNumber,chapterName) VALUES(?,?,?)",chapter.getCourseId(),chapter.getChapterNumber(),chapter.getChapterName());
    }

    public int addOverView(Overview overview){

        try{
            jdbcTemplate.queryForObject("SELECT courseId FROM overview WHERE courseId = ?",new BeanPropertyRowMapper<>(Overview.class),overview.getCourseId());
            return 0;
        }
        catch (Exception e) {
            int chapterCount = jdbcTemplate.queryForObject("SELECT COUNT(courseId) FROM chapter WHERE courseId = ?", Integer.class, overview.getCourseId());
            int lessonCount = 0;
            List<Integer> chapterIds = jdbcTemplate.queryForList("SELECT chapterId FROM chapter WHERE courseId = ?", Integer.class,overview.getCourseId());

            for(int i : chapterIds)
            {
                lessonCount += jdbcTemplate.queryForObject("SELECT COUNT(chapterId) FROM lesson WHERE chapterId = ?", Integer.class, i);
            }


            List<Integer> chapterIDs = jdbcTemplate.queryForList("SELECT chapterId FROM chapter WHERE courseId = ?", Integer.class, overview.getCourseId());

            int testCount = 0;
            for (int i : chapterIDs) {
                testCount += jdbcTemplate.queryForObject("SELECT COUNT(testId) FROM test WHERE chapterId = ?", Integer.class, i);
            }

            return jdbcTemplate.update("INSERT INTO overView(courseId,courseTagLine,description,chapterCount,lessonCount,testCount,learningOutCome,requirements,instructorId,difficultyLevel) VALUES(?,?,?,?,?,?,?,?,?,?)",
                    overview.getCourseId(), overview.getCourseTagLine(), overview.getDescription(), chapterCount, lessonCount, testCount, overview.getLearningOutCome(),overview.getRequirements(), overview.getInstructorId(),overview.getDifficultyLevel());
        }
    }

    public int addPolicy(Policy policy)
    {
        try
        {
            jdbcTemplate.queryForObject("SELECT termsAndConditions FROM policy WHERE termsAndConditions = ? AND privacyPolicy = ?",String.class,policy.getTermsAndConditions(),policy.getPrivacyPolicy());
            return 0;
        }
        catch (Exception e)
        {
            return jdbcTemplate.update("INSERT INTO policy(termsAndConditions,privacyPolicy) VALUES(?,?)",policy.getTermsAndConditions(),policy.getPrivacyPolicy());
        }
    }

}
