/**
 * Q-Bittorrent 下载工具配置类型定义
 */

/**
 * 认证类型枚举
 */
export enum AuthType {
  /** 用户名密码 */
  USERNAME_PASSWORD = 1,
  /** Cookie */
  COOKIE = 2,
}

/**
 * 媒体资源获取配置（简化）
 */
export interface MediaFetchConfigSimple {
  /** ID */
  id: number
  /** 名称 */
  name? : string
  /** 来源 */
  fetcherSource? : string
}

/**
 * 预认证配置（简化）
 */
export interface SysPreAuthSimple {
  /** ID */
  id: number
  /** 认证名称 */
  authName: string
}

/**
 * 下载工具配置输入
 */
export interface DownloadToolConfigInput {
  /** ID（更新时需要） */
  id?: number
  /** 名称 */
  name: string
  /** 认证类型 1-用户名密码 2-cookie */
  authType: AuthType
  /** 工具类型 q-bittorrent */
  type: string
  /** 访问地址 */
  url: string
  /** 用户名 */
  username?: string
  /** 密码 */
  password?: string
  /** Cookie */
  cookie?: string
  /** 默认保存路径 */
  savePath: string
  /** 是否默认 */
  defaultTool: boolean
  /** 关联的媒体资源获取配置 ID 列表 */
  mediaFetchConfigIds?: number[]
  /** 关联的预认证配置 ID */
  sysPreAuthId?: number
}

/**
 * 下载工具配置视图
 */
export interface DownloadToolConfigView {
  /** ID */
  id: number
  /** 创建时间 */
  createTime: string
  /** 更新时间 */
  updateTime?: string
  /** 名称 */
  name: string
  /** 认证类型 1-用户名密码 2-cookie */
  authType: AuthType
  /** 工具类型 q-bittorrent */
  type: string
  /** 访问地址 */
  url: string
  /** 用户名 */
  username?: string
  /** 密码 */
  password?: string
  /** Cookie */
  cookie?: string
  /** 默认保存路径 */
  savePath: string
  /** 是否默认 */
  defaultTool: boolean
  /** 关联的媒体资源获取配置列表 */
  mediaFetchConfigs?: MediaFetchConfigSimple[]
  /** 关联的预认证配置 */
  sysPreAuth?: SysPreAuthSimple
}
