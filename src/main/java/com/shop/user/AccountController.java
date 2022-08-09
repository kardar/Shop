package com.shop.user;

import com.shop.entity.User;
import com.shop.security.ShopUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal ShopUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
         User user = userService.getByEmail(email);
         model.addAttribute("user", user);

         return "account_form";
    }

    @PostMapping("/account/update")
    public String saveDetails(User user, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal ShopUserDetails loggedUser,
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
            userService.updateAccount(user);
        }
        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());
        redirectAttributes.addFlashAttribute("message", "Your account details has been updated.");
        return "redirect:/account";
    }
}
