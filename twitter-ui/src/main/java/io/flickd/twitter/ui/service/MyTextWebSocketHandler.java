package io.flickd.twitter.ui.service;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import io.flickd.twitter.model.TweetData;


@Component
public class MyTextWebSocketHandler extends TextWebSocketHandler{


	@Autowired
	private Jackson2JsonObjectMapper objMap;
    private Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();

    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }
  
    public void send(TweetData tweetData) {
    	System.out.println(">>> onStatsReceived was invoked with " + tweetData.toString());
    	for (WebSocketSession session : sessions.values()) {
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(objMap.toJson(tweetData)));
                    System.out.println(">>> Message" + tweetData.toString() + " was sent" + "websocket session: " + session);
                } catch (IOException e) {
                    System.out.println("IOException caught when sending message: " + e);
                } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }
}
