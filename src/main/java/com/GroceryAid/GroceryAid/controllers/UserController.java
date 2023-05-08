package com.GroceryAid.GroceryAid.controllers;


import com.GroceryAid.GroceryAid.dtos.GroceryListDto;
import com.GroceryAid.GroceryAid.dtos.UserDto;
import com.GroceryAid.GroceryAid.entities.GroceryList;
import com.GroceryAid.GroceryAid.entities.User;
import com.GroceryAid.GroceryAid.repositories.GroceryListRepository;
import com.GroceryAid.GroceryAid.repositories.UserRepository;
import com.GroceryAid.GroceryAid.services.GroceryListService;
import com.GroceryAid.GroceryAid.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroceryListController groceryListController;

    @PostMapping(value = "/get-logged-in-user")
    public UserDto getLoggedInUser(HttpSession session) {

        return userService.getLoggedInUser(session);
    }

    @PostMapping(value = "/register", produces = "text/plain")
    public String addUser(@RequestBody UserDto userDto) {
        userService.addUser(userDto);
        return "/login";
    }

    @PostMapping(value="/login", produces = "text/plain")
    public String userLogin(@RequestBody UserDto userDto, HttpSession session) {
        session.setAttribute("user", userService.userLogin(userDto));
        return "/";
    }
}