/**
 * Z-Space 预认证设置 API 服务
 */
import type { SysPreAuthInput, SysPreAuthView } from '@/types/sys-pre-auth'
import { axiosInstance } from '@/plugins/axios'

/**
 * 通用返回结果
 */
interface CommonResult<T> {
  code: number
  message?: string
  data?: T
}

/**
 * 保存预认证设置
 * @param config 预认证配置
 */
export async function saveSetting(config: SysPreAuthInput): Promise<CommonResult<void>> {
  const response = await axiosInstance.post('/api/z-space/saveSetting', config)
  return response.data as CommonResult<void>
}

/**
 * 获取预认证设置
 * @param id 设置ID
 */
export async function getSetting(id: number): Promise<CommonResult<SysPreAuthView>> {
  const response = await axiosInstance.post(`/api/z-space/getSetting/${id}`)
  return response.data as CommonResult<SysPreAuthView>
}

/**
 * 删除预认证设置
 * @param id 设置ID
 */
export async function deleteSetting(id: number): Promise<CommonResult<void>> {
  const response = await axiosInstance.post(`/api/z-space/deleteSetting/${id}`)
  return response.data as CommonResult<void>
}
