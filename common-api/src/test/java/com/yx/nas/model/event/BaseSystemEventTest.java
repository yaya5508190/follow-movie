package com.yx.nas.model.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BaseSystemEventTest {

    @Test
    @DisplayName("parentEvent 默认为 false")
    void testParentEventDefaultValue() {
        TestEvent event = new TestEvent(this);
        assertThat(event.getParentEvent()).isFalse();
    }

    @Test
    @DisplayName("parentEvent 可以设置为 true")
    void testParentEventCanBeSetToTrue() {
        TestEvent event = new TestEvent(this);
        event.setParentEvent(true);
        assertThat(event.getParentEvent()).isTrue();
    }

    @Test
    @DisplayName("source 正确传递")
    void testSourceIsSetCorrectly() {
        Object source = new Object();
        TestEvent event = new TestEvent(source);
        assertThat(event.getSource()).isSameAs(source);
    }

    // 测试用的具体事件类
    static class TestEvent extends BaseSystemEvent {
        public TestEvent(Object source) {
            super(source);
        }
    }
}
