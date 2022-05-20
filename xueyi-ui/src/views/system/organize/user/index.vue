<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--组织数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="organizeName"
            placeholder="请输入组织名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="organizeOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="treeRef"
            highlight-current
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>

      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item label="用户编码" prop="code">
            <el-input
              v-model="queryParams.code"
              placeholder="请输入"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="用户账号" prop="userName">
            <el-input
              v-model="queryParams.userName"
              placeholder="请输入"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="用户昵称" prop="nickName">
            <el-input
              v-model="queryParams.nickName"
              placeholder="请输入"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phone">
            <el-input
              v-model="queryParams.phone"
              placeholder="请输入"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="用户性别" prop="sex">
            <el-select v-model="queryParams.sex" placeholder="请选择" clearable>
              <el-option
                v-for="dict in dict.type.sys_user_sex"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="请选择"
              clearable
            >
              <el-option
                v-for="dict in dict.type.sys_normal_disable"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              :icon="IconEnum.SEARCH"
              size="mini"
              @click="handleQuery"
            >搜索
            </el-button
            >
            <el-button :icon="IconEnum.RESET" size="mini" @click="resetQuery"
            >重置
            </el-button
            >
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              :icon="IconEnum.ADD"
              size="mini"
              @click="handleAdd"
              v-hasPermi="[UserAuth.ADD]"
            >新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              :icon="IconEnum.EDIT"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="[UserAuth.EDIT]"
            >修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              :icon="IconEnum.DELETE"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="[UserAuth.DELETE]"
            >删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              :icon="IconEnum.EXPORT"
              size="mini"
              @click="handleExport"
              v-hasPermi="[UserAuth.EXPORT]"
            >导出
            </el-button>
          </el-col>
          <right-toolbar
            :showSearch.sync="showSearch"
            @queryTable="getList"
            :columns="columns"
          />
        </el-row>

        <el-table
          v-loading="loading"
          :data="tableList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column
            type="selection"
            align="center"
            v-if="columns[0].visible"
            min-width="55"
          />
          <el-table-column
            label="序号"
            align="center"
            v-if="columns[1].visible"
            min-width="80"
          >
            <template v-slot="scope">
              <span>{{
                  queryParams.pageSize * (queryParams.page - 1) + scope.$index + 1
                }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="用户昵称"
            align="center"
            prop="nickName"
            v-if="columns[2].visible"
            :show-overflow-tooltip="true"
            min-width="100"
          />
          <el-table-column
            label="用户账号"
            align="center"
            prop="userName"
            v-if="columns[3].visible"
            :show-overflow-tooltip="true"
            min-width="100"
          />
          <el-table-column
            label="用户编码"
            align="center"
            prop="code"
            v-if="columns[4].visible"
            :show-overflow-tooltip="true"
            min-width="100"
          />
          <el-table-column
            label="用户性别"
            align="center"
            prop="sex"
            v-if="columns[5].visible"
            :show-overflow-tooltip="true"
            min-width="100"
          >
            <template v-slot="scope">
              <dict-tag
                :options="dict.type.sys_user_sex"
                :value="scope.row.sex"
              />
            </template>
          </el-table-column>
          <el-table-column
            label="状态"
            align="center"
            prop="status"
            v-if="columns[6].visible"
            :show-overflow-tooltip="true"
            min-width="100"
          >
            <template v-slot="scope">
              <dict-tag
                :options="dict.type.sys_normal_disable"
                :value="scope.row.status"
              />
            </template>
          </el-table-column>
          <el-table-column
            label="最后登录时间"
            align="center"
            prop="loginDate"
            v-if="columns[7].visible"
            :show-overflow-tooltip="true"
            min-width="100"
          >
            <template v-slot="scope">
              <span>{{
                  parseTime(scope.row.loginDate, '{y}-{m}-{d} {h}:{i}:{s}')
                }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            v-if="columns[8].visible"
            class-name="small-padding fixed-width"
            width="220"
            fixed="right"
          >
            <template v-slot="scope">
              <el-button
                size="mini"
                type="text"
                :icon="IconEnum.EDIT"
                @click="handleUpdate(scope.row)"
                v-hasPermi="[UserAuth.EDIT]"
              >修改
              </el-button>

              <!-- <el-button
                size="mini"
                type="text"
                :icon="IconEnum.AUTH"
                @click="handleAuth(scope.row)"
                v-hasPermi="[UserAuth.AUTH]"
                >分配角色
              </el-button> -->

              <el-button
                size="mini"
                type="text"
                :icon="IconEnum.DELETE"
                @click="handleDelete(scope.row)"
                v-hasPermi="[UserAuth.DELETE]"
              >删除
              </el-button>

              <el-dropdown
                size="mini"
                @command="(command) => handleCommand(command, scope.row)"
                v-hasPermi="[UserAuth.AUTH, UserAuth.EDIT_PASSWORD]"
              >
                <span class="el-dropdown-link">
                  <i class="el-icon-d-arrow-right el-icon--right"></i>更多
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item
                    command="handleResetPwd"
                    :icon="IconEnum.EDIT_PASSWORD"
                    v-hasPermi="[UserAuth.EDIT_PASSWORD]"
                  >重置密码
                  </el-dropdown-item
                  >
                  <el-dropdown-item
                    command="handleAuth"
                    :icon="IconEnum.AUTH"
                    v-hasPermi="[UserAuth.AUTH]"
                  >分配角色
                  </el-dropdown-item
                  >
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.page"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- 添加或修改用户对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="780px"
      :before-close="handleClose"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="归属岗位" prop="postIds">
              <treeselect
                v-model="form.postIds"
                :options="organizeOptions"
                :multiple="true"
                :disableBranchNodes="true"
                :normalizer="normalizer"
                placeholder="请选择归属岗位"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入用户昵称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入用户编码"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户账号" prop="userName">
              <el-input
                v-model="form.userName"
                placeholder="请输入用户账号"
                :disabled="form.id !== undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.id === undefined">
            <el-form-item label="密码" prop="password">
              <el-input
                v-model="form.password"
                placeholder="请输入密码"
                show-password
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号码"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入用户邮箱"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户性别" prop="sex">
              <el-select v-model="form.sex" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.sys_user_sex"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio-button
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                  :value="dict.value"
                >{{ dict.label }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="sort">
              <el-input-number v-model="form.sort" :max="65535"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="form.remark"
                placeholder="请输入备注"
                type="textarea"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm"
        >确 定
        </el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <UserRoleModal ref="authRef" :roleOptions="roleOptions"/>
  </div>
</template>

<script>
import {
  listUserApi,
  getUserApi,
  addUserApi,
  editUserApi,
  resetUserPwdApi,
  delUserApi
} from '@/api/system/organize/user'
import { UserAuth } from '@auth/system'
import { DicSortEnum, DicStatusEnum, IconEnum } from '@enums'
import { organizeOptionApi } from '@/api/system/organize/organize'
import { OrganizeTypeEnum } from '@enums/system'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { optionRoleApi } from '@/api/system/authority/role'
import UserRoleModal from './UserRoleModal'

export default {
  name: 'UserManagement',
  /** 字典查询 */
  dicts: ['sys_normal_disable', 'sys_user_sex'],
  components: { UserRoleModal, Treeselect },
  data() {
    return {
      //权限标识
      UserAuth: UserAuth,
      // 图标标识
      IconEnum: IconEnum,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中数组名称
      idNames: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      tableList: [],
      // 组织树选项
      organizeOptions: [],
      // 角色选项
      roleOptions: [],
      // 组织名称
      organizeName: undefined,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 提交状态
      submitLoading: false,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        postId: undefined,
        deptId: undefined,
        code: undefined,
        userName: undefined,
        nickName: undefined,
        phone: undefined,
        sex: undefined,
        status: undefined
      },
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 列信息
      columns: [
        { key: 0, label: `勾选列`, visible: true },
        { key: 1, label: `序号列`, visible: true },
        { key: 2, label: `用户昵称`, visible: true },
        { key: 3, label: `用户编码`, visible: true },
        { key: 4, label: `用户账号`, visible: true },
        { key: 5, label: `用户性别`, visible: true },
        { key: 6, label: `状态`, visible: true },
        { key: 7, label: `最后登录时间`, visible: true },
        { key: 8, label: `操作列`, visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          { required: true, message: '用户编码不能为空', trigger: 'blur' }
        ],
        userName: [
          { required: true, message: '用户账号不能为空', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: '用户昵称不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '用户昵称不能为空', trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '用户性别不能为空', trigger: 'change' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ],
        postIds: [
          { required: true, message: '归属岗位不能为空', trigger: 'change' }
        ],
        email: [
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change']
          }
        ],
        phone: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: '请输入正确的手机号码',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  watch: {
    // 根据名称筛选组织树
    deptName(val) {
      this.$refs.treeRef.filter(val)
    }
  },
  created() {
    this.getList()
    this.getOptions()
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true
      listUserApi(this.queryParams).then((response) => {
        this.tableList = response.data.items
        this.total = response.data.total
        this.loading = false
      })
    },
    /** 查询选项列表 */
    getOptions() {
      this.organizeOptions = []
      this.roleOptions = []
      organizeOptionApi().then((response) => {
        this.organizeOptions = response.data
      })
      optionRoleApi().then((response) => {
        this.roleOptions = response.data.items
      })
    },
    /** 转换树数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.label,
        isDisabled: false,
        children: node.children
      }
    },
    /** 模态框取消操作 */
    handleClose() {
      this.$modal
        .confirm('确认关闭？')
        .then((_) => {
          this.cancel()
        })
        .catch((_) => {
        })
    },
    /** 取消操作 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        code: undefined,
        userName: undefined,
        nickName: undefined,
        phone: undefined,
        email: undefined,
        sex: undefined,
        password: undefined,
        sort: DicSortEnum.ZERO,
        status: DicStatusEnum.NORMAL,
        remark: undefined,
        postIds: []
      }
      this.resetForm('form')
      this.submitLoading = false
    },
    /** 搜索操作 */
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    /** 重置操作 */
    resetQuery() {
      this.organizeName = undefined
      this.queryParams.postId = undefined
      this.queryParams.deptId = undefined
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
      this.idNames = selection.map((item) => item.nickName)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 筛选节点 */
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    /** 节点单击事件 */
    handleNodeClick(data) {
      if (data.type === OrganizeTypeEnum.DEPT) {
        this.queryParams.postId = undefined
        this.queryParams.deptId = data.id
      } else if (data.type === OrganizeTypeEnum.POST) {
        this.queryParams.postId = data.id
        this.queryParams.deptId = undefined
      }
      this.handleQuery()
    },
    /** 新增操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加用户'
    },
    /** 修改操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getUserApi(id).then((response) => {
        this.form = response.data
        this.open = true
        this.title = '修改用户'
      })
    },

    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case 'handleResetPwd':
          this.handleResetPwd(row)
          break
        case 'handleAuth':
          this.handleAuth(row)
          break
        default:
          break
      }
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.userName + '"的新密码', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        closeOnClickModal: false,
        inputPattern: /^.{5,20}$/,
        inputErrorMessage: '用户密码长度必须介于 5 和 20 之间'
      })
        .then(({ value }) => {
          console.log('tag', row)
          resetUserPwdApi(row.id, value).then((response) => {
            this.$modal.msgSuccess('修改成功，新密码是：' + value)
          })
        })
        .catch(() => {
        })
    },
    /** 角色分配操作 */
    handleAuth(row) {
      this.$refs.authRef.handleAuth(row)
    },
    /** 提交操作 */
    submitForm: function() {
      this.submitLoading = false
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.id !== undefined) {
            editUserApi(this.form)
              .then((response) => {
                this.$modal.msgSuccess('修改成功')
                this.open = false
                this.getList()
              })
              .catch()
          } else {
            addUserApi(this.form)
              .then((response) => {
                this.$modal.msgSuccess('新增成功')
                this.open = false
                this.getList()
              })
              .catch()
          }
        }
      })
      this.submitLoading = false
    },
    /** 删除操作 */
    handleDelete(row) {
      const delIds = row.id || this.ids
      const delNames = row.nickName || this.idNames
      this.$modal
        .confirm('是否确定要删除' + delNames + '？')
        .then(function() {
          return delUserApi(delIds)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('删除成功！')
        })
        .catch(() => {
        })
    },
    /** 导出操作 */
    handleExport() {
      this.download(
        '/system/user/export',
        {
          ...this.queryParams
        },
        `用户_${new Date().getTime()}.xlsx`
      )
      this.$modal.msgSuccess('导出成功！')
    }
  }
}
</script>
