package com.example.hiveinform.service;

import com.example.hiveinform.entity.Message;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface MessageService {
    public void sendMessage (Message message) throws IOException, TimeoutException;

    public void listenMessage(long id) throws IOException, TimeoutException;

    public List<Message> getMessage(long userId, String messageId);
}
