package com.example.hiveinform.controller;

import com.example.hiveinform.dto.ChangePassword;
import com.example.hiveinform.dto.UserDto;
import com.example.hiveinform.dto.UserFindAllDto;
import com.example.hiveinform.dto.UserInformDto;
import com.example.hiveinform.entity.User;
import com.example.hiveinform.entity.UserInform;
import com.example.hiveinform.repository.UserRepository;
import com.example.hiveinform.service.RedisService;
import com.example.hiveinform.service.RegisterConfirmService;
import com.example.hiveinform.service.UserInformService;
import com.example.hiveinform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *   userInform 是 账户信息 而不是个人信息
 *      @UserInformService
 *   账户信息是 非敏感信息的类
 * **/

@RestController
@RequestMapping("/userInform")
public class UserInformController {

    @Autowired
    private UserService userService ;
    @Autowired
    private UserInformService userInformService ;

    @Autowired
    private RegisterConfirmService registerConfirmService ;

    @Autowired
    private UserDetailsPasswordService userDetailsPasswordService ;
    @Autowired
    private RedisService redisService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getUserInform/{userId}")
    public UserInform getUserInform(@PathVariable String userId) {
       return userInformService.findInform(Long.parseLong(userId));
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @GetMapping("/getUser")
    public User getUser() {
        long userId = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        return userService.getUserById(userId);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @GetMapping
    public UserInform getUserInformBySys()
    {
        long id = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        return userInformService.findInform(id);
    }

    @Secured({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/addUserInform")
    public UserInform addUserInform(@RequestBody UserInformDto userInformDto)
    {
        return userInformService.addInform(userInformDto);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/updateInform")
    public UserInform updateUserInform (@RequestBody UserInformDto userInformDto)
    {
        return userInformService.updateInform(userInformDto) ;
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/registerInform/{username}/{id}")
    public UserInform registerUserInform (@PathVariable String username, @PathVariable String id)
    {
        UserInformDto userInformDto = new UserInformDto();
        userInformDto.setUserName(username);
        userInformDto.setImageId(id);
        return userInformService.registerSetInform(userInformDto);
    }

    @Secured({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/updateByAdmin")
    public User updateByAdmin (@RequestBody UserDto userDto)
    {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User oldUser = userService.getByUsername(name);
        oldUser.setEmail(userDto.getEmail());
        oldUser.setAddress(userDto.getAddress());
        oldUser.setRole(userDto.getRole());
        oldUser.setBirthDate(userDto.getBirthDate());
        oldUser.setFullName(userDto.getFullName());
        return userService.updateUser(oldUser);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/updateUser/{confirm}")
    public User updateUser (@RequestBody UserDto userDto , @PathVariable String confirm)
    {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User oldUser = userService.getByUsername(name);
        if (oldUser.getId() == userDto.getId())
        {
            String confirmEmail = oldUser.getEmail();

            if (registerConfirmService.confirmCode(confirm, confirmEmail))
            {
                redisService.delete(confirm.toLowerCase());
                oldUser.setEmail(userDto.getEmail());
                oldUser.setAddress(userDto.getAddress());
                oldUser.setFullName(userDto.getFullName());
                oldUser.setBirthDate(userDto.getBirthDate());
                return userService.updateUser(oldUser);
            }
            else throw new RuntimeException("验证失败");
        }
        throw new IllegalArgumentException("你修改不是你自己的账户");
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/changePassword/{confirm}")
    public boolean changePassword(@RequestBody ChangePassword changePassword, @PathVariable String confirm) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User oldUser = userService.getByUsername(name);
        String confirmEmail = oldUser.getEmail();

        if (registerConfirmService.confirmCode(confirm, confirmEmail)) {
            redisService.delete(confirm.toLowerCase());
            oldUser.setPassword(changePassword.getOldPassword());
            UserDetails userDetails = userDetailsPasswordService.updatePassword(oldUser, changePassword.getNewPassword());
            return userDetails!=null;
        } else throw new RuntimeException("验证失败");

    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/changeUser/{userId}")
    public Boolean changeUser (@PathVariable String userId )
    {
        User userById = userRepository.findById(Long.parseLong(userId)).orElse(null);
        if (userById == null)
            throw new IllegalArgumentException("不存在这个用户");
        else {
            if (userById.getRole().equals("ROLE_USER"))
                userById.setRole("");
            else
                userById.setRole("ROLE_USER");
                userRepository.save(userById);
            return true;
        }

    }

    @Secured({"ROLE_SUPERADMIN"})
    @PostMapping("/changeUserHandler/{userId}/{role}")
    public Boolean changeUserHandler (@PathVariable String userId , @PathVariable String role )
    {
        User userById = userRepository.findById(Long.parseLong(userId)).orElse(null);
        if (userById == null)
            throw new IllegalArgumentException("不存在这个用户");
        else {
            userById.setRole(role);
            userRepository.save(userById);
            return true;
        }

    }

    @Secured({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/getAllUser")
    public List<User> getAllUser(@RequestBody UserFindAllDto userFindAllDto){
        return userService.getAll(userFindAllDto);
    }

    @Secured({"ROLE_SUPERADMIN"})
    @GetMapping("/isSuperAdmin")
    public boolean isSuperAdmin()
    {
        return true;
    }
}
