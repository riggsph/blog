/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.capstoneblog;

import com.sg.capstoneblog.dao.BlogDao;
import com.sg.capstoneblog.model.Article;
import com.sg.capstoneblog.model.Category;
import com.sg.capstoneblog.model.Page;
import com.sg.capstoneblog.model.Role;
import com.sg.capstoneblog.model.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author peterriggs
 */
public class BlogDaoTest {
    
    private BlogDao dao;

    public BlogDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        // ask Spring for our DAO
        ApplicationContext ctx
            = new ClassPathXmlApplicationContext(
                        "test-applicationContext.xml");
        dao = ctx.getBean("blogDao", BlogDao.class);
        
        // remove all of the Articles
        List<Article> articles = dao.getAllArticles();
        for (Article currentArticle : articles) {
            dao.deleteArticle(currentArticle.getId());
        }
        
        // remove all of the Pages
        List<Page> pages = dao.getAllPages();
        for (Page currentPage : pages) {
            dao.deletePage(currentPage.getId());
        }
        
        // remove all of the Users
        List<User> users = dao.getAllUsers();
        for (User currentUser : users) {
            dao.deleteUser(currentUser.getId());
        }
        
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void addGetDeleteArticle() {
        
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(dao.getRoleByRole("ROLE_ADMIN"));
        
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        user.setPassword("password");
        user.setRoles(userRoles);
        user.setEnabled(true);
        dao.addUser(user);
        
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("#test");
       
        Article article = new Article();
        article.setTitle("Test Article #1");
        article.setContent("Lorem Ipsum");
        article.setDate(LocalDate.parse("2021-10-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        article.setExpirationDate(LocalDate.parse("2022-11-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        article.setApprovalId(2);
        article.setImageLink("https://cdn.pixabay.com/photo/2018/03/08/01/04/cuba-3207576_1280.jpg");
        article.setAuthor(user);
       
        dao.addArticle(article);
        Article fromDb = dao.getArticleById(article.getId());
        assertEquals(fromDb.getId(),article.getId());
        dao.deleteArticle(article.getId());
        assertNull(dao.getArticleById(article.getId()));
    }

    @Test
    public void addEditArticle() {
    
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(dao.getRoleByRole("ROLE_ADMIN"));
        
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        user.setPassword("password");
        user.setRoles(userRoles);
        user.setEnabled(true);
        dao.addUser(user);
        
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("#test");
       
        Article article = new Article();
        article.setTitle("Test Article #1");
        article.setContent("Lorem Ipsum");
        article.setDate(LocalDate.parse("2021-10-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        article.setExpirationDate(LocalDate.parse("2022-11-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        article.setApprovalId(2);
        article.setImageLink("https://cdn.pixabay.com/photo/2018/03/08/01/04/cuba-3207576_1280.jpg");
        article.setAuthor(user);
       
        dao.addArticle(article);
        
        article.setTitle("Test Title #2");
        dao.editArticle(article);
        Article fromDb = dao.getArticleById(article.getId());
        assertEquals(fromDb.getTitle(),article.getTitle());
    }

    
    @Test
    public void getAllArticle() {
        
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(dao.getRoleByRole("ROLE_ADMIN"));
        
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        user.setPassword("password");
        user.setRoles(userRoles);
        user.setEnabled(true);
        dao.addUser(user);
        
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("#test");
       
        Article article = new Article();
        article.setTitle("Test Article #1");
        article.setContent("Lorem Ipsum");
        article.setDate(LocalDate.parse("2021-10-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        article.setExpirationDate(LocalDate.parse("2022-11-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        article.setApprovalId(2);
        article.setImageLink("https://cdn.pixabay.com/photo/2018/03/08/01/04/cuba-3207576_1280.jpg");
        article.setAuthor(user);
        dao.addArticle(article);
        
        Article article2 = new Article();
        article2.setTitle("Test Article #1");
        article2.setContent("Lorem Ipsum");
        article2.setDate(LocalDate.parse("2021-10-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        article2.setExpirationDate(LocalDate.parse("2022-11-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        article2.setApprovalId(2);
        article2.setImageLink("https://cdn.pixabay.com/photo/2018/03/08/01/04/cuba-3207576_1280.jpg");
        article2.setAuthor(user);
        dao.addArticle(article2);
        
        List<Article> articleList = dao.getAllArticles();
        assertEquals(articleList.size(), 2);
    }

    @Test
    public void getMatchingArticles() {
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(dao.getRoleByRole("ROLE_ADMIN"));
        
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        user.setPassword("password");
        user.setRoles(userRoles);
        user.setEnabled(true);
        dao.addUser(user);
        
        Category category = new Category();
        category.setName("#test");
        List<Category> cList = new ArrayList<>();
        cList.add(category);
       
        Article article = new Article();
        article.setTitle("Test Article #1");
        article.setContent("Lorem Ipsum");
        article.setDate(LocalDate.parse("2021-10-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        article.setExpirationDate(LocalDate.parse("2022-11-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        article.setApprovalId(2);
        article.setImageLink("https://cdn.pixabay.com/photo/2018/03/08/01/04/cuba-3207576_1280.jpg");
        article.setAuthor(user);
        article.setCategories(cList);
        dao.addArticle(article);
        
        Article article2 = new Article();
        article2.setTitle("Test Article #1");
        article2.setContent("Lorem Ipsum");
        article2.setDate(LocalDate.parse("2021-10-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        article2.setExpirationDate(LocalDate.parse("2022-11-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        article2.setApprovalId(2);
        article2.setImageLink("https://cdn.pixabay.com/photo/2018/03/08/01/04/cuba-3207576_1280.jpg");
        article2.setAuthor(user);
        article2.setCategories(cList);
        dao.addArticle(article2);
        
        List<Article> articleList = dao.getMatchingArticles(1);
        assertEquals(articleList.size(), 2);
    }
    
}
