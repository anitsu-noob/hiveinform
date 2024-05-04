package com.example.hiveinform.repository;

import com.example.hiveinform.entity.Article;
import com.example.hiveinform.entity.UserInform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserInformRepository extends MongoRepository<UserInform, String> {
    UserInform getUserInformByUserId(long userId);
}
