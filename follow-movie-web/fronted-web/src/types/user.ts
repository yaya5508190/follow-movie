/**
 * 用户管理类型定义
 */

/**
 * 用户视图
 */
export interface UserView {
  id: number
  userName: string
  nickName?: string
  email?: string
  enabled: boolean
  createTime?: string
  updateTime?: string
}

/**
 * 用户输入
 */
export interface UserInput {
  userName: string
  password: string
  nickName?: string
  email?: string
  enabled?: boolean
}

/**
 * 用户更新输入
 */
export interface UserUpdateInput {
  id: number
  nickName?: string
  email?: string
  enabled?: boolean
  password?: string
}

/**
 * 用户查询规范
 */
export interface UserSpec {
  userName?: string
  nickName?: string
  email?: string
  enabled?: boolean
}

/**
 * 分页结果
 */
export interface PageResult<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

