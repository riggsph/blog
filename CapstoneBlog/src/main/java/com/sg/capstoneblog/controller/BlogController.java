/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.capstoneblog.controller;

import com.sg.capstoneblog.dao.BlogDao;
import com.sg.capstoneblog.model.Article;
import com.sg.capstoneblog.model.Page;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author peterriggs
 */
@Controller
public class BlogController {
    
    BlogDao dao;
    
    @Inject
    public BlogController(BlogDao dao) {
        this.dao = dao;
    }
    
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String loadHomePage(Model model) {
        List<Article> articleList = dao.getAllArticles();
        articleList.forEach((a) -> {
            a.setAuthor(dao.findAuthorOfArticle(a));
            a.setCategories(dao.getCategoriesForArticle(a));
        });
        model.addAttribute("articleList", articleList);
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        
        return "index";
    }
    
    @RequestMapping(value="/deletePost", method=RequestMethod.GET)
    public String deletePost(HttpServletRequest request, Model model) {
        String articleIdParameter = request.getParameter("id");
        int articleId = Integer.parseInt(articleIdParameter);
        dao.deleteArticle(articleId);
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        return "redirect:/";
    }
    
    @RequestMapping(value="/deletePage", method=RequestMethod.GET)
    public String deletePage(HttpServletRequest request, Model model) {
        String pageIdParameter = request.getParameter("id");
        int pageId = Integer.parseInt(pageIdParameter);
        dao.deletePage(pageId);
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        return "redirect:/displayAdminPage";
    }
    
    @RequestMapping(value="/displayMatchingArticles", method=RequestMethod.GET)
    public String displayMatchingArticles(HttpServletRequest request, Model model) {
        String categoryIdParameter = request.getParameter("categoryId");
        int categoryId = Integer.parseInt(categoryIdParameter);
        List<Article> matchingList = dao.getMatchingArticles(categoryId);
        model.addAttribute("matchingList", matchingList);
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        return "categorySearch";
    }
    
    @RequestMapping(value="/displayArticleDetails", method=RequestMethod.GET)
    public String displayArticleDetails(HttpServletRequest request, Model model) {
        String articleIdParameter = request.getParameter("articleId");
        int articleId = Integer.parseInt(articleIdParameter);
        Article article = dao.getArticleById(articleId);
        article.setAuthor(dao.findAuthorOfArticle(article));
        article.setCategories(dao.getCategoriesForArticle(article));
        model.addAttribute("article", article);
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        return "articleDetails";
    }
    
}
