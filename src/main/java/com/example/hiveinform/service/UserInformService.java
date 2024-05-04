package com.example.hiveinform.service;

import com.example.hiveinform.dto.UserInformDto;
import com.example.hiveinform.entity.UserInform;

public interface UserInformService {
    UserInform addInform(UserInformDto userInformDto);

    UserInform updateInform(UserInformDto userInformDto);

    UserInform findInform(long userId);

    UserInform registerSetInform(UserInformDto userInformDto);

    UserInform getInform();
}
