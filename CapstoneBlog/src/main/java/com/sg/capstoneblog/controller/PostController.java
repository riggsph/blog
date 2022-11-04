package com.sg.capstoneblog.controller;

import com.sg.capstoneblog.dao.BlogDao;
import com.sg.capstoneblog.model.Article;
import com.sg.capstoneblog.model.Category;
import com.sg.capstoneblog.model.Page;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
public class PostController {
    
    BlogDao dao;
    
    @Inject
    public PostController(BlogDao dao){
        this.dao = dao;
    }
    
    @RequestMapping(value="/displayPostingPage", method = RequestMethod.GET)
    public String loadPostingPage(Model model) {
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        return "compose";
    }
    
    @RequestMapping(value="/createPost", method = RequestMethod.POST)
    public String addPost(HttpServletRequest request, Model model, Principal principal
    ) {
        
        Article newArticle = new Article();
        newArticle.setTitle(request.getParameter("title"));
        newArticle.setContent(request.getParameter("content"));
        newArticle.setDate(LocalDate.parse(request.getParameter("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        newArticle.setExpirationDate(LocalDate.parse(request.getParameter("expirationDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if("admin".equals(principal.getName())) {
           newArticle.setApprovalId(2); 
        } else {
           newArticle.setApprovalId(1);
        }
        newArticle.setImageLink(request.getParameter("imageLink"));
        newArticle.setAuthor(dao.getUserByName(principal.getName()));
        
        String categoryString = request.getParameter("tags");
        String[] categoryArr = categoryString.split(", ");
        List<Category> categories = new ArrayList<>();
        for (String s : categoryArr) {
            Category cat = new Category();
            cat.setName(s);
            categories.add(cat);
        }
        
        newArticle.setCategories(categories);
        
        dao.addArticle(newArticle);
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        
        return "redirect:/";
    }
    
}
