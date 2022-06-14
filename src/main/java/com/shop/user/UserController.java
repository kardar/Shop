package com.shop.user;

import com.shop.entity.Role;
import com.shop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        user.setEnabled(true);
        List<Role> listRole = userService.listRole();
        model.addAttribute("user",user);
        model.addAttribute("listRole",listRole);
        return "user_form";
    }
    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes){
        System.out.println(user);
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "User is added successfully.");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{edit}")
    public String editUser(@PathVariable(name = "id")Integer id, RedirectAttributes redirectAttributes,Model model){
        try {
            User user = userService.get(id);
            model.addAttribute("user",user);
            return "user_form";
        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
        }

    }
}
