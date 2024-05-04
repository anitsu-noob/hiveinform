package com.example.hiveinform.repository;

import com.example.hiveinform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserRepository extends JpaRepository<User,Long> {

//    org.springframework.security.core.userdetails.User findByUsername(String username);  弃用

    User getUserByUsername (String username);

    List<User> getUserByEmail (String email);

    Page<User> findAll(Pageable pageable);
}
