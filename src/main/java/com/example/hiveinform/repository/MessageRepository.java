package com.example.hiveinform.repository;

import com.example.hiveinform.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message , String > {
}
