package com.shop.Brand;

import com.shop.category.CategoryService;
import com.shop.entity.Brand;
import com.shop.entity.Catogry;
import com.shop.user.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BrandController {

    @Autowired
    private BrandService service;
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String listAll(Model model){
        List<Brand> listBrands = service.listAll();
        model.addAttribute("listBrands", listBrands);
        return "/brands";
    }

    @GetMapping("brands/new")
    public String newBrand(Model model){
        List<Catogry> listCategories = categoryService.getCategoriesUsedInForm();
        model.addAttribute("listCategories",listCategories);
        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle","Create New Brand");

        return "/brand_form";
    }

    @PostMapping("/brands/save")
    public String saveBrand(Brand brand, @RequestParam("fileImage")MultipartFile multipartFile,
                            RedirectAttributes ra) throws IOException {
        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);

            Brand savedBrand = service.save(brand);
            String uploadDir = "../brand-logos/"+ savedBrand.getId();

            UploadFileUtil.cleanDirectory(uploadDir);
            UploadFileUtil.saveFile(uploadDir,fileName,multipartFile);
        } else {
            service.save(brand);
        }
        ra.addFlashAttribute("message","The brand saved Successfully");
        return "redirect:/brands";
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra){

        try {
            Brand brand = service.get(id);
            List<Catogry> listCategories = categoryService.getCategoriesUsedInForm();

            model.addAttribute("brand",brand);
            model.addAttribute("listCategories",listCategories);
            model.addAttribute("pageTitle","Edit Brand (ID "+id+")");
            return "/brand_form";
        }catch (NoSuchElementException nsa){
           ra.addFlashAttribute("message",nsa.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id ,Model model,
                              RedirectAttributes ra){

        try{
            service.delete(id);
            String brandDir = "../brand-logos/"+id;
            UploadFileUtil.removeDir(brandDir);

            ra.addFlashAttribute("message","The brand ID: "+id+"deleted");
        } catch (NoSuchElementException | IOException e) {
            ra.addFlashAttribute("message",e.getMessage());
        }
        return "redirect:/brands";
    }
}
