package com.example.hiveinform.service;

import com.example.hiveinform.dto.CaptchaDto;

import java.io.IOException;

public interface CaptchaService {
    String getCaptcha() throws IOException;

    Boolean verifyCaptcha(CaptchaDto captchaDto);
}
