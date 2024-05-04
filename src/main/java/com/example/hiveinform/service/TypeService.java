package com.example.hiveinform.service;

import com.example.hiveinform.dto.TypeDto;
import com.example.hiveinform.entity.Type;

import java.util.List;

public interface TypeService {
    Type createType(TypeDto typeDto);

    Type getTypeByUserId(Long id);


}
