package com.shop.category;

import com.shop.entity.Catogry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Catogry, Integer> {

    @Query("SELECT c FROM Catogry c WHERE c.parent.id is NULL")
    public List<Catogry> findRootCategory(Sort sort);

    @Query("SELECT c FROM Catogry c WHERE c.parent.id is NULL")
    public Page<Catogry> findRootCategory(Pageable pageable);

    public Catogry findByName(String name);

    public Catogry findByAlias(String alias);

    public Long countById(Integer id);


    @Query("UPDATE Catogry c SET c.enabled = ?2 where c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);
}
