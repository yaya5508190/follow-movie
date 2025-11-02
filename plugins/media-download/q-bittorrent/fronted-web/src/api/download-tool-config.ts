/**
 * Q-Bittorrent 下载工具配置 API 服务
 */
import type { DownloadToolConfigInput, DownloadToolConfigView } from '@/types/download-tool-config'
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
 * 保存下载工具配置
 * @param config 配置
 */
export async function saveSetting (config: DownloadToolConfigInput): Promise<CommonResult<void>> {
  const response = await axiosInstance.post('/api/q-bittorrent/saveSetting', config)
  return response.data as CommonResult<void>
}

/**
 * 获取下载工具配置
 * @param id 设置ID
 */
export async function getSetting (id: number): Promise<CommonResult<DownloadToolConfigView>> {
  const response = await axiosInstance.post(`/api/q-bittorrent/getSetting/${id}`)
  return response.data as CommonResult<DownloadToolConfigView>
}

/**
 * 删除下载工具配置
 * @param id 设置ID
 */
export async function deleteSetting (id: number): Promise<CommonResult<void>> {
  const response = await axiosInstance.post(`/api/q-bittorrent/deleteSetting/${id}`)
  return response.data as CommonResult<void>
}

/**
 * 获取所有媒体资源获取配置列表
 */
export async function getAllMediaFetchConfigs (): Promise<CommonResult<Array<{ id: number; name: string; fetcherSource: string }>>> {
  const response = await axiosInstance.post('/api/mediaResourceFetcherSetting/queryAllFetcherSettings')
  return response.data
}

/**
 * 获取所有预认证配置列表
 */
export async function getAllSysPreAuthConfigs (): Promise<CommonResult<Array<{ id: number; authName: string }>>> {
  const response = await axiosInstance.post('/api/preAuthSetting/queryAllPreAuthSettings')
  return response.data
}
