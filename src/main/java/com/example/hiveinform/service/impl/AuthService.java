package com.example.hiveinform.service.impl;

import com.example.hiveinform.dto.JwtRequest;
import com.example.hiveinform.dto.JwtResponse;
import com.example.hiveinform.dto.RegisterRequest;
import com.example.hiveinform.dto.UserInformDto;
import com.example.hiveinform.entity.Type;
import com.example.hiveinform.entity.User;
import com.example.hiveinform.entity.UserInform;
import com.example.hiveinform.repository.TypeRepository;
import com.example.hiveinform.repository.UserRepository;
import com.example.hiveinform.service.RedisService;
import com.example.hiveinform.service.TypeService;
import com.example.hiveinform.service.UserInformService;
import com.example.hiveinform.service.UserService;
import com.example.hiveinform.util.RabbitMQManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisService redisService ;

    private final RabbitMQManager rabbitMQManager ;
    private final UserInformService userInformService;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final TypeRepository typeRepository;
    private final UserRepository userRepository ;

    public JwtResponse register(RegisterRequest request) {

        if(userRepository.getUserByUsername(request.getUsername()) == null) {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setBirthDate(request.getBirthDate());
            user.setFullName(request.getFullName());
            user.setAddress(request.getAddress());
            user.setRole("ROLE_USER");
            user.setEmail(request.getEmail());
            user.setType(request.getType() == "" || request.getType()==null ? "test" : request.getType());
            User save = userService.save(user);
            Type type = new Type();
            type.setHobby(user.getType());
            type.setUserId(save.getId());
            type.setId(null);
            typeRepository.save(type);
            if (save != null) {
                JwtResponse authResponse = getAuthResponse(user);
                redisService.save(request.getUsername(), authResponse.getJwttoken(), 864500000);
                UserInformDto userInformDto = new UserInformDto();
                userInformDto.setUserId(userService.getByUsername(request.getUsername()).getId());
                userInformDto.setArticleId(new ArrayList<>());
                userInformDto.setGroupReadLine(new HashMap<>());
                userInformDto.setGroupJoined(new ArrayList<>());
                userInformDto.setGroupVisited(new ArrayList<>());
                userInformService.addInform(userInformDto);
                rabbitMQManager.createUserQueue(save.getId());
                return authResponse;
            }
            return null;
        }
        else return null;
    }


    public JwtResponse authenticate(JwtRequest request) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authenticate.isAuthenticated()) {
            User user = this.userService.getByUsername(request.getUsername());
            if (user == null) {
                throw new UsernameNotFoundException("Username " + request.getUsername() + "not found");
            }
            return getAuthResponse(user);
        }
        else return null ;
    }
    //  Get jwtResponse by Authentication but this method is not implemented to get Authentication Token from security framework

    private JwtResponse getAuthResponse(User user) {
        JwtResponse response = new JwtResponse(jwtService.generateToken(user));
        return response;
    }
}