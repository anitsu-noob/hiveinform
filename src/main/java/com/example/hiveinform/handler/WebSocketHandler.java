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
    /**
     * 用户ID
     */
    private long userId;

    @Autowired
    private RabbitMQManager rabbitMQManager;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    //虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    // 用来存在线连接用户信息
    private static ConcurrentHashMap<Long, Session> sessionPool = new ConcurrentHashMap<Long, Session>();

    /**
     * 客户端与服务端连接成功
     *
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") long userId) {
        try {
            this.session = session;
            this.userId = userId;
            sessionPool.put(userId, session);
            log.info("【websocket message】have a new connection，total:" + webSockets.size());
        } catch (Exception e) {
        }

    }

    /**
     * 客户端与服务端连接关闭
     *
     * @param session
     * @param userId
     */
    @OnClose
    public void onClose(Session session, @PathParam("userId") long userId) {
        try {
            sessionPool.remove(this.userId);
            log.info("【websocket message】remove a connection，total:" + webSockets.size());
        } catch (Exception e) {
        }
    }

    /**
     * 客户端与服务端连接异常
     *
     * @param error
     * @param session
     * @param userId
     */
    @OnError
    public void onError(Throwable error, Session session, @PathParam("userId") long userId) {
    }

    /**
     * 客户端向服务端发送消息
     *
     * @param message
     * @param userId
     * @throws IOException
     */
    @OnMessage
    public void onMsg(Session session, String message, @PathParam("userId") long userId) throws IOException {
        session=this.session;
        System.out.println("【 has a stupid send a message to a disuse method 】: the session is"+session+"and the stupid send the message :"+message +"userid is :"+userId);
    }

    public void sendOneMessage(long userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()) {
            try {
                log.info("【websocket message】 single Message  :" + message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息(多人)
    public void sendMoreMessage(List<Long> userIds, String message) {
        for (long userId : userIds) {
            Session session = sessionPool.get(userId);
            if (session == null)
                continue;
            if (session != null && session.isOpen()) {
                try {
                    log.info("【websocket message】 single to different person Message :" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
