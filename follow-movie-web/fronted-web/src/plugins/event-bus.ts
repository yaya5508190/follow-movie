import mitt from 'mitt'

// 定义微前端事件类型
type MicroFrontendEvents = {
  // 子应用向主应用发送的事件
  'plugin:show-message': { message: string, type: 'success' | 'error' | 'warning' | 'info' }

  // 主应用向子应用发送的事件
}

// 创建事件总线实例
export const eventBus = mitt<MicroFrontendEvents>()

// 导出类型供其他地方使用
export type { MicroFrontendEvents }
