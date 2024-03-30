package com.esmt.lms.controller;

import com.esmt.lms.model.User;
import com.esmt.lms.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    // Method to show the registration form
    @GetMapping
    public String showRegistrationForm(Model model) {
        // Create a new user object to bind form data
        User user = new User();
        model.addAttribute("user", user);
        return "/registration";
    }

    // Method to process the registration form submission
    @PostMapping
    public String processRegistrationForm(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            // If there are errors, return to the registration form
            return "/registration";
        }

        // Here you would typically save the user to the database
         userService.addNew(user);


        // Redirect to a success page or login page
        return "redirect:/login?success";
    }
}
