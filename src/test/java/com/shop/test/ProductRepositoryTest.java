package com.shop.test;

import com.shop.entity.Brand;
import com.shop.entity.Catogry;
import com.shop.entity.Product;
import com.shop.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.Table;
import java.util.Date;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct(){
        Brand brand = entityManager.find(Brand.class,11);
        Catogry catogry = entityManager.find(Catogry.class, 36);

        Product product = new Product();
        product.setName("Puma Runner");
        product.setAlias("Puma_Runner");
        product.setShortDescription("A GOOD Shoes from Puma");
        product.setLongDescription("A very good full description of shoes");

        product.setBrand(brand);
        product.setCategory(catogry);

        product.setPrice(100);
        product.setCost(80);
        product.setEnabled(true);
        product.setInStock(true);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = repo.save(product);
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllProducts(){
        Iterable<Product> iterableProduct = repo.findAll();
        iterableProduct.forEach(System.out::println);
    }

    @Test
    public void testGetProduct(){
        Integer id = 2;
        Product product = repo.findById(id).get();
        System.out.println(product);
        assertThat(product).isNotNull();
    }

    @Test
    public void saveProductWithImages(){
        Integer productId = 1;
        Product product = repo.findById(productId).get();

        product.setMainImage("main image.jpg");
        product.addExtraImage("extra image 1.jpg");
        product.addExtraImage("extra image 2.jpg");
        product.addExtraImage("extra image 3.jpg");

        Product savedProduct = repo.save(product);
        assertThat(savedProduct.getImages().size()).isEqualTo(3);

    }
}
