package com.example.hiveinform.repository;

import com.example.hiveinform.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<Group,String> {
    @Query("{'groupName': {$regex: ?0}}")
    Page<Group> getGroupsByGroupNameLike(String groupName, Pageable pageable);

    Group getGroupByGroupName(String groupName) ;
}
