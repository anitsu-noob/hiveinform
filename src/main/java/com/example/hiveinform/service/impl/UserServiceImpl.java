package com.example.hiveinform.service.impl;

import com.example.hiveinform.dto.ArticleDto;
import com.example.hiveinform.dto.UserFindAllDto;
import com.example.hiveinform.entity.Article;
import com.example.hiveinform.entity.Type;
import com.example.hiveinform.entity.User;
import com.example.hiveinform.repository.TypeRepository;
import com.example.hiveinform.repository.UserRepository;
import com.example.hiveinform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsPasswordService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private TypeRepository typeRepository;

//    public UserServiceImpl(UserRepository userRepository)
//    {
//        this.userRepository = userRepository;
//    }

    @Override
    public User getByUsername(String username) {
        User byUsername = userRepository.getUserByUsername(username);
        return byUsername;
    }

    @Override
    @Transactional
    public User save(User user) {
        if (userRepository.getUserByUsername(user.getUsername())!=null)
        {
            return null;
        }
        User save = userRepository.save(user);
        return save;
    }

    @Override
    public User getUserById(long id) {
        User user = userRepository.findById(id).orElse(null);
        Type type = typeRepository.getTypesByUserId(user.getId());
        if (!user.getType().equals(type.getHobby()))
            user.setType(type.getHobby());
        return user;
    }

    @Override
    public List<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public User updateUser(User user) {

        if (user != null && user.getPassword() != null && user.getId() != 0 && user.getRole() != null && userRepository.getUserByUsername(user.getUsername())!=null)
        {

            User oldUser = userRepository.getUserByUsername(user.getUsername());
            oldUser.setEmail(user.getEmail());
            oldUser.setAddress(user.getAddress());
            oldUser.setBirthDate(user.getBirthDate());
            oldUser.setFullName(user.getFullName());
            return  userRepository.save(oldUser) ;
        }

        return null;
    }

    @Override
    public List<User> getAll(UserFindAllDto userFindAllDto) {
        PageRequest request = getRequest(userFindAllDto);
        return getArticleList( userRepository.findAll(request) );
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        User findByUsername = userRepository.getUserByUsername(user.getUsername());
        if(passwordEncoder.matches(user.getPassword(), findByUsername.getPassword()));
        {
            findByUsername.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(findByUsername);
        }
    }

    public PageRequest getRequest(UserFindAllDto userFindAllDto)
    {
        return  PageRequest.of(userFindAllDto.getPage(),userFindAllDto.getSize());
    }

    public List<User> getArticleList(Page<User> users)
    {
        List<User> userList = new ArrayList<>();
        for (User user : users) {
            userList.add(user);
        }
        return userList;
    }


}
