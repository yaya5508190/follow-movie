// filepath: c:\develop\x-project\follow-movie\plugins\pre-auth\z-space\fronted-web\src\types\sys-pre-auth.ts
/**
 * 系统预认证配置类型与运行时枚举实现
 * 使用 `.ts` 文件导出枚举以便在运行时被 Vite/ESM 导入。
 */

/**
 * 认证类型枚举（运行时）
 */
export enum AuthType {
  /** 无认证 */
  NONE = 0,
  /** API Key 认证 */
  API_KEY = 1,
  /** 用户名密码认证 */
  USERNAME_PASSWORD = 2,
}

/**
 * 凭据类型枚举（运行时）
 */
export enum CredentialType {
  /** Cookie 凭据 */
  COOKIE = 1,
}

/**
 * 系统预认证配置输入（类型，仅用于编译时检查）
 */
export interface SysPreAuthInput {
  /** ID（更新时需要） */
  id?: number
  /** 认证名称 */
  authName: string
  /** 认证URL */
  authUrl?: string
  /** 访问用户名 */
  userName?: string
  /** 访问密码 */
  password?: string
  /** 认证类型：0-无，1-API Key，2-用户名密码 */
  authType: AuthType
  /** 认证凭据 */
  credential?: string
  /** 认证凭据类型：1-Cookie */
  credentialType: CredentialType
  /** 额外元数据（JSON格式） */
  extraMetainfo?: object | string
  /** 插件ID */
  pluginId: string
}

/**
 * 系统预认证配置视图（类型，仅用于编译时检查）
 */
export interface SysPreAuthView {
  id: number
  authName: string
  authUrl?: string
  userName?: string
  password?: string
  authType: AuthType
  credential?: string
  credentialType: CredentialType
  extraMetainfo?: object | string
  pluginId: string
  createdTime?: string
  modifiedTime?: string
}

