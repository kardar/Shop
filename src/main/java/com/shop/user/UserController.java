package com.shop.user;

import com.shop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private  UserService userService;

    @GetMapping("/users")
    public String listAll(Model model){
        List<User>  listUser = userService.getAll();
        model.addAttribute("listUser",listUser);
        return "users";
    }
    @GetMapping("users/new")
    public String getForm(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "user_form";
    }
}
