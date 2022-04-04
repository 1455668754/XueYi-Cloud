<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="deptName"
            placeholder="请输入部门名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="treeRef"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--岗位数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px"
        >
          <el-form-item label="岗位编码" prop="code">
            <el-input
              v-model="queryParams.code"
              placeholder="请输入"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="岗位名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入"
              clearable
              @keyup.enter.native="handleQuery"
            />
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
              v-hasPermi="[PostAuth.ADD]"
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
              v-hasPermi="[PostAuth.EDIT]"
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
              v-hasPermi="[PostAuth.DELETE]"
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
          <el-table-column label="岗位名称" align="center" prop="name" v-if="columns[2].visible" :show-overflow-tooltip="true" min-width="100" />
          <el-table-column label="岗位编码" align="center" prop="code" v-if="columns[3].visible" :show-overflow-tooltip="true" min-width="100" />
          <el-table-column label="部门Id" align="center" prop="deptId" v-if="columns[4].visible" :show-overflow-tooltip="true" min-width="100" />
          <el-table-column label="状态" align="center" prop="status" v-if="columns[5].visible" :show-overflow-tooltip="true" min-width="100">
            <template v-slot="scope">
              <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" v-if="columns[6].visible" class-name="small-padding fixed-width" width="220" fixed="right">
            <template v-slot="scope">
              <el-button
                size="mini"
                type="text"
                :icon="IconEnum.EDIT"
                @click="handleUpdate(scope.row)"
                v-hasPermi="[PostAuth.EDIT]"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                :icon="IconEnum.AUTH"
                @click="handleAuth(scope.row)"
                v-hasPermi="[PostAuth.AUTH]"
              >分配角色
              </el-button>
              <el-button
                size="mini"
                type="text"
                :icon="IconEnum.DELETE"
                @click="handleDelete(scope.row)"
                v-hasPermi="[PostAuth.DELETE]"
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
      </el-col>
    </el-row>

    <!-- 添加或修改岗位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" :before-close="handleClose" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="归属部门" prop="deptId">
              <treeselect v-model="form.deptId" :options="deptOptions" :normalizer="normalizer" placeholder="请选择归属部门" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="岗位编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入岗位编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="岗位名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入岗位名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="sort">
              <el-input-number v-model="form.sort" :max="65535" />
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

    <PostRoleModal ref="authRef" :roleOptions="roleOptions" />
  </div>
</template>

<script>
import { listPostApi, getPostApi, addPostApi, editPostApi, delPostApi } from '@/api/system/organize/post'
import { PostAuth } from '@auth/system'
import { DicSortEnum, DicStatusEnum, IconEnum } from '@enums'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { optionDeptApi } from '@/api/system/organize/dept'
import { optionRoleApi } from '@/api/system/authority/role'
import PostRoleModal from './PostRoleModal'

export default {
  name: 'PostManagement',
  /** 字典查询 */
  dicts: ['sys_normal_disable'],
  components: { PostRoleModal, Treeselect },
  data() {
    return {
      //权限标识
      PostAuth: PostAuth,
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
      // 表格数据
      tableList: [],
      // 部门树选项
      deptOptions: [],
      // 角色选项
      roleOptions: [],
      // 部门名称
      deptName: undefined,
      // 总条数
      total: 0,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 提交状态
      submitLoading: false,
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        deptId: undefined,
        code: undefined,
        name: undefined,
        status: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `勾选列`, visible: true },
        { key: 1, label: `序号列`, visible: true },
        { key: 2, label: `岗位名称`, visible: true },
        { key: 3, label: `岗位编码`, visible: true },
        { key: 4, label: `部门Id`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `操作列`, visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deptId: [
          { required: true, message: '归属部门不能为空', trigger: 'change' }
        ],
        name: [
          { required: true, message: '岗位名称不能为空', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '岗位编码不能为空', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '显示顺序不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.treeRef.filter(val)
    }
  },
  created() {
    this.getList()
    this.getOptions()
  },
  methods: {
    /** 查询岗位列表 */
    getList() {
      this.loading = true
      listPostApi(this.queryParams).then(response => {
        this.tableList = response.data.items
        this.total = response.data.total
        this.loading = false
      })
    },
    /** 查询选项列表 */
    getOptions() {
      this.deptOptions = []
      this.roleOptions = []
      optionDeptApi().then(response => {
        this.deptOptions = response.data
      })
      optionRoleApi().then(response => {
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
        label: node.name,
        isDisabled: false,
        children: node.children
      }
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
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        deptId: undefined,
        code: undefined,
        name: undefined,
        sort: DicSortEnum.ZERO,
        status: DicStatusEnum.NORMAL,
        remark: undefined
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
      this.deptName = undefined
      this.queryParams.deptId = undefined
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
    /** 筛选节点 */
    filterNode(value, data) {
      if (!value) return true
      return data.name.indexOf(value) !== -1
    },
    /** 节点单击事件 */
    handleNodeClick(data) {
      this.queryParams.deptId = data.id
      this.handleQuery()
    },
    /** 新增操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加岗位'
    },
    /** 修改操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getPostApi(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改岗位'
      })
    },
    /** 角色分配操作 */
    handleAuth(row) {
      this.$refs.authRef.handleAuth(row)
    },
    /** 提交操作 */
    submitForm: function() {
      this.submitLoading = false
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            editPostApi(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).catch()
          } else {
            addPostApi(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }).catch()
          }
        }
      })
      this.submitLoading = false
    },
    /** 删除操作 */
    handleDelete(row) {
      const delIds = row.id || this.ids
      const delNames = row.name || this.idNames
      this.$modal.confirm('是否确定要删除' + delNames + '？').then(function() {
        return delPostApi(delIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功！')
      }).catch(() => {
      })
    }
  }
}
</script>
