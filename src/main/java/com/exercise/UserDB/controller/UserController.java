package com.exercise.UserDB.controller;

import com.exercise.UserDB.service.UserMongoService;
import com.exercise.UserDB.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Value("${app.title}")
    private String appName;

    @Value("${page.title}")
    private String pageTitle;

    private final UserService userService;

    private final UserMongoService userMongoService;

    public UserController(final UserService userService, final UserMongoService userMongoService) {
        this.userService = userService;
        this.userMongoService = userMongoService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping( "/")
    public String showUserList(Model model) {
        log.info("Showing user list");
        model.addAttribute("users",userService.getAllUsers());
        model.addAttribute("appName",appName);
        return "index";
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping( "/m")
    public String showUserMongoList(Model model) {
        log.info("Showing user mongo list");
        model.addAttribute("users",userMongoService.findAll());
        model.addAttribute("appName",appName);
        return "index";
    }


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping( "/mt")
    public String showTitle(Model model) {
        log.info("Showing project title");
        model.addAttribute("pageTitle",pageTitle);
        return "title";
    }
}
