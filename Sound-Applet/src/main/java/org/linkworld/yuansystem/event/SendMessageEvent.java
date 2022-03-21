package org.linkworld.yuansystem.event;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/3
 */


import org.springframework.context.ApplicationEvent;

public class SendMessageEvent extends ApplicationEvent {
    public SendMessageEvent(Object source) {
        super(source);
    }
}
