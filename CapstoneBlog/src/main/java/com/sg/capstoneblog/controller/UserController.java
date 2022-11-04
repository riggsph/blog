/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.capstoneblog.controller;

import com.sg.capstoneblog.dao.BlogDao;
import com.sg.capstoneblog.model.Page;
import com.sg.capstoneblog.model.Role;
import com.sg.capstoneblog.model.User;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author peterriggs
 */
@Controller
public class UserController {

    private BlogDao dao;
    private PasswordEncoder encoder;

    @Inject
    public UserController(BlogDao dao, PasswordEncoder encoder) {
        this.dao = dao;
        this.encoder = encoder;
    }
    
    @RequestMapping(value = "/displayUserList", method = RequestMethod.GET)
    public String displayUserList(Map<String, Object> model, Model model2) {
        List users = dao.getAllUsers();
        model.put("users", users);
        
        List<Page> pageList = dao.getAllPages();
        model2.addAttribute("pageList", pageList);
        return "displayUserList";
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUser(HttpServletRequest req, Model model) {
        User newUser = new User();
        newUser.setUsername(req.getParameter("username"));
        String clearPw = req.getParameter("password");
        String hashPw = encoder.encode(clearPw);
        newUser.setPassword(hashPw);
        newUser.setEnabled(true);
        
        Set<Role> userRoles = new HashSet<>();
        if (req.getParameter("role").equalsIgnoreCase("admin")) {
            userRoles.add(dao.getRoleByRole("ROLE_USER"));
            userRoles.add(dao.getRoleByRole("ROLE_EDITOR"));
            userRoles.add(dao.getRoleByRole("ROLE_ADMIN"));
            newUser.setRoles(userRoles);
        } else if (req.getParameter("role").equalsIgnoreCase("editor")) {
            userRoles.add(dao.getRoleByRole("ROLE_USER"));
            userRoles.add(dao.getRoleByRole("ROLE_EDITOR"));
            newUser.setRoles(userRoles);
        } else {
            userRoles.add(dao.getRoleByRole("ROLE_USER"));
            newUser.setRoles(userRoles);
        }
        
        dao.addUser(newUser);
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);

        return "redirect:/";
    }
    
    @RequestMapping(value = "/createNewUser", method = RequestMethod.POST)
    public String createNewUser(HttpServletRequest req, Model model) {
        User newUser = new User();
        newUser.setUsername(req.getParameter("username"));
        String clearPw = req.getParameter("password");
        String hashPw = encoder.encode(clearPw);
        newUser.setPassword(hashPw);
        newUser.setEnabled(true);
        
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(dao.getRoleByRole("ROLE_USER"));
        newUser.setRoles(userRoles);
        
        dao.addUser(newUser);
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);

        return "redirect:/";
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public String deleteUser(Integer id, Model model) {
        dao.deleteUser(id);
        
        List<Page> pageList = dao.getAllPages();
        model.addAttribute("pageList", pageList);
        return "redirect:displayAdminPage";
    }

    @RequestMapping(value = "/displayUserForm", method = RequestMethod.GET)
    public String displayUserForm(Map<String, Object> model, Model model2) {
        List<Page> pageList = dao.getAllPages();
        model2.addAttribute("pageList", pageList);
        return "addUserForm";
    }
    
    @RequestMapping(value = "/createAccount", method = RequestMethod.GET)
    public String createAccount(Map<String, Object> model, Model model2) {
        List<Page> pageList = dao.getAllPages();
        model2.addAttribute("pageList", pageList);
        return "userCreateAccount";
    }

}
