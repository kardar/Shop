package com.shop.user;

import com.shop.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface UserRepository extends CrudRepository<User,Integer> {

    @Query("SELECT u from User u where u.email = :email")
    public User getUserByEmail(@Param("email") String email);
}
