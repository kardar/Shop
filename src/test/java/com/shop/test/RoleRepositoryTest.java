package com.shop.test;


import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import com.shop.entity.Role;
import com.shop.user.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository repo;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role("Admin","Handle Everything");
        Role savedRole = repo.save(roleAdmin);
       assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRoles(){
        Role roleSalesPerson = new Role("SalesPerson","Manage Product price,"+
                "customers, shipping, orders and Sales Report");
        Role roleEditor = new Role("Editor","Manage categories , brands,products article and menu");

        Role roleShipper = new Role("Shipper","View products,view order  and update order status");
        Role roleAssistant = new Role("Assistant","Manage questions and reviews");
        repo.saveAll(List.of(roleSalesPerson,roleEditor,roleShipper,roleAssistant));

    }
}
