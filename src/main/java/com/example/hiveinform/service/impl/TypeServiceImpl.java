package com.example.hiveinform.service.impl;

import com.example.hiveinform.dto.TypeDto;
import com.example.hiveinform.entity.Type;
import com.example.hiveinform.entity.User;
import com.example.hiveinform.repository.TypeRepository;
import com.example.hiveinform.repository.UserRepository;
import com.example.hiveinform.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("typeService")
public class TypeServiceImpl implements TypeService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Override
    @Transactional
    public Type createType(TypeDto typeDto) {
        User user = userRepository.findById(typeDto.getId()).orElse(null) ;
        List<Type> types = new ArrayList<Type>();
        if (user == null) {
            throw new RuntimeException("Illegal injection");
        }

        Type type = typeRepository.getTypesByUserId(typeDto.getId());
        if (type == null) {
            type = new Type();
            type.setUserId(typeDto.getId());
            type.setHobby("test");
            type.setId(null);
        }
        else
        type.setHobby(typeDto.getHobbies());
        return typeRepository.save(type);
    }

    @Override
    @Transactional
    public Type getTypeByUserId(Long id) {
        User user = userRepository.findById(id).orElse(null) ;
        if (user == null)
            throw new RuntimeException("Illegal injection");
        Type type = typeRepository.getTypesByUserId(user.getId());
        return type;
    }
}
