package com.yx.nas.tool.context;

import com.yx.nas.model.event.BaseSystemEvent;
import com.yx.nas.model.event.DownloadEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CrossContextEventBridgeTest {

    @Mock
    private ContextRegistry contextRegistry;

    @Mock
    private ApplicationContext childContext1;

    @Mock
    private ApplicationContext childContext2;

    private CrossContextEventBridge bridge;
    private Map<String, ApplicationContext> contextMap;

    @BeforeEach
    void setUp() {
        contextMap = new ConcurrentHashMap<>();
        // 模拟子 Context 注册
        contextMap.put(UUID.randomUUID().toString(), childContext1);
        contextMap.put(UUID.randomUUID().toString(), childContext2);
        when(contextRegistry.getContextMap()).thenReturn(contextMap);

        bridge = new CrossContextEventBridge(contextRegistry);
    }

    @Test
    @DisplayName("首次事件触发广播到所有子 Context")
    void testFirstEventBroadcastsToAllChildContexts() {
        DownloadEvent event = new DownloadEvent(this, 1L, "qbittorrent", 100L);

        bridge.onEvent(event);

        // 验证事件被广播到所有子 Context
        verify(childContext1, times(1)).publishEvent(event);
        verify(childContext2, times(1)).publishEvent(event);
    }

    @Test
    @DisplayName("parentEvent 为 true 时不再广播")
    void testNoBroadcastWhenParentEventIsTrue() {
        DownloadEvent event = new DownloadEvent(this, 1L, "qbittorrent", 100L);
        event.setParentEvent(true); // 模拟已处理过的事件

        bridge.onEvent(event);

        // 验证不再广播
        verify(childContext1, never()).publishEvent(any());
        verify(childContext2, never()).publishEvent(any());
    }

    @Test
    @DisplayName("广播后 parentEvent 被设置为 true")
    void testParentEventSetToTrueAfterBroadcast() {
        DownloadEvent event = new DownloadEvent(this, 1L, "qbittorrent", 100L);
        assertThat(event.getParentEvent()).isFalse();

        bridge.onEvent(event);

        assertThat(event.getParentEvent()).isTrue();
    }

    @Test
    @DisplayName("空 Context 列表时不抛出异常")
    void testNoExceptionWhenNoChildContexts() {
        when(contextRegistry.getContextMap()).thenReturn(new ConcurrentHashMap<>());
        CrossContextEventBridge bridgeWithEmptyContext = new CrossContextEventBridge(contextRegistry);

        DownloadEvent event = new DownloadEvent(this, 1L, "qbittorrent", 100L);

        // 不应抛出异常
        bridgeWithEmptyContext.onEvent(event);
    }

    @Test
    @DisplayName("可处理任意 BaseSystemEvent 子类")
    void testCanHandleAnyBaseSystemEventSubclass() {
        // 使用匿名内部类创建自定义事件
        BaseSystemEvent customEvent = new BaseSystemEvent(this) {
            public String getCustomField() {
                return "custom";
            }
        };

        bridge.onEvent(customEvent);

        verify(childContext1, times(1)).publishEvent(customEvent);
        verify(childContext2, times(1)).publishEvent(customEvent);
    }
}
