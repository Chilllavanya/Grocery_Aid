package com.GroceryAid.GroceryAid.controllers;

import com.GroceryAid.GroceryAid.dtos.CartDto;
import com.GroceryAid.GroceryAid.dtos.UserDto;
import com.GroceryAid.GroceryAid.entities.User;
import com.GroceryAid.GroceryAid.repositories.GroceryListRepository;
import com.GroceryAid.GroceryAid.services.CartService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AppController {
    @Autowired
    GroceryListRepository groceryListRepository;

    @GetMapping("/")
    public String home(HttpSession session)
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        return "index";
    }

    @GetMapping("/grocery-list/{gListID}")
    public String editGroceryList(@PathVariable("gListID") Long gListID,  HttpSession session, Model model)
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        model.addAttribute("g_list", groceryListRepository.findById(gListID).get());

        System.out.println("sys");

        return "grocery_list";
    }

    @GetMapping("/signup")
    public String signUp(Model model)
    {
        model.addAttribute("userDto", new UserDto());
        return "signup";
    }

    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/";
    }
}
