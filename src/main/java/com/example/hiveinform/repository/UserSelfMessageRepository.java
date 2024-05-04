package com.example.hiveinform.repository;

import com.example.hiveinform.entity.UserSelfMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSelfMessageRepository extends MongoRepository<UserSelfMessage,String> {
    UserSelfMessage getByUserId(long userId);
}
