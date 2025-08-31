// 统一加载所有 mock 模块
const modules = import.meta.glob('./modules/**/*.ts', { eager: true })

export async function setupMocks () {
  for (const mod of Object.values(modules)) {
    const setup = (mod as any)?.default
    if (typeof setup === 'function') {
      // 支持同步或异步 setup
      await setup()
    }
  }
}
