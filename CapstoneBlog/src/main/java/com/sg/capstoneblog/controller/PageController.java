/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.capstoneblog.controller;

import com.sg.capstoneblog.dao.BlogDao;
import com.sg.capstoneblog.model.Page;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class PageController {
    
    BlogDao dao;
    
    @Inject
    public PageController(BlogDao dao){
        this.dao = dao;
    }
    
    @RequestMapping(value="/displayAddPage", method = RequestMethod.GET)
    public String loadAddPage(Model model) {
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        return "addPage";
    }
    
    @RequestMapping(value="/displayPage", method=RequestMethod.GET)
    public String displayPage(HttpServletRequest request, Model model) {
        String pageIdParameter = request.getParameter("pageId");
        int pageId = Integer.parseInt(pageIdParameter);
        Page page = dao.getPageById(pageId);
        page.setAuthor(dao.findAuthorOfPage(page));
        model.addAttribute("page", page);
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        return "pageDetails";
    }
    
    @RequestMapping(value="/createPage", method = RequestMethod.POST)
    public String addPage(HttpServletRequest request, Model model, Principal principal
    ) {
        
        Page newPage = new Page();
        newPage.setTitle(request.getParameter("title"));
        newPage.setContent(request.getParameter("content"));
        newPage.setDate(LocalDate.parse(request.getParameter("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        newPage.setExpirationDate(LocalDate.parse(request.getParameter("expirationDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        newPage.setAuthor(dao.getUserByName(principal.getName()));
        newPage.setAuthor(dao.getUserByName("admin"));
        
        dao.addPage(newPage);
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        
        return "redirect:/";
    }
}
