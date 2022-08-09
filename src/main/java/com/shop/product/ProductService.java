package com.shop.product;

import com.shop.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<Product> listAll(){
         return (List<Product>) repo.findAll();
    }

    public Product save(Product product){
        if (product.getId() == null){
            product.setCreatedTime(new Date());
        }
        if (product.getAlias() == null || product.getAlias().isEmpty()){
            String defaultAlias = product.getName().replace(" ","-");
            product.setAlias(defaultAlias);
        }else {
            product.setAlias(product.getAlias().replace(" ","-"));
        }
        product.setUpdatedTime(new Date());
        return repo.save(product);
    }

    public String checkUnique(Integer id, String name){

        boolean isCreatingNew = (id == null || id == 0);
        Product productByName = repo.findByName(name);

        if (isCreatingNew){
            if (productByName != null) return "Duplicate";
        }else {
            if (productByName != null && productByName.getId() != id){
                return "Duplicate";
            }
        }
        return "OK";
    }
    public void updateProductEnabledStatus(Integer id, boolean enabled){
        repo.updateEnabledStatus(id,enabled);
    }
    public void delete(Integer id){
        Long countById = repo.countById(id);
        if (countById == null || countById == 0){
            throw new NoSuchElementException("Product not fund");
        }
        repo.deleteById(id);
    }
    public Product get(Integer id){
        try {
            return repo.findById(id).get();
        }catch (NoSuchElementException nsa){
            throw new NoSuchElementException("No product found");
        }
    }

}
