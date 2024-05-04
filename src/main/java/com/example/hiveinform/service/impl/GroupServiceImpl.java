package com.example.hiveinform.service.impl;

import com.example.hiveinform.dto.GroupDto;
import com.example.hiveinform.entity.Group;
import com.example.hiveinform.entity.User;
import com.example.hiveinform.entity.UserInform;
import com.example.hiveinform.entity.UserSelfMessage;
import com.example.hiveinform.repository.GroupRepository;
import com.example.hiveinform.repository.UserInformRepository;
import com.example.hiveinform.repository.UserSelfMessageRepository;
import com.example.hiveinform.service.GroupService;
import com.example.hiveinform.service.UserInformService;
import com.example.hiveinform.service.UserService;
import com.example.hiveinform.util.RabbitMQManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("groupService")
public class GroupServiceImpl implements GroupService {

    @Autowired
    private UserSelfMessageRepository userSelfMessageRepository;

    @Autowired
    private UserInformRepository userInformRepository;

    @Autowired
    private RabbitMQManager rabbitMQManager ;

    @Autowired
    private UserService userService ;

    @Autowired
    private GroupRepository groupRepository ;

    @Override
    public List<Group> findGroup(GroupDto groupDto) {
        PageRequest pageRequest = getRequest(groupDto);
        return getGroup(groupRepository.getGroupsByGroupNameLike(groupDto.getGroupName(),pageRequest));
    }

    @Override
    public Group createGroup(Group group) throws IOException {

        Group save = groupRepository.save(group);
        rabbitMQManager.createGroupExchange(group.getCreateUser(),group.getId());
        return save;
    }

    @Override
    public void deleteGroup(String groupId) throws IOException {
        Group groupByGroupName = groupRepository.findById(groupId).orElse(null);

        if (groupByGroupName!=null)
       {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
               long userId = userService.getByUsername(authentication.getName()).getId();
               if (groupByGroupName.getBelongsUser() == userId) {
                   groupByGroupName.setStatus(false);
                   groupRepository.save(groupByGroupName);
                   rabbitMQManager.deleteExchange(groupByGroupName.getUserId(), groupByGroupName.getId());
               }
               throw new IllegalStateException("you is not this group owner");
           }
           throw new  IllegalStateException("you can't delete group") ;
       }
        throw  new IllegalStateException("haven't this group");
    }

    @Override
    public Group updateGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public boolean addGroup(String id) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            long userId = userService.getByUsername(authentication.getName()).getId();
            Group group = groupRepository.findById(id).orElse(null);
            if (group == null)
                return false;
            List<Long> userIds = group.getUserId();
            if (userIds.indexOf(userId) == -1) {
                UserInform informByUserId = userInformRepository.getUserInformByUserId(userId);
                List<String> groupJoined = informByUserId.getGroupJoined();
                if (groupJoined == null)
                    groupJoined = new ArrayList<String>();
                groupJoined.add(group.getId());
                informByUserId.setGroupJoined(groupJoined);
                userInformRepository.save(informByUserId);
                userIds.add(userId);
                group.setUserId(userIds);
                groupRepository.save(group);
                rabbitMQManager.addUserToGroup(userId, group.getId());
                return true ;
            }
            else {
                return false ;
            }
        }
        else {
            return false ;
        }

    }

    @Override
    public boolean removeGroup(String id) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()&& !authentication.getName().equals("anonymousUser")) {
            long userId = userService.getByUsername(authentication.getName()).getId();
            Group group = groupRepository.findById(id).orElse(null);
            if (group == null) return false;
            List<Long> userIds = group.getUserId();
            if (userIds.indexOf(userId) == -1) {
                return  false ;
            }
            UserInform userInform = userInformRepository.getUserInformByUserId(userId);
            if (!userInform.getGroupJoined().remove(id))
            return false ;
            userIds.remove(userId);
            group.setUserId(userIds);
            userInformRepository.save(userInform);
            groupRepository.save(group);
            rabbitMQManager.removeUserFromGroup(userId, group.getId());
            return true ;
        }
        return false;
    }

    @Override
    public List<Group> getGroupByRecommend() {
        List<Group> all = groupRepository.findAll(Sort.by(Sort.Direction.DESC, "status", "heat"));
        List<Group> result = new ArrayList<>();
        for (Group group : all) {
            if (group.isStatus())
            {
                result.add(group);
            }
        }
        return result;
    }

    @Override
    public Group getGroup(String id) {
        Group group = groupRepository.findById(id).orElse(null);
        if (group == null)
            return null ;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            long userId = userService.getByUsername(authentication.getName()).getId();
            UserInform user = userInformRepository.getUserInformByUserId(userId);
            List<String> groupVisited = user.getGroupVisited();
            if (groupVisited == null)
                groupVisited = new ArrayList<String>();
            groupVisited.remove(group.getId());
            groupVisited.add(group.getId());
            user.setGroupVisited(groupVisited);
            userInformRepository.save(user);
        }
        return group;
    }

    @Override
    public Group getGroupById(String id) throws IOException {
        Group group = groupRepository.findById(id).orElse(null);
        if (group == null)
            return null ;
        return group;
    }

    public PageRequest getRequest(GroupDto groupDto)
    {
        return  PageRequest.of(groupDto.getPage(),groupDto.getSize(), Sort.by(Sort.Direction.DESC,"status","heat"));
    }

    public List<Group> getGroup(Page<Group> groups)
    {
        List<Group> group = new ArrayList<Group>();

        for (Group group1 : groups) {
            group.add(group1);
        }
        return group;
    }
}
