package com.shop.Brand;

import com.shop.entity.Brand;
import com.shop.entity.Catogry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BrandRepository extends PagingAndSortingRepository<Brand,Integer> {

    public Long countById(Integer id);

    @Query("SELECT NEW Brand(b.id,b.name) FROM Brand b ORDER BY b.name ASC")
    public List<Brand> findALL();
}
