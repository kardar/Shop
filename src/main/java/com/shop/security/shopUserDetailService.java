package com.shop.security;

import com.shop.entity.User;
import com.shop.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class shopUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user =  userRepo.getUserByEmail(username);
        if (user != null){
            return new ShopUserDetails(user);
        }
        throw  new UsernameNotFoundException("Username not found");
    }
}
