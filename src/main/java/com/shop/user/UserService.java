package com.shop.user;

import com.shop.entity.Role;
import com.shop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired()
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        boolean isUpdatingUser = (user.getId() != null);
        if (isUpdatingUser){
           User existingUser = userRepo.findById(user.getId()).get();
           if (user.getPassword().isEmpty()){
               user.setPassword(existingUser.getPassword());
           }
        }else {
            encodePassword(user);
        }

        userRepo.save(user);
    }
    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
    public boolean isEmailUnique(Integer id,String email){
        User userByEmail = userRepo.getUserByEmail(email);
        if(userByEmail == null) return true;
        boolean isCreatingNew = (id == null);
        if (isCreatingNew){
            if (userByEmail != null) return false;
        }else {
            if (userByEmail.getId() != id){
                return false;
            }
        }
        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try{
            return userRepo.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UserNotFoundException("user not found with id:"+id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepo.countById(id);
        if (countById == null || countById == 0){
            throw new UserNotFoundException("user not found with id:"+id);
        }
        userRepo.deleteById(id);
    }
}
