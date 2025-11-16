/**
 * 用户管理 API 服务
 */
import type { UserView, UserInput, UserUpdateInput, UserSpec, PageResult } from '@/types/user'
import { axiosInstance } from '@/plugins/axios'

export interface CommonResult<T> {
  code: number
  message: string
  data: T
}

/**
 * 分页查询用户列表
 */
export async function getUserList(
  spec: UserSpec,
  page: number = 0,
  size: number = 20,
  sort: string = 'id,desc'
): Promise<CommonResult<PageResult<UserView>>> {
  const params = {
    ...spec,
    page,
    size,
    sort,
  }
  const response = await axiosInstance.get<CommonResult<PageResult<UserView>>>(
    '/user/list',
    { params }
  )
  return response.data
}

/**
 * 查询所有用户
 */
export async function getAllUsers(): Promise<CommonResult<UserView[]>> {
  const response = await axiosInstance.get<CommonResult<UserView[]>>('/user/all')
  return response.data
}

/**
 * 根据ID查询用户
 */
export async function getUserById(id: number): Promise<CommonResult<UserView>> {
  const response = await axiosInstance.get<CommonResult<UserView>>(`/user/${id}`)
  return response.data
}

/**
 * 创建用户
 */
export async function createUser(user: UserInput): Promise<CommonResult<void>> {
  const response = await axiosInstance.post<CommonResult<void>>('/user', user)
  return response.data
}

/**
 * 更新用户信息
 */
export async function updateUser(user: UserUpdateInput): Promise<CommonResult<void>> {
  const response = await axiosInstance.put<CommonResult<void>>('/user', user)
  return response.data
}

/**
 * 删除用户
 */
export async function deleteUser(id: number): Promise<CommonResult<void>> {
  const response = await axiosInstance.delete<CommonResult<void>>(`/user/${id}`)
  return response.data
}

/**
 * 批量删除用户
 */
export async function batchDeleteUsers(ids: number[]): Promise<CommonResult<void>> {
  const response = await axiosInstance.delete<CommonResult<void>>('/user/batch', {
    data: ids,
  })
  return response.data
}

/**
 * 启用/禁用用户
 */
export async function toggleUserStatus(id: number, enabled: boolean): Promise<CommonResult<void>> {
  const response = await axiosInstance.patch<CommonResult<void>>(
    `/user/${id}/status`,
    null,
    { params: { enabled } }
  )
  return response.data
}

/**
 * 用户注册
 */
export async function register(user: UserInput): Promise<CommonResult<void>> {
  const response = await axiosInstance.post<CommonResult<void>>('/user/register', user)
  return response.data
}

