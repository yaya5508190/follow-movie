/**
 * 下载工具配置 API 服务
 */
import type { BasicDownloadToolConfig } from '@/types/download-tool-config'
import { axiosInstance } from '@/plugins/axios'

export interface CommonResult<T> {
  code: number
  message: string
  data: T
}

/**
 * 查询所有下载工具配置
 */
export async function queryAllDownloadToolSettings (): Promise<CommonResult<BasicDownloadToolConfig[]>> {
  const response = await axiosInstance.post<CommonResult<BasicDownloadToolConfig[]>>(
    '/downloadToolSetting/queryAllDownloadToolSettings',
  )
  return response.data
}
