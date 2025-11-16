/**
 * Snackbar Store
 * 用于显示全局消息提示
 */
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useSnackbar = defineStore('snackbar', () => {
  const show = ref(false)
  const message = ref('')
  const color = ref('success')
  const timeout = ref(3000)

  function showMessage(msg: string, msgColor: string = 'success', msgTimeout: number = 3000) {
    message.value = msg
    color.value = msgColor
    timeout.value = msgTimeout
    show.value = true
  }

  function showSuccess(msg: string, msgTimeout: number = 3000) {
    showMessage(msg, 'success', msgTimeout)
  }

  function showError(msg: string, msgTimeout: number = 3000) {
    showMessage(msg, 'error', msgTimeout)
  }

  function showWarning(msg: string, msgTimeout: number = 3000) {
    showMessage(msg, 'warning', msgTimeout)
  }

  function showInfo(msg: string, msgTimeout: number = 3000) {
    showMessage(msg, 'info', msgTimeout)
  }

  function hide() {
    show.value = false
  }

  return {
    show,
    message,
    color,
    timeout,
    showMessage,
    showSuccess,
    showError,
    showWarning,
    showInfo,
    hide,
  }
})

