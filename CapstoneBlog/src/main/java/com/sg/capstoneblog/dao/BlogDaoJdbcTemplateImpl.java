/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.capstoneblog.dao;

import com.sg.capstoneblog.model.Article;
import com.sg.capstoneblog.model.Category;
import com.sg.capstoneblog.model.Page;
import com.sg.capstoneblog.model.Role;
import com.sg.capstoneblog.model.User;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author peterriggs
 */
public class BlogDaoJdbcTemplateImpl implements BlogDao {
    
    private JdbcTemplate jdbcTemplate;
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    // ================================
    // User Related Prepared Statements
    // ================================
    
    private static final String SQL_INSERT_USER
            = "insert into user (`username`, `password`, `enabled`) values (?, ?, ?)";
    
    private static final String SQL_UPDATE_USER
            = "update user set `username` = ?, `password` = ?, `enabled` = ? where `id` = ?";

    private static final String DELETE_USER_ROLE
            = "DELETE FROM user_role WHERE user_id = ?";
    
    private static final String DELETE_USER
            = "DELETE FROM user WHERE id = ?";
    
    private static final String SQL_GET_USER
            = "select * from user where `id` = ?";
    
    private static final String SQL_GET_USER_BY_NAME
            = "select * from user where `username` = ?";
    
    private static final String SQL_GET_ALL_USERS
            = "select * from user";
    
    private static final String SELECT_USER_ID_BY_ARTICLE_ID
            = "select userId from article where `articleId` = ?";
    
    private static final String SELECT_USER_ID_BY_PAGE_ID
            = "select userId from page where `pageId` = ?";
    
    // ================================
    // Role Related Prepared Statements
    // ================================
    
    private static final String SELECT_ROLES_FOR_USER 
            = "SELECT r.* FROM user_role ur "
                + "JOIN role r ON ur.role_id = r.id "
                + "WHERE ur.user_id = ?";
    
    private static final String SELECT_ROLE_BY_ID 
            = "SELECT * FROM role WHERE id = ?";
    
    private static final String SELECT_ROLE_BY_ROLE 
            = "SELECT * FROM role WHERE role = ?";
    
    private static final String SELECT_ALL_ROLES 
            = "SELECT * FROM role";   
    
    private static final String DELETE_ROLE 
            = "DELETE FROM role WHERE id = ?";
    
    private static final String UPDATE_ROLE
            = "UPDATE role SET role = ? WHERE id = ?";
    
    private static final String INSERT_ROLE
            = "INSERT INTO role(role) VALUES(?)";
    
    private static final String INSERT_USER_ROLE
            = "INSERT INTO user_role(user_id, role_id) VALUES(?,?)";
    
    // ===================================
    // Article Related Prepared Statements
    // ===================================
    
    private static final String SQL_INSERT_ARTICLE
            = "insert into article (`title`, `content`, postDate, expirationDate, approvalId, imageLink, userId) "
            + "values (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_UPDATE_ARTICLE
            = "update article set `title` = ?, `content` = ?, postDate = ?, expirationDate = ?,"
            + "approvalId = ?, imageLink = ?, userId = ? where `articleId` = ?";
    
    private static final String SQL_DELETE_ARTICLE
            = "delete from article where `articleId` = ?";
    
    private static final String SQL_GET_ARTICLE
            = "select * from article where `articleId` = ?";
    
    private static final String SQL_GET_ALL_ARTICLES
            = "select * from article order by `articleId` desc";
    
    private static final String SQL_GET_UNAPPROVED_ARTICLES
            = "select * from article where `approvalId` = 1";

    // ====================================
    // Cateogry Related Prepared Statements
    // ====================================
    
    private static final String SQL_INSERT_CATEGORY
            = "insert into category (`description`) values (?)";
    
    private static final String SQL_GET_CATS_FOR_ARTICLE
            = "select c.categoryId, c.description from category c "
            + "join article_category ac on c.categoryId = ac.categoryId "
            + "where ac.articleId = ?";
    
    private static final String SQL_GET_ALL_CATEGORIES
            = "select * from category";
    
    private static final String SQL_GET_MATCHING_ARTICLES
            = "select a.articleId, a.title, a.content, a.postDate, a.expirationDate, a.approvalId, a.imageLink, a.userId from article a " +
              "join article_category ac on a.articleId = ac.articleId " +
              "where ac.categoryId = ? AND a.approvalId = 2";
    
    // ===================================
    // Page Related Prepared Statements
    // ===================================
    
    private static final String SQL_INSERT_PAGE
            = "insert into page (`title`, `content`, postDate, expirationDate, userId) "
            + "values (?, ?, ?, ?, ?)";
    
    private static final String SQL_UPDATE_PAGE
            = "update page set `title` = ?, `content` = ?, postDate = ?, "
            + "userId = ? where `pageId` = ?";
    
    private static final String SQL_DELETE_PAGE
            = "delete from page where `pageId` = ?";
    
    private static final String SQL_GET_PAGE
            = "select * from page where `pageId` = ?";
    
    private static final String SQL_GET_ALL_PAGES
            = "select * from page order by `pageId` desc";

    // ===============================
    // Bridge Table Related Statements
    // ===============================
    
    private static final String SQL_INSERT_ARTICLE_CATEGORY
            = "insert into article_category (articleId, categoryId) values (?, ?)";
    
    private static final String SQL_DELETE_ARTICLE_CATEGORY
            = "delete from article_category where articleId = ?";
    
    // ============
    // User Methods
    // ============
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public User addUser(User newUser) {
        
        jdbcTemplate.update(SQL_INSERT_USER,
                newUser.getUsername(),
                newUser.getPassword(),
                newUser.isEnabled());
        
        newUser.setId(jdbcTemplate.queryForObject("select LAST_INSERT_ID()", Integer.class));
        
        for (Role role : newUser.getRoles()) {
            jdbcTemplate.update(INSERT_USER_ROLE, newUser.getId(), role.getId());
        }
        
        return newUser;
    }
        
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteUser(int id) {
        jdbcTemplate.update(DELETE_USER_ROLE, id);
        jdbcTemplate.update(DELETE_USER, id);
    }

    @Override
    public User getUserById(int id) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_GET_USER, new UserMapper(), id);
            user.setRoles(getRolesForUser(user.getId()));
            return user;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
        
    }
    
    private Set getRolesForUser(int id) throws DataAccessException {
        Set roles = new HashSet(jdbcTemplate.query(SELECT_ROLES_FOR_USER, new RoleMapper(), id));
        return roles;
    }
    
    @Override
    public User getUserByName(String username) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_GET_USER_BY_NAME, new UserMapper(), username);
            user.setRoles(getRolesForUser(user.getId()));
            return user;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
        
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = jdbcTemplate.query(SQL_GET_ALL_USERS, new UserMapper());
        for (User user : users) {
            user.setRoles(getRolesForUser(user.getId()));
        }
        return users;
    }
    
    @Override
    public void updateUser(User user) {
        jdbcTemplate.update(SQL_UPDATE_USER, user.getUsername(), user.getPassword(), user.isEnabled(), user.getId());

        jdbcTemplate.update(DELETE_USER_ROLE, user.getId());
        for (Role role : user.getRoles()) {
            jdbcTemplate.update(INSERT_USER_ROLE, user.getId(), role.getId());
        }
    }
    
    // ================
    // Role Methods
    // ================
    
    @Override
    public Role getRoleById(int id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ROLE_BY_ID, new RoleMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public Role getRoleByRole(String role) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ROLE_BY_ROLE, new RoleMapper(), role);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List getAllRoles() {
        return jdbcTemplate.query(SELECT_ALL_ROLES, new RoleMapper());
    }

    @Override
    public void deleteRole(int id) {
        jdbcTemplate.update(DELETE_USER_ROLE, id);
        jdbcTemplate.update(DELETE_ROLE, id);
    }

    @Override
    public void updateRole(Role role) {
        jdbcTemplate.update(UPDATE_ROLE, role.getRole(), role.getId());
    }

    @Override
    @Transactional
    public Role createRole(Role role) {
        jdbcTemplate.update(INSERT_ROLE, role.getRole());
        int newId = jdbcTemplate.queryForObject("select LAST_INSERT_ID()", Integer.class);
        role.setId(newId);
        return role;
    }

    // ===============
    // Article Methods
    // ===============
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Article addArticle(Article newArticle) {
        jdbcTemplate.update(SQL_INSERT_ARTICLE,
                newArticle.getTitle(),
                newArticle.getContent(),
                java.sql.Date.valueOf(newArticle.getDate()),
                java.sql.Date.valueOf(newArticle.getExpirationDate()),
                newArticle.getApprovalId(),
                newArticle.getImageLink(),
                newArticle.getAuthor().getId());
        
        newArticle.setId(jdbcTemplate.queryForObject("select LAST_INSERT_ID()",
                Integer.class));
        
        addCategories(newArticle.getCategories());
        
        addArticleCategoriesEntries(newArticle);
        
        return newArticle;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Article editArticle(Article article) {
        jdbcTemplate.update(SQL_UPDATE_ARTICLE,
                article.getTitle(),
                article.getContent(),
                java.sql.Date.valueOf(article.getDate()),
                java.sql.Date.valueOf(article.getExpirationDate()),
                article.getApprovalId(),
                article.getImageLink(),
                article.getAuthor().getId(),
                article.getId());
        
        return article;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteArticle(int id) {
        jdbcTemplate.update(SQL_DELETE_ARTICLE_CATEGORY, id);
        jdbcTemplate.update(SQL_DELETE_ARTICLE, id);
    }

    @Override
    public Article getArticleById(int id) {
        try {
            Article article = jdbcTemplate.queryForObject(SQL_GET_ARTICLE, new ArticleMapper(), id);
            if (article.getExpirationDate() != null) {
                LocalDate currentDate = getCurrentDate();
                if (currentDate.isBefore(article.getExpirationDate())) {
                    return article;
                } else {
                    return null;
                }
            } else {
                return article;
            }
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Article> getAllArticles() {
        List<Article> articleList = jdbcTemplate.query(SQL_GET_ALL_ARTICLES, new ArticleMapper());
        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            if (article.getExpirationDate() != null) {
                LocalDate currentDate = getCurrentDate();
                if (currentDate.isEqual(article.getExpirationDate()) || 
                    currentDate.isAfter(article.getExpirationDate())) {
                articleList.remove(article);
                }
            }
        }
        return articleList;
    }
    
    @Override
    public List<Article> getUnapprovedArticles() {
        return jdbcTemplate.query(SQL_GET_UNAPPROVED_ARTICLES, new ArticleMapper());
    }
    
    // ================
    // Category Methods
    // ================
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Category addCategory(Category category) {
        if (category.getCategoryId() > 0) {
            return category;
        } else {
            jdbcTemplate.update(SQL_INSERT_CATEGORY, category.getName());
            category.setCategoryId(jdbcTemplate.queryForObject("select LAST_INSERT_ID()",
                Integer.class));
            return category;
        }
    }
    
    @Override
    public List<Category> getAllCategories() {
        return jdbcTemplate.query(SQL_GET_ALL_CATEGORIES, new CategoryMapper());
    }
    
    @Override
    public List<Article> getMatchingArticles(int id) {
        List<Article> articleList = jdbcTemplate.query(SQL_GET_MATCHING_ARTICLES, new ArticleMapper(), id);
        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            if (article.getExpirationDate() != null) {
                LocalDate currentDate = getCurrentDate();
                if (currentDate.isEqual(article.getExpirationDate()) || 
                    currentDate.isAfter(article.getExpirationDate())) {
                articleList.remove(article);
                }
            }
        }
        return articleList;
    }
    
    // ==============
    // Helper Methods
    // ==============
    
    private void addCategories(List<Category> categories) {
        categories.forEach((currentCategory) -> {
            
            List<Category> categoryList = getAllCategories();
            for (int i = 0; i < categories.size(); i++) {
                Category currentLoopCategory = categories.get(i);
                for (int j = 0; j < categoryList.size(); j++) {
                    if (currentLoopCategory.getName().equalsIgnoreCase(categoryList.get(j).getName())) {
                        currentLoopCategory.setCategoryId(categoryList.get(j).getCategoryId());
                    }
                }
            }
            this.addCategory(currentCategory);
        });
    }
    
    private void addArticleCategoriesEntries(Article article) {
        List<Category> categories = article.getCategories();
        categories.forEach((currentCategory) -> {
            jdbcTemplate.update(SQL_INSERT_ARTICLE_CATEGORY,
                    article.getId(),
                    currentCategory.getCategoryId());
        });
    }
    
    @Override
    public User findAuthorOfArticle(Article article) {
        int userId = jdbcTemplate.queryForObject(SELECT_USER_ID_BY_ARTICLE_ID,
                new UserIdMapper(), article.getId());
        
        User u = this.getUserById(userId);
        
        return u;
    }
    
    @Override
    public User findAuthorOfPage(Page page) {
        int userId = jdbcTemplate.queryForObject(SELECT_USER_ID_BY_PAGE_ID,
                new UserIdMapper(), page.getId());
        
        User u = this.getUserById(userId);
        
        return u;
    }
    
    @Override
    public List<Category> getCategoriesForArticle(Article article) {
        return jdbcTemplate.query(SQL_GET_CATS_FOR_ARTICLE, new CategoryMapper(), article.getId());
    }
    
    // ==============
    // Page Methods
    // ==============
    
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Page addPage(Page newPage) {
        jdbcTemplate.update(SQL_INSERT_PAGE,
                newPage.getTitle(),
                newPage.getContent(),
                java.sql.Date.valueOf(newPage.getDate()),
                java.sql.Date.valueOf(newPage.getExpirationDate()),
                newPage.getAuthor().getId());
        
        newPage.setId(jdbcTemplate.queryForObject("select LAST_INSERT_ID()",
                Integer.class));
        
        return newPage;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Page editPage(Page page) {
        jdbcTemplate.update(SQL_UPDATE_PAGE,
                page.getTitle(),
                page.getContent(),
                java.sql.Date.valueOf(page.getDate()),
                java.sql.Date.valueOf(page.getExpirationDate()),
                page.getAuthor().getId(),
                page.getId());
        
        return page;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deletePage(int id) {
        jdbcTemplate.update(SQL_DELETE_PAGE, id);
    }

    @Override
    public Page getPageById(int id) {
        try {
            Page page = jdbcTemplate.queryForObject(SQL_GET_PAGE, new PageMapper(), id);
            if (page.getExpirationDate() != null) {
                LocalDate currentDate = getCurrentDate();
                if (currentDate.isBefore(page.getExpirationDate())) {
                    return page;
                } else {
                    return null;
                }
            } else {
                return page;
            }
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Page> getAllPages() {
        List<Page> pageList = jdbcTemplate.query(SQL_GET_ALL_PAGES, new PageMapper());
        for (int i = 0; i < pageList.size(); i++) {
            Page page = pageList.get(i);
            if (page.getExpirationDate() != null) {
                LocalDate currentDate = getCurrentDate();
                if (currentDate.isEqual(page.getExpirationDate()) || 
                    currentDate.isAfter(page.getExpirationDate())) {
                pageList.remove(page);
                }
            }
        }
        return pageList;
    }
    
    // ==============
    // Mappers
    // ==============
    
    private static final class UserMapper implements RowMapper<User> {
        
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User u = new User();
            u.setId(rs.getInt("id"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setEnabled(rs.getBoolean("enabled"));
            
            return u;
        }
    }
    private static final class UserIdMapper implements RowMapper<Integer> {
        
        @Override
        public Integer mapRow(ResultSet rs, int i) throws SQLException {

            return rs.getInt("userId");
        }
    }
    
    public static final class RoleMapper implements RowMapper<Role> {

        @Override
        public Role mapRow(ResultSet rs, int i) throws SQLException {
            Role role = new Role();
            role.setId(rs.getInt("id"));
            role.setRole(rs.getString("role"));
            return role;
        }
    }
    
    private static final class ArticleMapper implements RowMapper<Article> {
        
        @Override
        public Article mapRow(ResultSet rs, int i) throws SQLException {
            Article a = new Article();
            a.setTitle(rs.getString("title"));
            a.setContent(rs.getString("content"));
            a.setDate(rs.getTimestamp("postDate").toLocalDateTime().toLocalDate());
            a.setApprovalId(rs.getInt("approvalId"));
            a.setImageLink(rs.getString("imageLink"));
            a.setId(rs.getInt("articleId"));
            
            if (rs.getTimestamp("expirationDate") != null) {
                a.setExpirationDate(rs.getTimestamp("expirationDate").toLocalDateTime().toLocalDate());
            }
            
            return a;
        }
    }
    
    private static final class CategoryMapper implements RowMapper<Category> {
        
        @Override
        public Category mapRow(ResultSet rs, int i) throws SQLException {
            Category c = new Category();
            c.setName(rs.getString("description"));
            c.setCategoryId(rs.getInt("categoryId"));
            
            return c;
        }
    }
    
    private static final class PageMapper implements RowMapper<Page> {
        
        @Override
        public Page mapRow(ResultSet rs, int i) throws SQLException {
            Page p = new Page();
            p.setTitle(rs.getString("title"));
            p.setContent(rs.getString("content"));
            p.setDate(rs.getTimestamp("postDate").toLocalDateTime().toLocalDate());
            p.setId(rs.getInt("pageId"));
            
            if (rs.getTimestamp("expirationDate") != null) {
                p.setExpirationDate(rs.getTimestamp("expirationDate").toLocalDateTime().toLocalDate());
            }
            
            return p;
        }
    }
    
    public LocalDate getCurrentDate() {
        Date date = new Date(System.currentTimeMillis());
        LocalDate localDate = LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return localDate;
    }
    
}
