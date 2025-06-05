package kr.ac.hansung.cse.hellospringdatajpa.controller;

import kr.ac.hansung.cse.hellospringdatajpa.entity.Role;
import kr.ac.hansung.cse.hellospringdatajpa.entity.User;
import kr.ac.hansung.cse.hellospringdatajpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/sign_up")
    public String signUp(Model model) {
        model.addAttribute("user", new User());
        return "sign_up";
    }

    @PostMapping("/sign_up")
    public String processSignUp(@ModelAttribute("user") User user, Model model) {
        Boolean isExist = userService.isExistUserByEmail(user.getEmail());

        if (isExist){
            model.addAttribute("emailExist", true);
            return "sign_up";
        }

        List<Role> userRoles = new ArrayList<>();
        Role userRole = userService.findByRolename("USER");
        userRoles.add(userRole);

        if (user.getEmail().equals("admin@hansung.ac.kr")) {
            Role adminRole = userService.findByRolename("ADMIN");
            userRoles.add(adminRole);
        }

        userService.registerUser(user, userRoles);
        return "redirect:/login";
    }
}
