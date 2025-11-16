<template>
  <div class="user-management pa-4 pa-sm-2">
    <v-card>
      <v-card-title>
        <div class="d-flex justify-space-between align-center">
          <span>用户管理</span>
          <v-btn color="primary" @click="openCreateDialog">
            <v-icon start>mdi-plus</v-icon>
            新增用户
          </v-btn>
        </div>
      </v-card-title>

      <v-card-text>
        <!-- 搜索过滤 -->
        <div class="mb-4">
          <v-row dense>
            <v-col cols="12" sm="6" md="3">
              <v-text-field
                v-model="searchSpec.userName"
                label="用户名"
                prepend-inner-icon="mdi-account-outline"
                clearable
                density="compact"
                variant="outlined"
                hide-details
                placeholder="请输入用户名"
              />
            </v-col>
            <v-col cols="12" sm="6" md="3">
              <v-text-field
                v-model="searchSpec.nickName"
                label="昵称"
                prepend-inner-icon="mdi-text-account"
                clearable
                density="compact"
                variant="outlined"
                hide-details
                placeholder="请输入昵称"
              />
            </v-col>
            <v-col cols="12" sm="6" md="3">
              <v-text-field
                v-model="searchSpec.email"
                label="邮箱"
                prepend-inner-icon="mdi-email-outline"
                clearable
                density="compact"
                variant="outlined"
                hide-details
                placeholder="请输入邮箱"
              />
            </v-col>
            <v-col cols="12" sm="6" md="3">
              <v-select
                v-model="searchSpec.enabled"
                label="状态"
                prepend-inner-icon="mdi-toggle-switch-outline"
                :items="statusOptions"
                clearable
                density="compact"
                variant="outlined"
                hide-details
                placeholder="请选择状态"
              />
            </v-col>
          </v-row>

          <v-row dense class="mt-3">
            <v-col cols="12" class="d-flex justify-end align-center">
              <div class="d-flex align-center" style="gap: 12px;">
                <v-btn
                  v-if="selectedUsers.length > 0"
                  color="error"
                  variant="elevated"
                  @click="batchDelete"
                  style="margin-right: auto;"
                >
                  <v-icon start>mdi-delete</v-icon>
                  批量删除 ({{ selectedUsers.length }})
                </v-btn>
                <v-btn
                  variant="outlined"
                  @click="resetSearch"
                >
                  <v-icon start>mdi-refresh</v-icon>
                  重置
                </v-btn>
                <v-btn
                  color="primary"
                  variant="elevated"
                  @click="loadUsers"
                >
                  <v-icon start>mdi-magnify</v-icon>
                  查询
                </v-btn>
              </div>
            </v-col>
          </v-row>
        </div>

        <!-- 用户列表表格 -->
        <v-data-table
          v-model="selectedUsers"
          :headers="headers"
          :items="users"
          :loading="loading"
          :items-per-page="pageSize"
          show-select
          item-value="id"
          return-object
          class="mt-4 fixed-action-table"
          fixed-header
          no-data-text="暂无数据"
          loading-text="加载中..."
          items-per-page-text="每页行数："
          :items-per-page-options="[
            { value: 10, title: '10' },
            { value: 20, title: '20' },
            { value: 50, title: '50' },
            { value: 100, title: '100' },
          ]"
        >
          <template #item.enabled="{ item }">
            <v-chip
              :color="item.enabled ? 'success' : 'error'"
              size="small"
            >
              {{ item.enabled ? '启用' : '禁用' }}
            </v-chip>
          </template>

          <template #item.createTime="{ item }">
            {{ formatDateTime(item.createTime) }}
          </template>

          <template #item.actions="{ item }">
            <div class="action-buttons">
              <v-btn
                icon="mdi-pencil"
                size="small"
                variant="text"
                @click="openEditDialog(item)"
              />
              <v-btn
                :icon="item.enabled ? 'mdi-cancel' : 'mdi-check'"
                size="small"
                variant="text"
                :color="item.enabled ? 'warning' : 'success'"
                @click="toggleStatus(item)"
              />
              <v-btn
                icon="mdi-delete"
                size="small"
                variant="text"
                color="error"
                @click="deleteUser(item)"
              />
            </div>
          </template>

          <template #bottom>
            <div class="d-flex align-center justify-space-between pa-2 border-t flex-wrap" style="gap: 16px;">
              <div class="d-flex align-center flex-wrap" style="gap: 16px;">
                <div class="d-flex align-center" style="gap: 8px;">
                  <span class="text-body-2">每页行数：</span>
                  <v-select
                    :model-value="pageSize"
                    :items="[10, 20, 50, 100]"
                    density="compact"
                    variant="outlined"
                    hide-details
                    style="width: 90px;"
                    @update:model-value="pageSize = $event; loadUsers()"
                  />
                </div>
                <span class="text-body-2 text-medium-emphasis">
                  {{ Math.min((currentPage - 1) * pageSize + 1, totalElements) }}-{{ Math.min(currentPage * pageSize, totalElements) }} 共 {{ totalElements }} 条
                </span>
              </div>
              <v-pagination
                v-model="currentPage"
                :length="totalPages"
                :total-visible="5"
                size="small"
                @update:model-value="loadUsers"
              />
            </div>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>

    <!-- 新增/编辑用户对话框 -->
    <v-dialog v-model="dialog" max-width="600">
      <v-card>
        <v-card-title>
          {{ isEdit ? '编辑用户' : '新增用户' }}
        </v-card-title>
        <v-card-text>
          <v-form ref="formRef" v-model="formValid">
            <v-text-field
              v-if="!isEdit"
              v-model="formData.userName"
              label="用户名"
              density="compact"
              variant="outlined"
              :rules="[rules.required]"
              required
            />
            <v-text-field
              v-model="formData.password"
              :label="isEdit ? '新密码（留空则不修改）' : '密码'"
              density="compact"
              variant="outlined"
              type="password"
              :rules="isEdit ? [rules.optionalMinLength] : [rules.required, rules.minLength]"
              :required="!isEdit"
            />
            <v-text-field
              v-model="formData.nickName"
              label="昵称"
              density="compact"
              variant="outlined"
            />
            <v-text-field
              v-model="formData.email"
              label="邮箱"
              density="compact"
              variant="outlined"
              :rules="[rules.email]"
            />
            <v-switch
              v-model="formData.enabled"
              label="启用状态"
              density="compact"
              variant="outlined"
              color="success"
              :true-value="true"
              :false-value="false"
              hide-details
            />
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn @click="dialog = false">取消</v-btn>
          <v-btn color="primary" :loading="saving" @click="saveUser">
            保存
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 删除确认对话框 -->
    <v-dialog v-model="deleteDialog" max-width="400">
      <v-card>
        <v-card-title>确认删除</v-card-title>
        <v-card-text>
          确定要删除选中的用户吗？此操作不可恢复。
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn @click="deleteDialog = false">取消</v-btn>
          <v-btn color="error" :loading="deleting" @click="confirmDelete">
            删除
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { useSnackbar } from '@/stores/snackbar'
import {
  getUserList,
  createUser,
  updateUser,
  deleteUser as deleteUserApi,
  batchDeleteUsers,
  toggleUserStatus,
} from '@/api/user'
import type { UserView, UserInput, UserUpdateInput, UserSpec } from '@/types/user'

const snackbar = useSnackbar()


// 数据
const users = ref<UserView[]>([])
const selectedUsers = ref<UserView[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const totalPages = ref(0)
const totalElements = ref(0)

// 搜索条件
const searchSpec = reactive<UserSpec>({
  userName: undefined,
  nickName: undefined,
  email: undefined,
  enabled: undefined,
})

const statusOptions = [
  { title: '启用', value: true },
  { title: '禁用', value: false },
]

// 对话框
const dialog = ref(false)
const deleteDialog = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const deleting = ref(false)
const formValid = ref(false)
const formRef = ref()

// 表单数据
const formData = reactive<UserInput & { id?: number }>({
  userName: '',
  password: '',
  nickName: '',
  email: '',
  enabled: true,
})

// 待删除的用户
const userToDelete = ref<UserView | null>(null)

// 表头配置，设置最小宽度防止换行
const headers = [
  { title: 'ID', key: 'id', sortable: true, minWidth: '60px' },
  { title: '用户名', key: 'userName', sortable: true, minWidth: '100px' },
  { title: '昵称', key: 'nickName', sortable: true, minWidth: '100px' },
  { title: '邮箱', key: 'email', sortable: true, minWidth: '150px' },
  { title: '状态', key: 'enabled', sortable: true, minWidth: '120px' },
  { title: '创建时间', key: 'createTime', sortable: true, minWidth: '160px' },
  { title: '操作', key: 'actions', sortable: false, minWidth: '130px' },
]

// 表单验证规则
const rules = {
  required: (v: string) => !!v || '此字段为必填项',
  minLength: (v: string) => (v && v.length >= 6) || '密码至少6位',
  optionalMinLength: (v: string) => !v || v.length >= 6 || '密码至少6位',
  email: (v: string) => !v || /.+@.+\..+/.test(v) || '邮箱格式不正确',
}

// 加载用户列表
async function loadUsers() {
  loading.value = true
  try {
    const result = await getUserList(
      searchSpec,
      currentPage.value - 1,
      pageSize.value
    )
    if (result.code === 10000) {
      users.value = result.data.content
      totalElements.value = result.data.totalElements
      totalPages.value = result.data.totalPages
    }
  } catch (error) {
    snackbar.showError('加载用户列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 重置搜索
function resetSearch() {
  searchSpec.userName = undefined
  searchSpec.nickName = undefined
  searchSpec.email = undefined
  searchSpec.enabled = undefined
  currentPage.value = 1
  loadUsers()
}

// 打开新增对话框
function openCreateDialog() {
  isEdit.value = false
  formData.id = undefined
  formData.userName = ''
  formData.password = ''
  formData.nickName = ''
  formData.email = ''
  formData.enabled = true
  dialog.value = true
}

// 打开编辑对话框
function openEditDialog(user: UserView) {
  isEdit.value = true
  formData.id = user.id
  formData.userName = user.userName
  formData.password = '' // 编辑时清空密码，留空表示不修改
  formData.nickName = user.nickName || ''
  formData.email = user.email || ''
  formData.enabled = user.enabled
  dialog.value = true
}

// 保存用户
async function saveUser() {
  if (!formRef.value) return

  const { valid } = await formRef.value.validate()
  if (!valid) return

  saving.value = true
  try {
    let result
    if (isEdit.value) {
      const updateData: UserUpdateInput = {
        id: formData.id!,
        nickName: formData.nickName,
        email: formData.email,
        enabled: formData.enabled,
      }
      // 如果填写了新密码，则包含密码字段
      if (formData.password && formData.password.trim()) {
        updateData.password = formData.password
      }
      result = await updateUser(updateData)
    } else {
      const createData: UserInput = {
        userName: formData.userName,
        password: formData.password,
        nickName: formData.nickName,
        email: formData.email,
        enabled: formData.enabled,
      }
      result = await createUser(createData)
    }

    if (result.code === 10000) {
      snackbar.showSuccess(isEdit.value ? '更新成功' : '创建成功')
      dialog.value = false
      loadUsers()
    } else {
      snackbar.showError(result.message || '操作失败')
    }
  } catch (error) {
    snackbar.showError('操作失败')
    console.error(error)
  } finally {
    saving.value = false
  }
}

// 删除用户
function deleteUser(user: UserView) {
  userToDelete.value = user
  deleteDialog.value = true
}

// 确认删除
async function confirmDelete() {
  if (!userToDelete.value) return

  deleting.value = true
  try {
    const result = await deleteUserApi(userToDelete.value.id)
    if (result.code === 10000) {
      snackbar.showSuccess('删除成功')
      deleteDialog.value = false
      loadUsers()
    } else {
      snackbar.showError(result.message || '删除失败')
    }
  } catch (error) {
    snackbar.showError('删除失败')
    console.error(error)
  } finally {
    deleting.value = false
  }
}

// 批量删除
async function batchDelete() {
  if (selectedUsers.value.length === 0) {
    snackbar.showWarning('请选择要删除的用户')
    return
  }

  if (!confirm(`确定要删除选中的 ${selectedUsers.value.length} 个用户吗？`)) {
    return
  }

  try {
    const ids = selectedUsers.value.map(u => u.id)
    console.log(selectedUsers.value)
    const result = await batchDeleteUsers(ids)
    if (result.code === 10000) {
      snackbar.showSuccess('批量删除成功')
      selectedUsers.value = []
      await loadUsers()
    } else {
      snackbar.showError(result.message || '批量删除失败')
    }
  } catch (error) {
    snackbar.showError('批量删除失败')
    console.error(error)
  }
}

// 切换用户状态
async function toggleStatus(user: UserView) {
  try {
    const result = await toggleUserStatus(user.id, !user.enabled)
    if (result.code === 10000) {
      snackbar.showSuccess(`已${user.enabled ? '禁用' : '启用'}用户`)
      await loadUsers()
    } else {
      snackbar.showError(result.message || '操作失败')
    }
  } catch (error) {
    snackbar.showError('操作失败')
    console.error(error)
  }
}

// 格式化日期时间
function formatDateTime(dateTime?: string) {
  if (!dateTime) return '-'
  try {
    const date = new Date(dateTime)
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
    })
  } catch (error) {
    return dateTime
  }
}

// 初始化
onMounted(() => {
  loadUsers()
})
</script>

<style scoped>

/* 表头和内容不换行 */
:deep(.v-data-table-header th) {
  white-space: nowrap !important;
}

:deep(.v-data-table tbody td) {
  white-space: nowrap !important;
}

/* 为每个列设置最小宽度 */
:deep(.v-data-table th),
:deep(.v-data-table td) {
  min-width: var(--column-min-width, auto);
}

/* 移动端固定操作列 */
@media (max-width: 960px) {

  /* 表格容器样式 */
  :deep(.fixed-action-table) {
    position: relative;
  }

  /* 表格外层包裹，允许横向滚动 */
  :deep(.v-table__wrapper) {
    overflow-x: auto;
    -webkit-overflow-scrolling: touch;
  }

  /* 表格主体 */
  :deep(.v-data-table table) {
    width: max-content;
    min-width: 100%;
  }

  /* 固定最后一列（操作列） */
  :deep(.v-data-table th:last-child),
  :deep(.v-data-table td:last-child) {
    position: sticky !important;
    right: 0 !important;
    background-color: rgb(var(--v-theme-surface)) !important;
    z-index: 2 !important;
    box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
  }

  /* 表头固定列需要更高的 z-index */
  :deep(.v-data-table-header th:last-child) {
    z-index: 3 !important;
  }

  /* 操作按钮容器 */
  .action-buttons {
    display: flex;
    gap: 2px;
    justify-content: center;
    min-width: 120px;
  }

  /* 操作按钮样式 */
  .action-buttons .v-btn {
    min-width: 32px;
    width: 32px;
    height: 32px;
  }
}
</style>

