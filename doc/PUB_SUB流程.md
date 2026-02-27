# Follow-Movie 事件发布/订阅流程

> 最后更新: 2026-02-27

---

## 1. 系统架构 Overview

### 集成运行模式

```mermaid
graph TB
    subgraph 主应用 ["主 ApplicationContext (Host)"]
        direction TB
        MC[ApplicationEventMulticaster<br/>事件多播器]
        CC[CrossContextEventBridge<br/>跨上下文事件桥]
        CR[ContextRegistry<br/>上下文注册表]

        MC --- CC
        CC --- CR
    end

    subgraph 插件mteam ["m-team 插件 Context"]
        P1[MediaResourceTorrentDownloaderImpl<br/>事件发布者]
    end

    subgraph 插件qb ["q-bittorrent 插件 Context"]
        L1[DownloadEventListener<br/>事件监听器]
    end

    P1 -->|"1. getParent().publishEvent()"| MC
    MC -->|"2. @EventListener"| CC
    CC -->|"3. 转发到子Context"| L1
    CC -.->|"注册子Context"| CR
```

### 独立测试模式

```mermaid
graph TB
    A[downloadTorrent] --> B{parent 为 null?}
    B -->|是| C[log.info: 独立测试模式]
    C --> D((End))
```

---

## 2. Spring Context 层级

```mermaid
graph LR
    subgraph Parent ["主 ApplicationContext"]
        direction TB
        MC[ApplicationEventMulticaster]
        CC[CrossContextEventBridge]
        CR[ContextRegistry]
    end

    subgraph Child1 ["子 Context (m-team)"]
        C1[MediaResourceTorrentDownloaderImpl]
    end

    subgraph Child2 ["子 Context (q-bittorrent)"]
        C2[DownloadEventListener]
    end

    C1 -->|"getParent().publishEvent()"| Parent
    Parent -.->|"继承关系"| C1
    Parent -.->|"继承关系"| C2
```

---

## 3. 事件流转时序

### 集成运行模式

```mermaid
sequenceDiagram
    participant M as m-team插件
    participant Host as 主Context
    participant Bridge as CrossContextEventBridge
    participant Reg as Context QB as q-bittorrent插件

    M->>Host: 1Registry
    participant. downloadTorrent()
    M->>Host: 2. getParent().publishEvent(event)

    Host->>Bridge: 3. @EventListener接收
    Note over Bridge: 4. 检查parentEvent标记<br/>(首次为false)
    Bridge->>Bridge: 5. setParentEvent(true)

    Bridge->>Reg: 6. getContextMap()
    Reg-->>Bridge: 返回所有子Context

    Bridge->>QB: 7. publishEvent(event)

    QB->>QB: 8. @EventListener接收
    Note over QB: 9. 检查targetDownloader<br/>="q-bittorrent"
    QB->>QB: 10. 处理下载任务
```

### 独立测试模式

```mermaid
sequenceDiagram
    participant M as m-team Plugin

    M->>M: downloadTorrent()
    Note over M: parent is null, skip event publishing
    M->>M: log.info("Standalone mode: task created")
```

---

## 4. 核心组件

| 组件 | 位置 | 角色 |
|------|------|------|
| `DownloadEvent` | common-api | 事件载体 |
| `MediaResourceTorrentDownloaderImpl` | plugins/media-fetch/m-team | Publisher 发布者 |
| `CrossContextEventBridge` | follow-movie-web | Bridge 桥接器 |
| `ContextRegistry` | follow-movie-web | Registry 注册表 |
| `DownloadEventListener` | plugins/media-download/q-bittorrent | Subscriber 订阅者 |

---

## 5. 事件流程步骤

```mermaid
flowchart TD
    A[用户触发下载] --> B[调用 downloadTorrent]
    B --> C{判断 parent}
    C -->|parent 为 null| D[独立测试模式：打印日志]
    C -->|parent 存在| E[集成运行模式：发布事件]

    D --> F[流程结束]

    E --> G[CrossContextEventBridge 接收]
    G --> H{检查 parentEvent}
    H -->|false| I[设为 true]
    H -->|true| J[跳过处理]

    I --> K[遍历 ContextRegistry]
    K --> L[获取所有子Context]
    L --> M[转发事件到每个子Context]

    M --> N[各插件监听器接收]
    N --> O{匹配 targetDownloader}
    O -->|匹配| P[执行业务逻辑]
    O -->|不匹配| Q[忽略]
```

---

## 6. 相关文件

| 文件 | 路径 |
|------|------|
| 事件定义 | `common-api/src/main/java/com/yx/nas/model/event/DownloadEvent.java` |
| 事件发布者 | `plugins/media-fetch/m-team/src/main/java/.../MediaResourceTorrentDownloaderImpl.java` |
| 事件桥接器 | `follow-movie-web/src/main/java/com/yx/nas/tool/context/CrossContextEventBridge.java` |
| 上下文注册表 | `follow-movie-web/src/main/java/com/yx/nas/tool/context/ContextRegistry.java` |
| 事件监听器 | `plugins/media-download/q-bittorrent/src/main/java/.../DownloadEventListener.java` |

---

*文档由 Claude Code 自动生成*
