package com.shop.user;

import com.shop.entity.Role;
import com.shop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    public static final int USER_PER_PAGE = 5;

    @Autowired()
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo) {
        this.userRepo = repo;
    }

    public User getByEmail(String email){
         return  userRepo.getUserByEmail(email);
    }

    public List<User>  getAll(){
       return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
    }

    public List<Role> listRole(){
        return (List<Role>) roleRepo.findAll();
    }

    public Page<User> listByPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber-1, USER_PER_PAGE);
        return userRepo.findAll(pageable);
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);
        if (isUpdatingUser){
           User existingUser = userRepo.findById(user.getId()).get();
           if (user.getPassword().isEmpty()){
               user.setPassword(existingUser.getPassword());
           }
        }else {
            encodePassword(user);
        }

      return   userRepo.save(user);
    }

    public User updateAccount(User userInForm){
        User userInDb = userRepo.findById(userInForm.getId()).get();

        if (!userInForm.getPassword().isEmpty()){
            userInDb.setPassword(userInForm.getPassword());
            encodePassword(userInDb);
        }
        if (userInForm.getPhotos() != null){
            userInDb.setPhotos(userInForm.getPhotos());
        }
        userInDb.setFirstName(userInForm.getFirstName());
        userInDb.setLastName(userInForm.getLastName());
        return userRepo.save(userInDb);
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
    public void updateUserEnableStatus(Integer id, boolean enabled){
        userRepo.updateEnabledStatus(id,enabled);
    }
}
