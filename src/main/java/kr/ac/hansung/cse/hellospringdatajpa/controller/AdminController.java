package kr.ac.hansung.cse.hellospringdatajpa.controller;

import kr.ac.hansung.cse.hellospringdatajpa.entity.Role;
import kr.ac.hansung.cse.hellospringdatajpa.entity.User;
import kr.ac.hansung.cse.hellospringdatajpa.entity.UserInfo;
import kr.ac.hansung.cse.hellospringdatajpa.repo.RoleRepository;
import kr.ac.hansung.cse.hellospringdatajpa.repo.UserRepository;
import kr.ac.hansung.cse.hellospringdatajpa.service.RoleService;
import kr.ac.hansung.cse.hellospringdatajpa.service.UserService;
import kr.ac.hansung.cse.hellospringdatajpa.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionService sessionService;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String adminPage(Model model) {
        List<User> users = userService.findAllUsers();
        List<UserInfo> userInfos = users.stream().map(user -> {
            Boolean isAdmin=user.hasRole("ADMIN");
            return  UserInfo.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRoles())
                    .isAdmin(isAdmin)
                    .build();
        }).toList();


        model.addAttribute("users", userInfos);

        return "admin";
    }

    @RequestMapping(path = "/delete/{user_id}", method = RequestMethod.POST)
    public String deleteUser(@PathVariable("user_id") Integer userId) {
        User user = userService.findUserById(userId);
        userService.deleteUser(user);

        return "redirect:/admin";
    }

    @RequestMapping(path = "/grant/{user_id}", method = RequestMethod.POST)
    public String grantAdmin(
            @PathVariable("user_id") Integer userId
    ) {
        User user = userService.findUserById(userId);
        Role adminRole = roleService.findByName("ADMIN");

        if(!user.hasRole("ADMIN")){
            user.getRoles().add(adminRole);
            userRepository.save(user);
            sessionService.expireUserSessions(user.getEmail());
        }
        return "redirect:/admin";
    }

    @RequestMapping(path = "/revoke/{user_id}", method = RequestMethod.POST)
    public String revokeAdmin(
            @PathVariable("user_id") Integer userId
    ) {
        User user = userService.findUserById(userId);
        Role adminRole = roleService.findByName("ADMIN");

        if(user.hasRole("ADMIN")){
            user.getRoles().remove(adminRole);
            userRepository.save(user);
            sessionService.expireUserSessions(user.getEmail());
        }
        return "redirect:/admin";
    }
}
