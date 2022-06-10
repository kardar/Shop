package com.shop.user;

import com.shop.entity.Role;
import com.shop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired()
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    public UserService(UserRepository repo) {
        this.userRepo = repo;
    }

    public List<User>  getAll(){
       return (List<User>) userRepo.findAll();
    }

    public List<Role> listRole(){
        return (List<Role>) roleRepo.findAll();
    }

    public void save(User user) {
        userRepo.save(user);
    }
}
