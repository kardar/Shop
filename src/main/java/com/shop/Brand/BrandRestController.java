package com.shop.Brand;

import com.shop.category.CategoryDTO;
import com.shop.entity.Brand;
import com.shop.entity.Catogry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class BrandRestController {
    
    @Autowired
    private BrandService brandService;

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoryByBrand(@PathVariable(name = "id") Integer brandId){
        List<CategoryDTO> listCategories = new ArrayList<>();

            Brand brand = brandService.get(brandId);
            Set<Catogry> categories = brand.getCategories();
            for (Catogry catogry : categories){
                CategoryDTO dto = new CategoryDTO(catogry.getId(),catogry.getName());
                listCategories.add(dto);
            }
            return listCategories;
    }
}
