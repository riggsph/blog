/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.capstoneblog.controller;

import com.sg.capstoneblog.dao.BlogDao;
import com.sg.capstoneblog.model.Page;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author peterriggs
 */
@Controller
public class LoginController {
    
    BlogDao dao;
    
    @Inject
    public LoginController(BlogDao dao){
        this.dao = dao;
    }
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String showLoginForm(Model model) {
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        
        return "login";
    }
    
}
