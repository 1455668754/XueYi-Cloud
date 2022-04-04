<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="租户账号" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="租户名称" prop="nick">
        <el-input
          v-model="queryParams.nick"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="源策略" prop="strategyId">
        <el-select v-model="queryParams.strategyId" placeholder="请选择" clearable>
          <el-option
            v-for="dict in strategyOptions"
            :key="dict.id"
            :label="dict.name"
            :value="dict.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="超管租户" prop="isLessor">
        <el-select v-model="queryParams.isLessor" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="默认租户" prop="isDefault">
        <el-select v-model="queryParams.isDefault" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
          v-hasPermi="[TenantAuth.ADD]"
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
          v-hasPermi="[TenantAuth.EDIT]"
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
          v-hasPermi="[TenantAuth.DELETE]"
        >删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns" />
    </el-row>

    <el-table v-loading="loading" :data="tableList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" v-if="columns[0].visible" min-width="55" />
      <el-table-column label="序号" align="center" v-if="columns[1].visible" min-width="80">
        <template v-slot="scope">
          <span>{{ queryParams.pageSize * (queryParams.page - 1) + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="租户名称" align="center" prop="nick" v-if="columns[2].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="租户账号" align="center" prop="name" v-if="columns[3].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="租户logo" align="center" prop="logo" v-if="columns[4].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <el-image style="width: 80px; height: 80px" :src="scope.row.logo" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column label="系统名称" align="center" prop="systemName" v-if="columns[5].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="策略Id" align="center" prop="strategyId" v-if="columns[6].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="账号修改次数" align="center" prop="nameFrequency" v-if="columns[7].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="超管租户" align="center" prop="isLessor" v-if="columns[8].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isLessor" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" v-if="columns[9].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="默认租户" align="center" prop="isDefault" v-if="columns[10].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isDefault" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[11].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" v-if="columns[12].visible" class-name="small-padding fixed-width" width="220" fixed="right">
        <template v-slot="scope">
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.EDIT"
            @click="handleUpdate(scope.row)"
            v-hasPermi="[TenantAuth.EDIT]"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.AUTH"
            v-if="!isAdminTenant(scope.row)"
            @click="handleAuth(scope.row)"
            v-hasPermi="[TenantAuth.AUTH]"
          >菜单权限
          </el-button>
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.DELETE"
            @click="handleDelete(scope.row)"
            v-hasPermi="[TenantAuth.DELETE]"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.page"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 修改租户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" :before-close="handleClose" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="租户名称" prop="nick">
              <el-input v-model="form.nick" placeholder="请输入租户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租户账号" prop="name">
              <el-input v-model="form.name" placeholder="请输入租户账号" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="系统名称" prop="systemName">
              <el-input v-model="form.systemName" placeholder="请输入系统名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="源策略" prop="strategyId">
              <el-select v-model="form.strategyId" placeholder="请选择" clearable disabled>
                <el-option
                  v-for="dict in strategyOptions"
                  :key="dict.id"
                  :label="dict.name"
                  :value="dict.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="租户logo" prop="logo">
              <image-upload v-model="form.logo" :limit="1" :isShowTip="false" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="超管租户" prop="isLessor">
              <el-radio-group v-model="form.isLessor" disabled>
                <el-radio-button
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.value"
                  :value="dict.value"
                >{{ dict.label }}
                </el-radio-button>
              </el-radio-group>
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
            <el-form-item label="修改次数" prop="nameFrequency">
              <el-input-number v-model="form.nameFrequency" :max="65535" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="sort">
              <el-input-number v-model="form.sort" :max="65535" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" type="textarea" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 修改租户对话框 -->
    <el-dialog :title="title" :visible.sync="openAuth" width="780px" :before-close="handleClose" append-to-body>
      <el-form ref="authForm" :model="authForm" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="名称" prop="nick">
              <el-input v-model="authForm.nick" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账号" prop="name">
              <el-input v-model="authForm.name" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单分配" prop="authIds">
              <TenantAuthModal ref="authRef" :menuOptions="menuOptions" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitAuthForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <TenantInitModal ref="initModalRef" :strategyOptions="strategyOptions" :menuOptions="menuOptions"
                     @getList="getList"
    />
  </div>
</template>

<script>
import {
  listTenantApi,
  getTenantApi,
  editTenantApi,
  delTenantApi,
  authScopeTenantApi,
  editAuthTenantApi
} from '@/api/tenant/tenant/tenant'
import { TenantAuth } from '@auth/tenant'
import { DicSortEnum, DicStatusEnum, DicYesNoEnum, IconEnum } from '@enums'
import { optionStrategyApi } from '@/api/tenant/tenant/strategy'
import TenantInitModal from './TenantInitModal'
import TenantAuthModal from './TenantAuthModal'

export default {
  name: 'TenantManagement',
  /** 字典查询 */
  dicts: ['sys_yes_no', 'sys_normal_disable'],
  components: { TenantInitModal, TenantAuthModal },
  data() {
    return {
      //权限标识
      TenantAuth: TenantAuth,
      // 图标标识
      IconEnum: IconEnum,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中数组名称
      idNames: [],
      // 源策略选项
      strategyOptions: [],
      // 权限树选项
      menuOptions: [],
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
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      openAuth: false,
      // 提交状态
      submitLoading: false,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        name: undefined,
        nick: undefined,
        strategyId: undefined,
        isLessor: undefined,
        status: undefined,
        isDefault: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `勾选列`, visible: true },
        { key: 1, label: `序号列`, visible: true },
        { key: 2, label: `租户名称`, visible: true },
        { key: 3, label: `租户账号`, visible: true },
        { key: 4, label: `租户logo`, visible: true },
        { key: 5, label: `系统名称`, visible: true },
        { key: 6, label: `策略Id`, visible: true },
        { key: 7, label: `账号修改次数`, visible: true },
        { key: 8, label: `超管租户`, visible: true },
        { key: 9, label: `状态`, visible: true },
        { key: 10, label: `默认租户`, visible: true },
        { key: 11, label: `创建时间`, visible: true },
        { key: 12, label: `操作列`, visible: true }
      ],
      // 表单参数
      form: {},
      // 权限表单参数
      authForm: {},
      // 表单校验
      rules: {
        strategyId: [
          { required: true, message: '源策略不能为空', trigger: 'change' }
        ],
        name: [
          { required: true, message: '租户账号不能为空', trigger: 'blur' }
        ],
        systemName: [
          { required: true, message: '系统名称不能为空', trigger: 'blur' }
        ],
        nick: [
          { required: true, message: '租户名称不能为空', trigger: 'blur' }
        ],
        nameFrequency: [
          { required: true, message: '账号修改次数不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getOptions()
  },
  methods: {
    /** 查询租户列表 */
    getList() {
      this.loading = true
      listTenantApi(this.queryParams).then(response => {
        this.tableList = response.data.items
        this.total = response.data.total
        this.loading = false
      })
    },
    /** 查询选项列表 */
    getOptions() {
      this.strategyOptions = []
      this.menuOptions = []
      optionStrategyApi().then(response => {
        this.strategyOptions = response.data.items
      })
      authScopeTenantApi().then(response => {
        this.menuOptions = response.data
      })
    },
    /** 模态框取消操作 */
    handleClose() {
      this.$modal.confirm('确认关闭？').then(_ => {
        this.cancel()
      }).catch(_ => {
      })
    },
    /** 取消操作 */
    cancel() {
      this.open = false
      this.openAuth = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        strategyId: undefined,
        name: undefined,
        systemName: undefined,
        nick: undefined,
        logo: undefined,
        nameFrequency: DicSortEnum.FIVE,
        isLessor: DicYesNoEnum.NO,
        sort: DicSortEnum.ZERO,
        status: DicStatusEnum.NORMAL,
        remark: undefined,
        isDefault: undefined
      }
      this.authForm = {
        id: undefined,
        name: undefined,
        nick: undefined,
        authIds: []
      }
      this.resetForm('form')
      this.resetForm('authForm')
      this.submitLoading = false
    },
    /** 搜索操作 */
    isAdminTenant(row) {
      return row.isLessor === DicYesNoEnum.YES
    },
    /** 搜索操作 */
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    /** 重置操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.idNames = selection.map(item => item.name)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增操作 */
    handleAdd() {
      this.$refs.initModalRef.handleAdd()
    },
    /** 修改操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getTenantApi(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改租户'
      })
    },
    /** 权限修改操作 */
    handleAuth(row) {
      this.reset()
      this.authForm = row
      this.openAuth = true
      this.title = '租户权限分配'
      this.$nextTick(() => {
        this.$refs.authRef.handleTreeNodeAll()
        this.$refs.authRef.getAuthScope(row.id)
      })
    },
    /** 提交操作 */
    submitForm: function() {
      this.submitLoading = false
      this.$refs['form'].validate(valid => {
        if (valid && this.form.id !== undefined) {
          editTenantApi(this.form).then(response => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          }).catch()
        }
      })
      this.submitLoading = false
    },
    /** 权限提交操作 */
    submitAuthForm: function() {
      this.submitLoading = false
      this.authForm.authIds = this.$refs.authRef.getAllCheckedKeys()
      if (this.authForm.id !== undefined) {
        editAuthTenantApi(this.authForm).then(response => {
          this.$modal.msgSuccess('权限修改成功')
          this.openAuth = false
          this.getList()
        }).catch()
      }
      this.submitLoading = false
    },
    /** 删除操作 */
    handleDelete(row) {
      const delIds = row.id || this.ids
      const delNames = row.name || this.idNames
      this.$modal.confirm('是否确定要删除' + delNames + '？').then(function() {
        return delTenantApi(delIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功！')
      }).catch(() => {
      })
    }
  }
}
</script>
