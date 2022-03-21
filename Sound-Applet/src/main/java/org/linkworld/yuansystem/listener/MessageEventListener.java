package org.linkworld.yuansystem.listener;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/3
 */



import org.linkworld.yuansystem.event.SendMessageEvent;
import org.linkworld.yuansystem.model.dto.StudentClassDTO;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.awt.*;

@Configuration
public class MessageEventListener {

 @EventListener
 public void SendMessageEvent(SendMessageEvent sendMessageEvent) {

 }



}
