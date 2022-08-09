package com.shop.test;

import com.shop.Brand.BrandRepository;
import com.shop.entity.Brand;
import com.shop.entity.Catogry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository repo;

    @Test
    public void testCreateBrand1(){
        Catogry laptop = new Catogry(1);
        Brand acer = new Brand("Lenovo");
        acer.getCategories().add(laptop);
        Brand savedBrand = repo.save(acer);

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);

    }
}
