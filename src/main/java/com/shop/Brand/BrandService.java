package com.shop.Brand;

import com.shop.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    private BrandRepository repo;

    public List<Brand> listAll(){
         return (List<Brand>) repo.findAll();
    }

    public Brand save(Brand brand){
        return repo.save(brand);
    }
    public Brand get(Integer id){
        try {
            return repo.findById(id).get();
        }catch (NoSuchElementException nsa){
            throw new NoSuchElementException("Brand not found");
        }
    }
    public void delete(Integer id){
        Long countById = repo.countById(id);
        if (countById == null || countById == 0){
            throw new NoSuchElementException("Brand not found");
        }

        repo.deleteById(id);
    }
}
