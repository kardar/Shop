package com.shop.user;

import com.shop.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface UserRepository extends PagingAndSortingRepository<User,Integer> {

    @Query("SELECT u from User u where u.email = :email")
    public User getUserByEmail(@Param("email") String email);

    public Long countById(int id);

    @Query("UPDATE User u SET u.isEnabled = ?2 where u.id = ?1" )
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);
}
