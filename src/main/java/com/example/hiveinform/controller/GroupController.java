package com.example.hiveinform.controller;


import com.example.hiveinform.dto.GroupDto;
import com.example.hiveinform.entity.Group;
import com.example.hiveinform.entity.UserInform;
import com.example.hiveinform.repository.UserInformRepository;
import com.example.hiveinform.repository.UserRepository;
import com.example.hiveinform.service.GroupService;
import com.example.hiveinform.service.UserInformService;
import com.example.hiveinform.service.UserService;
import com.example.hiveinform.util.RabbitMQManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/group")
@RestController
public class GroupController {
    @Autowired
    private UserService userService ;
    @Autowired
    private GroupService groupService;

    @Autowired
    private UserInformService userInformService ;

    @Autowired
    private RabbitMQManager rabbitMQManager ;

    @Autowired
    private UserInformRepository userInformRepository;

    @PostMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN","ROLE_SUPERADMIN"})
    public Group createGroup(@RequestBody GroupDto groupDto) throws IOException {
        Group group = new Group();
        group.setGroupName(groupDto.getGroupName());
        group.setDescription(groupDto.getDescription());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long userId = userService.getByUsername(authentication.getName()).getId();
        UserInform inform = userInformService.findInform(userId);
        List<Long> userIds =  new ArrayList<>() ;
        userIds.add(userId);
        group.setUserId(userIds);
        group.setCreateUser(userId);
        group.setAdminId(userIds);
        group.setHeat(0);
        group.setStatus(true);
        group.setBelongsUser(userId);
        group.setLevel(0);
        group.setImageId(groupDto.getImageId() == null ? "653b548e46ffbd15d42f524e" : groupDto.getImageId());
        Group group1 = groupService.createGroup(group);
        inform.getGroupJoined().add(group1.getId());
        inform.getGroupVisited().add(group1.getId());
        userInformRepository.save(inform);
        rabbitMQManager.createGroupExchange(group1.getCreateUser(),group1.getId());
        return group1;
    }

    @GetMapping
    public List<Group> getGroups() {
        // 获取推荐的 group 意思是 流量前几位 （获取的方式很简单）
        return groupService.getGroupByRecommend();
    }

    @PostMapping("/get")
    @Secured({"ROLE_USER", "ROLE_ADMIN","ROLE_SUPERADMIN"})
    public List<Group> findByGroup(@RequestBody GroupDto groupDto)
    {
        return groupService.findGroup(groupDto);
    }

    @PostMapping("/update")
    @Secured({"ROLE_USER", "ROLE_ADMIN","ROLE_SUPERADMIN"})
    public Group updateGroup(@RequestBody Group group)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Group group1 = groupService.getGroup(group.getId());
        if (authentication.isAuthenticated() && authentication != null  )
        {
            long id = userService.getByUsername(authentication.getName()).getId();
            for (Long aLong : group1.getAdminId()) {
                if (id == aLong || id == group1.getBelongsUser())
                {
                    return groupService.updateGroup(group);
                }
            }
        }
        throw new IllegalArgumentException(" the method has the error , the Authentication's user not the admin or the authentication has some error . the user :" + userService.getByUsername(authentication.getName()).getId());
    }

    @PostMapping("/add/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN","ROLE_SUPERADMIN"})
    public void addGroup(@PathVariable String id) throws IOException {
        if (!groupService.addGroup(id))
            throw new IllegalArgumentException(" join the group failed please check the Authentication or group models");
    }


    @GetMapping("/getGroup/{id}")
    public Group getGroup(@PathVariable String id) {
        Group group = groupService.getGroup(id);
        if (group == null)
            throw new IllegalArgumentException("haven't found group");
        return group;
    }

    @GetMapping("/getGroupJoinedList")
    @Secured({"ROLE_USER", "ROLE_ADMIN","ROLE_SUPERADMIN"})
    public List<Group> getJoinedList() throws IOException {
        List<String> groupJoineds = userInformService.getInform().getGroupJoined();
        List<Group> groups = new ArrayList<>();
        for (String groupJoined : groupJoineds){
            if (groupJoined.equals("") || groupJoined == null)
                continue;
            Group group = groupService.getGroupById(groupJoined);
            if (group != null && group.isStatus())
                groups.add(group);
            else continue;
        }
        if (groups.size() == 0)
            throw new IllegalStateException("you haven't joined any groups");
        return groups;
    }


    @GetMapping("/getGroupVisitedList")
    @Secured({"ROLE_USER", "ROLE_ADMIN","ROLE_SUPERADMIN"})
    public List<Group> getVisitedList() throws IOException {
        List<String> groupJoineds = userInformService.getInform().getGroupVisited();
        List<Group> groups = new ArrayList<>();
        for (String groupJoined : groupJoineds){
            Group group = groupService.getGroupById(groupJoined);
            if (groupJoined.equals("") || groupJoined == null)
                continue;
            if (group != null && group.isStatus())
                groups.add(group);
        }
        if (groups.size() == 0)
            throw new IllegalStateException("you haven't visited any groups");
        return groups;
    }

    @GetMapping("/removeGroup/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN","ROLE_SUPERADMIN"})
    public boolean removeGroup(@PathVariable String id) throws IOException {
        return groupService.removeGroup(id);
    }

    @GetMapping("/delGroup/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN","ROLE_SUPERADMIN"})
    public void delGroup(@PathVariable String id) throws IOException {
        groupService.deleteGroup(id);
    }
}

