// Plugins
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import Vue from '@vitejs/plugin-vue'
import Vuetify, { transformAssetUrls } from 'vite-plugin-vuetify'
import Fonts from 'unplugin-fonts/vite'

// Utilities
import { defineConfig } from 'vite'
import { fileURLToPath, URL } from 'node:url'
import {federation} from "@module-federation/vite";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    Vue({
      template: { transformAssetUrls },
    }),
    // https://github.com/vuetifyjs/vuetify-loader/tree/master/packages/vite-plugin#readme
    Vuetify(),
    Components({
      // 默认是 ['src/components']，这里加上你新的目录
      dirs: ['src/components', 'src/pages'],
      extensions: ['vue'], // 需要的话可加 'tsx'
      deep: true,          // 递归子目录
    }),
    AutoImport({
      imports: [
        'vue',
        {
          '@/plugins/axios.ts': ['axiosInstance'],
        },
      ],
      dts: 'src/auto-imports.d.ts',
      eslintrc: {
        enabled: true,
      },
      vueTemplate: true,
    }),
    Fonts({
      fontsource: {
        families: [
          {
            name: 'Roboto',
            weights: [100, 300, 400, 500, 700, 900],
            styles: ['normal', 'italic'],
          },
        ],
      },
    }),
    federation({
      name: 'm-team-resource-fetcher',
      exposes: {
        './App': './src/exposes/app/mount.ts',
        './App-Setting': './src/exposes/app-setting/mount.ts',
      },
      filename: 'remoteEntry.js',
      manifest: true,
      shared: {
        vue: { singleton: true, requiredVersion: '^3.4.0' },
      },
    }),
  ],
  optimizeDeps: {
    exclude: ['vuetify'],
  },
  define: { 'process.env': {} },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('src', import.meta.url)),
    },
    extensions: [
      '.js',
      '.json',
      '.jsx',
      '.mjs',
      '.ts',
      '.tsx',
      '.vue',
    ],
  },
  server: {
    port: 3002,
    host: '0.0.0.0',
    origin: 'http://192.168.123.249:3002',
    cors: true,
    proxy: {
      // 以 /api 开头的请求转发到后端
      '/0fb0cde6-545c-4172-82b8-d2404dbbfb51/api': {
        target: 'http://127.0.0.1:8082/', // 你的后端
        changeOrigin: true,               // 修改 Origin 为 target
      },
    },
  },
  css: {
    preprocessorOptions: {
      sass: {
        api: 'modern-compiler',
      },
      scss: {
        api: 'modern-compiler',
      },
    },
  },
  // base: '/0fb0cde6-545c-4172-82b8-d2404dbbfb51/',
  base: '/',
  build: {
    target: 'esnext' ,
    // minify: false,
    // sourcemap: true,
    outDir: 'dist',
    assetsDir: '',   // 👈 静态资源直接输出在 dist 根目录
  },
})
