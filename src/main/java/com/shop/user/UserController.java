package com.shop.user;

import com.shop.entity.Role;
import com.shop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private  UserService userService;

    @GetMapping("/users")
    public String listByPage(Model model){
        return listByPage(1, model);
    }

    @GetMapping("users/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum, Model model){
        Page<User> page = userService.listByPage(pageNum);
        List<User> listUser = page.getContent();
        long startCount = (pageNum - 1) * UserService.USER_PER_PAGE + 1;
        long endCount = startCount + UserService.USER_PER_PAGE - 1;
        if (endCount > page.getTotalElements()){
            endCount = page.getTotalElements();
        }
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUser", listUser);
        return "users";
    }



    @GetMapping("users/new")
    public String getForm(Model model){
        User user = new User();
        user.setEnabled(true);
        List<Role> listRole = userService.listRole();
        model.addAttribute("user",user);
        model.addAttribute("listRole",listRole);
        model.addAttribute("pageTitle", "Create New User");
        return "user_form";
    }
    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
            @RequestParam("image") MultipartFile multipartFile) throws IOException {
       if (!multipartFile.isEmpty()){
           String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
           user.setPhotos(fileName);
           User savedUser = userService.save(user);
           String uploadDir = "user-photos/"+ savedUser.getId();
           UploadFileUtil.cleanDirectory(uploadDir);
           UploadFileUtil.saveFile(uploadDir, fileName, multipartFile);
       }else {
           if (user.getPhotos().isEmpty()) user.setPhotos(null);
           userService.save(user);
       }
        redirectAttributes.addFlashAttribute("message", "User is added successfully.");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id")Integer id, RedirectAttributes redirectAttributes,Model model){
        List<Role> listRole = userService.listRole();

        try {
            User user = userService.get(id);
            model.addAttribute("user",user);
            model.addAttribute("pageTitle", "Edit User (ID :"+id+")");
            model.addAttribute("listRole",listRole);

            return "user_form";
        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
        }

    }

    @GetMapping("/users/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<User> listUser = userService.getAll();
       UserCsvExporter exporter = new UserCsvExporter();
       exporter.export(listUser,response);
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id")Integer id,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        try {
             userService.delete(id);
             redirectAttributes.addFlashAttribute("message", "The user ID"+id+"has been Deleted");
        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/users";
    }
    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnableStatus(@PathVariable("id") Integer id,
                                         @PathVariable("status") boolean enabled,
                                         RedirectAttributes redirectAttributes){

        userService.updateUserEnableStatus(id,enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID" + id + "has been"+status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/users";
    }



    }

