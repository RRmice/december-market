package com.geekbrains.decembermarket.services;

import com.geekbrains.decembermarket.entites.Role;
import com.geekbrains.decembermarket.entites.User;
import com.geekbrains.decembermarket.repositories.RoleRepository;
import com.geekbrains.decembermarket.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private HashMap<User, String> confirmKeys;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void init(){
        confirmKeys = new HashMap<User, String>();
    }

    public User findByPhone(String phone) {
        return userRepository.findOneByPhone(phone);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOneByPhone(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(user.getPhone(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public boolean createNewUser(User user){

        if (isUserExist(user.getPhone())){
            return false;
        }

        setBaseRole(user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setIsConfirm(0);
        userRepository.save(user);

        generateKey(user);

        return true;

    }

    private void setBaseRole(User user){

        ArrayList<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findOneByName("ROLE_CUSTOMER"));

        user.setRoles(roles);
    }

    public boolean isUserExist(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public void generateKey(User user) {
        Random rnd = new Random();

        confirmKeys.put(user,
               String.format("%04d", rnd.ints(0, 9999).findFirst().getAsInt()));

    }

    public void confirmUser(User user, String pin){

        if (confirmKeys.get(user).equals(pin)){
            user.setIsConfirm(1);
            userRepository.save(user);
        }

    }

    public String getConfirmKeys(User user) {
        // TODO: 07.01.2020 Заглушка удалить
        return confirmKeys.get(user);
    }

    public boolean isUserConfirm(User user){
        return user.getIsConfirm() > 0;
    }

}