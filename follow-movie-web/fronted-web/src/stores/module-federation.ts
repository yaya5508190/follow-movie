import type { MFConfig } from '@/types/module-federation.ts'
// Utilities
import { defineStore } from 'pinia'

export const useModuleFederation = defineStore('module-federation', {
  state: () => ({
    mfConfig: {
      remotes: [],
      menus: [],
    } as MFConfig,
  }),

  actions: {
    setMfConfig (mfConfig: MFConfig) {
      this.mfConfig = mfConfig
    },
  },
})
