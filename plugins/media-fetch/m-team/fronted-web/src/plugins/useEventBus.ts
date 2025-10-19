import {inject} from 'vue'
import type {Emitter} from 'mitt'

// 定义事件类型（与主应用保持一致）
type MicroFrontendEvents = {
  'plugin:show-message': { message: string, type: 'success' | 'error' | 'warning' | 'info' }
}

export function useEventBus() {
  const eventBus = inject<Emitter<MicroFrontendEvents>>('eventBus')

  if (!eventBus) {
    console.warn('事件总线未注入，某些功能可能无法使用')
  }

  return eventBus
}

export type {MicroFrontendEvents}



