/**
 * 系统预认证配置类型定义
 */

/**
 * 基础预认证配置（列表展示用）
 */
export interface BasicPreAuthConfig {
  /** ID */
  id: number
  /** 认证名称 */
  authName: string
  /** 插件ID */
  pluginId: string
  /** 创建时间 */
  createdTime?: string
  /** 修改时间 */
  modifiedTime?: string
}
