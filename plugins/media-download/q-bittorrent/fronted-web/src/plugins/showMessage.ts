import {type MicroFrontendEvents} from '@/plugins/useEventBus'
import type {Emitter} from "mitt";

export function showMessage(message: string, type: 'success' | 'error' | 'warning' | 'info', eventBus: Emitter<MicroFrontendEvents> | undefined = undefined) {
  eventBus?.emit('plugin:show-message', {message, type})
}



