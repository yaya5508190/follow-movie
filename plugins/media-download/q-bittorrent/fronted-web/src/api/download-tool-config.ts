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

