package com.yx.nas.model.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public abstract class BaseSystemEvent extends ApplicationEvent {

    /**
     * 是否是父事件（已由父容器处理过）
     * 用于防止事件在子 Context 中重复分发
     */
    private Boolean parentEvent = false;

    public BaseSystemEvent(Object source) {
        super(source);
    }
}
