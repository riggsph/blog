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
import java.util.List;

/**
 *
 * @author peterriggs
 */
public interface BlogDao {
    
    public User addUser(User newUser);
    
    public void deleteUser(int id);
    
    public User getUserById(int id);
    
    public User getUserByName(String username);
    
    public List<User> getAllUsers();
    
    public void updateUser(User user);
    
    public Role getRoleById(int id);
    
    public Role getRoleByRole(String role);
    
    public List getAllRoles();
    
    public void deleteRole(int id);
    
    public void updateRole(Role role);
    
    public Role createRole(Role role);
    
    public Article addArticle(Article newArticle);
    
    public Article editArticle(Article article);
    
    public void deleteArticle(int id);
    
    public Article getArticleById(int id);
    
    public List<Article> getAllArticles();
    
    public List<Article> getUnapprovedArticles();
    
    public Category addCategory(Category category);
    
    public List<Category> getAllCategories();
    
    public List<Article> getMatchingArticles(int id);
    
    public User findAuthorOfArticle(Article article);
    
    public List<Category> getCategoriesForArticle(Article art);
    
    public Page addPage(Page newPage);
    
    public Page editPage(Page page);
    
    public void deletePage(int id);
    
    public Page getPageById(int id);
    
    public List<Page> getAllPages();
    
    public User findAuthorOfPage(Page page);
    
    
}
