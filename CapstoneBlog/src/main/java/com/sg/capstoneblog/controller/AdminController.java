/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.capstoneblog.controller;

import com.sg.capstoneblog.dao.BlogDao;
import com.sg.capstoneblog.model.Article;
import com.sg.capstoneblog.model.Page;
import com.sg.capstoneblog.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author peterriggs
 */
@Controller
public class AdminController {
    
    BlogDao dao;
    
    @Inject
    public AdminController(BlogDao dao){
        this.dao = dao;
    }
    
    @RequestMapping(value="/displayAdminPage", method = RequestMethod.GET)
    public String loadAdminPage(Model model) {
        List<User> userList = dao.getAllUsers();
        List<Article> pendingList = dao.getUnapprovedArticles();
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("userList", userList);
        model.addAttribute("pendingList", pendingList);
        model.addAttribute("pageList", pageList);
        return "admin";
    }
    
    @RequestMapping(value="displayApprovalForm", method = RequestMethod.GET)
    public String displayApprovalForm(HttpServletRequest request, Model model) {
        String articleIdParameter = request.getParameter("articleId");
        int articleId = Integer.parseInt(articleIdParameter);
        Article article = dao.getArticleById(articleId);
        article.setAuthor(dao.findAuthorOfArticle(article));
        
        Map<Integer, String> approvalMap = new HashMap<>();
        approvalMap.put(1, "Pending Approval");
        approvalMap.put(2, "Approved");
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        
        model.addAttribute("article", article);
        model.addAttribute("approvalMap", approvalMap);
        return "approvalForm";
    }
    
    @RequestMapping(value="/approveArticle", method = RequestMethod.POST)
    public String approveArticle(@Valid @ModelAttribute("article") Article article, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "articleDetails";
        }
        article.setAuthor(dao.findAuthorOfArticle(article));
        
        dao.editArticle(article);
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        
        return "redirect:displayAdminPage";
    }
    
}
