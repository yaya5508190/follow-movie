/**
 * 下载工具配置（基础信息）
 */
export interface BasicDownloadToolConfig {
  /** ID */
  id: number
  /** 名称 */
  name: string
  /** 工具类型 */
  type: string
  /** 插件ID */
  pluginId: string
  /** 创建时间 */
  createTime?: string
  /** 修改时间 */
  updateTime?: string
}
