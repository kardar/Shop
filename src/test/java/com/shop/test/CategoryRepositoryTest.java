package com.shop.test;

import com.shop.category.CategoryRepository;
import com.shop.entity.Catogry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repo;

    @Test
    public void testRootCategory(){
     Catogry catogry = new Catogry("Electronics");
     Catogry savedCategory = repo.save(catogry);

     assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory(){
      Catogry parent = new Catogry(5);
      Catogry memory = new Catogry("Memory", parent);
      Catogry savedCategory = repo.save(memory);
        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetCategory(){
        Catogry catogry = repo.findById(2).get();
        System.out.println(catogry.getName());

        Set<Catogry> childern = catogry.getChildren();
        for (Catogry subCategory : childern){
            System.out.println(subCategory.getName());
        }
        assertThat(childern.size()).isGreaterThan(0);
    }
    @Test
    public void testListRootCat(){
        List<Catogry> rootCategory = repo.findRootCategory(Sort.by("name").ascending());
        rootCategory.forEach(cat -> System.out.println(cat.getName()));
    }

    @Test
    public void testFindByName(){
        String name = "Computer";
        Catogry catogry = repo.findByName(name);

        assertThat(catogry).isNotNull();
        assertThat(catogry.getName()).isEqualTo(name);
    }

    @Test
    public void testFindByAlias(){
        String name = "Electronics";
        Catogry catogry = repo.findByAlias(name);

        assertThat(catogry).isNotNull();
        assertThat(catogry.getName()).isEqualTo(name);
    }
}
