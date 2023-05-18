package org.example;


import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@ServerEndpoint("/socket/chat")
public class WebSocketChat {
    // 클라이언트가 접속될 때마다 생성되어 클라이언트와 직접 통신하는 클래스
    // 클라이언트가 접속할 때마다 session 관련 정보를 정적으로 저장하여, 1:N 통신이 가능하도록 한다.
    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) throws Exception {
        if(!clients.contains(session)) {
            clients.add(session);
        }else  {
          // 이미 연결된 세션
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        for (Session client : clients) {
            client.getBasicRemote().sendText(message);
        }
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
    }
}
