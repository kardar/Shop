package com.shop.category;

import com.shop.entity.Catogry;
import com.shop.user.UploadFileUtil;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService service;
    @GetMapping("/categories")
    public String listFirstPage( @Param("sortDir")String sortDir, Model model){
           return listByPage(1,sortDir,model);
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum")int pageNum,@Param("sortDir") String sortDir, Model model){
        if (sortDir == null || sortDir.isEmpty()){
            sortDir = "asc";
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Catogry> listCategories = service.listByPage(pageInfo,pageNum,sortDir);
        model.addAttribute("totalPages",pageInfo.getTotalPages());
        model.addAttribute("totalItems",pageInfo.getTotalElements());
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("listCategories",listCategories);
        model.addAttribute("reverseSortDir",reverseSortDir);
        return "categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model){
        List<Catogry> listCategory = service.getCategoriesUsedInForm();
        model.addAttribute("category", new Catogry());
        model.addAttribute("listCategory",listCategory);
        model.addAttribute("pageTitle","Create New Category");
        return "category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Catogry catogry, @RequestParam("fileImage")MultipartFile multipartFile,
             RedirectAttributes ra) throws IOException {
             if (!multipartFile.isEmpty()){
                 String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                 catogry.setImage(fileName);
                 Catogry savedCategory = service.save(catogry);
                 String uploadDir = "category-images/"+ savedCategory.getId();
                 UploadFileUtil.cleanDirectory(uploadDir);
                 UploadFileUtil.saveFile(uploadDir,fileName,multipartFile);
             } else {
                 service.save(catogry);
             }
           ra.addFlashAttribute("message","The Category is been saved");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id,Model model,
        RedirectAttributes ra){

        try{
            Catogry catogry = service.get(id);
            List<Catogry> listCategory = service.getCategoriesUsedInForm();

            model.addAttribute("category", catogry);
            model.addAttribute("listCategory",listCategory);
            model.addAttribute("pageTitle","Edit Category (ID "+id+")");
            return "/category_form";
        }catch (NoSuchElementException nse){
            ra.addFlashAttribute("message",nse.getMessage());

            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id")Integer id,
                                              @PathVariable("status")boolean enabled,
                                              RedirectAttributes ra){
             service.updateCategoryEnabledStatus(id,enabled);
             String status = enabled ? "enabled" : "disabled";
             String message = "The Category ID "+id+" has been "+status;
             ra.addFlashAttribute("message", message);
             return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id")Integer id,
                                              Model model,
                                              RedirectAttributes ra){
        try{
            service.delete(id);
            String categoryDir = "category-images/" +id;
            UploadFileUtil.removeDir(categoryDir);
            ra.addFlashAttribute("message","The Category ID "+id+" Deleted");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/categories";
    }
}
