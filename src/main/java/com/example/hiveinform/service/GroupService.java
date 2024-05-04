package com.example.hiveinform.service;

import com.example.hiveinform.dto.GroupDto;
import com.example.hiveinform.entity.Group;

import java.io.IOException;
import java.util.List;

public interface GroupService {

    List<Group> findGroup(GroupDto groupDto);

    Group createGroup(Group group) throws IOException;

    void deleteGroup(String groupId) throws IOException;

    Group updateGroup(Group group) ;

    boolean addGroup(String id) throws IOException;

    boolean removeGroup(String id) throws IOException;

    List<Group> getGroupByRecommend() ;

    Group getGroup(String id);

    Group getGroupById(String id) throws IOException;

}
