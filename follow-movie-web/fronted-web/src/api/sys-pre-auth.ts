/**
 * 预认证配置 API 服务
 */
import type { BasicPreAuthConfig } from '@/types/sys-pre-auth'
import { axiosInstance } from '@/plugins/axios'

export interface CommonResult<T> {
  code: number
  message: string
  data: T
}

/**
 * 查询所有预认证配置
 */
export async function queryAllPreAuthSettings (): Promise<CommonResult<BasicPreAuthConfig[]>> {
  const response = await axiosInstance.post<CommonResult<BasicPreAuthConfig[]>>(
    '/preAuthSetting/queryAllPreAuthSettings',
  )
  return response.data
}
