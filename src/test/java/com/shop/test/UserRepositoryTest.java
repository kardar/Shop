package com.shop.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.shop.entity.Role;
import com.shop.entity.User;
import com.shop.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUserWithOneRole(){
        Role roleAdmin = entityManager.find(Role.class,1);
        User userNamH = new User("Na","Manh","namh@gmail.com","nam2020");
        userNamH.addRole(roleAdmin);
        User savedUser = repo.save(userNamH);
        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateUserWithTwoRole(){
        User userRavi = new User("Ravi","Shankar","shankar@gmail.com","ravi2020");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        userRavi.addRole(roleAssistant);
        userRavi.addRole(roleEditor);

        User savedUser = repo.save(userRavi);
        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    public void testListAllUsers(){
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }
    @Test
    public void testGetUserById(){
     User user =  repo.findById(1).get();
        System.out.println(user);
        assertThat(user).isNotNull();
    }

    @Test
    public void testUpdateUser(){
        User user = repo.findById(1).get();
        user.setEnabled(true);
        user.setEmail("javaCode@gmail.com");

        repo.save(user);
    }

    @Test
    public void testUpdateRole(){
        User userRavi = repo.findById(1).get();
        Role roleEditor = new Role(3);
        Role roleSalesPerson = new Role(2);
        userRavi.getRoles().remove(roleEditor);
        userRavi.addRole(roleSalesPerson);
    }
    @Test
    public void testDeleteUserById(){
        Integer userId = 2;
        repo.deleteById(userId);

    }
}
