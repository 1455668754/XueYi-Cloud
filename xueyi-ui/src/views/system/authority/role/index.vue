<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="角色编码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="角色名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据范围" prop="dataScope">
        <el-select v-model="queryParams.dataScope" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.auth_data_scope"
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
      <el-form-item>
        <el-button type="primary" :icon="IconEnum.SEARCH" size="mini" @click="handleQuery">搜索</el-button>
        <el-button :icon="IconEnum.RESET" size="mini" @click="resetQuery">重置</el-button>
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
          v-hasPermi="[RoleAuth.ADD]"
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
          v-hasPermi="[RoleAuth.EDIT]"
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
          v-hasPermi="[RoleAuth.DELETE]"
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
      <el-table-column label="角色编码" align="center" prop="code" v-if="columns[2].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="角色名称" align="center" prop="name" v-if="columns[3].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="数据范围" align="center" prop="dataScope" v-if="columns[4].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.auth_data_scope" :value="scope.row.dataScope" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" v-if="columns[5].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" v-if="columns[6].visible" class-name="small-padding fixed-width" width="300" fixed="right">
        <template v-slot="scope">
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.EDIT"
            @click="handleUpdate(scope.row)"
            v-hasPermi="[RoleAuth.EDIT]"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.AUTH"
            @click="handleAuth(scope.row)"
            v-hasPermi="[RoleAuth.AUTH]"
          >功能权限
          </el-button>
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.AUTH"
            @click="handleData(scope.row)"
            v-hasPermi="[RoleAuth.AUTH]"
          >数据权限
          </el-button>
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.DELETE"
            @click="handleDelete(scope.row)"
            v-hasPermi="[RoleAuth.DELETE]"
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

    <!-- 添加或修改角色对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" :before-close="handleClose" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="角色编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入角色编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入角色名称" />
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

    <!-- 添加或修改角色对话框 -->
    <el-dialog :title="title" :visible.sync="openAuth" width="780px" :before-close="handleClose" append-to-body>
      <el-form ref="authForm" :model="form" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="角色名称" prop="name">
              <el-input v-model="form.name" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单分配" prop="authIds">
              <RoleAuthModal ref="authRef" :menuOptions="menuOptions" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitAuthForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改角色对话框 -->
    <el-dialog :title="title" :visible.sync="openData" width="780px" :before-close="handleClose" append-to-body>
      <el-form ref="dataForm" :model="form" :rules="dataRules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="角色名称" prop="name">
              <el-input v-model="form.name" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限字符串" prop="roleKey">
              <el-input v-model="form.roleKey" placeholder="请输入权限字符串" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据范围" prop="dataScope">
              <el-select v-model="form.dataScope" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.auth_data_scope"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-show="form.dataScope === DataScopeEnum.CUSTOM">
            <el-form-item label="数据权限" prop="organizeIds">
              <RoleDataModal ref="dataRef" :dataOptions="dataOptions" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitDataForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <RoleInitModal ref="initRef" :menuOptions="menuOptions" :dataOptions="dataOptions" @getList="getList" />
  </div>
</template>

<script>
import {
  listRoleApi,
  getRoleApi,
  editRoleApi,
  delRoleApi, editAuthScopeApi, editDataScopeApi
} from '@/api/system/authority/role'
import { RoleAuth } from '@auth/system'
import { DicSortEnum, DicStatusEnum, IconEnum } from '@enums'
import { organizeScopeApi } from '@/api/system/organize/organize'
import { authScopeEnterpriseApi } from '@/api/system/authority/auth'
import RoleInitModal from './RoleInitModal'
import RoleAuthModal from './RoleAuthModal'
import { DataScopeEnum } from '@enums/system'
import RoleDataModal from './RoleDataModal'

export default {
  name: 'RoleManagement',
  components: { RoleDataModal, RoleAuthModal, RoleInitModal },
  /** 字典查询 */
  dicts: ['sys_normal_disable', 'auth_data_scope'],
  data() {
    return {
      //权限标识
      RoleAuth: RoleAuth,
      // 图标标识
      IconEnum: IconEnum,
      // 数据范围
      DataScopeEnum: DataScopeEnum,
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
      // 菜单权限树
      menuOptions: [],
      // 数据权限树
      dataOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      openAuth: false,
      openData: false,
      // 提交状态
      submitLoading: false,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        code: undefined,
        name: undefined,
        dataScope: undefined,
        status: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `勾选列`, visible: true },
        { key: 1, label: `序号列`, visible: true },
        { key: 2, label: `角色编码`, visible: true },
        { key: 3, label: `角色名称`, visible: true },
        { key: 4, label: `数据范围`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `操作列`, visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '角色编码不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      },
      dataRules: {
        roleKey: [
          { required: true, message: '权限字符串不能为空', trigger: 'blur' }
        ],
        dataScope: [
          { required: true, message: '数据范围不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getOptions()
  },
  methods: {
    /** 查询角色列表 */
    getList() {
      this.loading = true
      listRoleApi(this.queryParams).then(response => {
        this.tableList = response.data.items
        this.total = response.data.total
        this.loading = false
      })
    },
    /** 查询选项列表 */
    getOptions() {
      this.menuOptions = []
      this.dataOptions = []
      authScopeEnterpriseApi().then(response => {
        this.menuOptions = response.data
      })
      organizeScopeApi().then(response => {
        this.dataOptions = response.data
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
      this.openData = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        code: undefined,
        name: undefined,
        roleKey: undefined,
        dataScope: undefined,
        sort: DicSortEnum.ZERO,
        status: DicStatusEnum.NORMAL,
        remark: undefined,
        authIds: [],
        organizeIds: []
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
      this.$refs.initRef.handleAdd()
    },
    /** 修改操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getRoleApi(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改角色'
      })
    },
    /** 修改菜单权限操作 */
    handleAuth(row) {
      this.reset()
      this.form.id = row.id
      this.form.name = row.name
      this.openAuth = true
      this.$nextTick(() => {
        this.$refs.authRef.handleTreeNodeAll()
        this.$refs.authRef.getAuthScope(row.id)
      })
    },
    /** 修改数据权限操作 */
    handleData(row) {
      this.reset()
      this.form.id = row.id
      this.form.name = row.name
      this.form.roleKey = row.roleKey
      this.form.dataScope = row.dataScope
      this.openData = true
      this.$nextTick(() => {
        this.$refs.dataRef.handleTreeNodeAll()
        this.$refs.dataRef.getDataScope(row.id)
      })
    },
    /** 提交操作 */
    submitForm: function() {
      this.submitLoading = false
      this.$refs['form'].validate(valid => {
        if (valid && this.form.id !== undefined) {
          editRoleApi(this.form).then(response => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
          }).catch()
        }
      })
      this.submitLoading = false
    },
    /** 提交操作 */
    submitAuthForm: function() {
      this.submitLoading = false
      this.form.authIds = this.$refs.authRef.getAllCheckedKeys()
      this.$refs['authForm'].validate(valid => {
        if (valid && this.form.id !== undefined) {
          editAuthScopeApi(this.form.id, this.form.authIds).then(response => {
            this.$modal.msgSuccess('修改成功')
            this.openAuth = false
            this.getList()
          }).catch()
        }
      })
      this.submitLoading = false
    },
    /** 提交操作 */
    submitDataForm: function() {
      this.submitLoading = false
      if (this.form.dataScope === DataScopeEnum.CUSTOM) {
        this.form.organizeIds = this.$refs.dataRef.getAllCheckedKeys()
      }
      this.$refs['dataForm'].validate(valid => {
        if (valid && this.form.id !== undefined) {
          editDataScopeApi(this.form).then(response => {
            this.$modal.msgSuccess('新增成功')
            this.openData = false
            this.getList()
          }).catch()
        }
      })
      this.submitLoading = false
    },

    /** 删除操作 */
    handleDelete(row) {
      const delIds = row.id || this.ids
      const delNames = row.name || this.idNames
      this.$modal.confirm('是否确定要删除' + delNames + '？').then(function() {
        return delRoleApi(delIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功！')
      }).catch(() => {
      })
    }
  }
}
</script>
