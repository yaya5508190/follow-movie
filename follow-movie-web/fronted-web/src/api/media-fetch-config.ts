import type { BasicMediaFetchConfig } from '@/types/media-fetch-config'
import { axiosInstance } from '@/plugins/axios'

export interface CommonResult<T> {
  code: number
  message: string
  data: T
}

export async function queryAllFetcherSettings (): Promise<CommonResult<BasicMediaFetchConfig[]>> {
  const response = await axiosInstance.post<CommonResult<BasicMediaFetchConfig[]>>(
    '/mediaResourceFetcherSetting/queryAllFetcherSettings',
  )
  return response.data
}
