package com.example.hiveinform.handler;


import com.example.hiveinform.service.MessageService;
import com.example.hiveinform.util.RabbitMQManager;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.WebSocket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")
public class WebSocketHandler {

    private Session session;
    
    private long userId;

    @Autowired
    private RabbitMQManager rabbitMQManager;

    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    
    private static ConcurrentHashMap<Long, Session> sessionPool = new ConcurrentHashMap<Long, Session>();


    @OnOpen
    public void onOpen(Session session, @PathParam("userId") long userId) {
        try {
            this.session = session;
            this.userId = userId;
            sessionPool.put(userId, session);
            log.info("have a new connection :" + webSockets.size());
        } catch (Exception e) {
        }

    }


    @OnClose
    public void onClose(Session session, @PathParam("userId") long userId) {
        try {
            sessionPool.remove(this.userId);
            log.info("remove a connection" + webSockets.size());
        } catch (Exception e) {
        }
    }

    @OnError
    public void onError(Throwable error, Session session, @PathParam("userId") long userId) {
    }

    @OnMessage
    public void onMsg(Session session, String message, @PathParam("userId") long userId) throws IOException {
        session=this.session;
        System.out.println("the session is"+session+"and the stupid send the message :"+message +"userid is :"+userId);
    }

    public void sendOneMessage(long userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()) {
            try {
                log.info("single Message  :" + message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMoreMessage(List<Long> userIds, String message) {
        for (long userId : userIds) {
            Session session = sessionPool.get(userId);
            if (session == null)
                continue;
            if (session != null && session.isOpen()) {
                try {
                    log.info(" single to different person Message :" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
