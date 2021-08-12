<template>
  <div class="app-container">
    <div class="wrapper-container" v-show="showSearch">
      <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
        <el-form-item label="租户Id" prop="tenantId">
          <el-input
            v-model="queryParams.tenantId"
            placeholder="请输入租户Id"
            type="number"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="租户账号" prop="tenantName">
          <el-input
            v-model="queryParams.tenantName"
            placeholder="请输入租户账号"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="系统名称" prop="tenantSystemName">
          <el-input
            v-model="queryParams.tenantSystemName"
            placeholder="请输入系统名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="租户名称" prop="tenantNick">
          <el-input
            v-model="queryParams.tenantNick"
            placeholder="请输入租户名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
            <el-option
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="wrapper-container">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['tenant:tenant:add']"
          >新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['tenant:tenant:edit']"
          >修改
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            :loading="submitLoading"
            v-hasPermi="['tenant:tenant:remove']"
          >删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['tenant:tenant:export']"
          >导出
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-sort"
            size="mini"
            @click="handleSort"
            v-show="sortVisible"
            :loading="submitLoading"
            v-hasPermi="['tenant:tenant:edit']"
          >保存排序
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @controlSortable="handleSortable" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="tenantList" @selection-change="handleSelectionChange" ref="dataTable"
                row-key="tenantId">
        <el-table-column type="selection" width="55" align="center" class-name="allowDrag"/>
        <el-table-column label="租户Id" align="center" prop="tenantId" class-name="allowDrag">
          <template slot-scope="scope">
            <el-tag type="danger">{{ scope.row.tenantId }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="租户账号" align="center" prop="tenantName" class-name="allowDrag"/>
        <el-table-column label="系统名称" align="center" prop="tenantSystemName" class-name="allowDrag"/>
        <el-table-column label="租户名称" align="center" prop="tenantNick" class-name="allowDrag"/>
        <el-table-column label="数据策略" align="center" prop="strategy.name" class-name="allowDrag">
          <template slot-scope="scope">
            <el-tag type="success">{{ scope.row.strategy.name }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="租户logo" align="center" prop="tenantLogo" class-name="allowDrag">
          <template slot-scope="scope">
            <el-image
              style="width: 60px; height: 60px"
              :src="scope.row.tenantLogo"
              fit="contain"
            />
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" class-name="allowDrag">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="0"
              inactive-value="1"
              @change="handleStatusChange(scope.row)"
              :disabled="scope.row.isChange === 'Y'"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width allowDrag">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['tenant:tenant:edit']"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-check"
              @click="handleMenuScope(scope.row)"
              v-hasPermi="['tenant:tenant:edit']"
              v-if="scope.row.isChange === 'N'"
            >菜单屏蔽配置
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['tenant:tenant:remove']"
              v-if="scope.row.isChange === 'N'"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 添加或修改租户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="租户账号" prop="tenantName">
              <el-input v-model="form.tenantName" placeholder="请输入租户账号"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="策略名称" prop="tenantName">
              <el-select v-model="form.strategyId" placeholder="请选择" :disabled="form.tenantId != undefined">
                <el-option
                  v-for="item in strategyList"
                  :key="item.strategyId"
                  :label="item.name"
                  :value="item.strategyId"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="租户名称" prop="tenantNick">
              <el-input v-model="form.tenantNick" placeholder="请输入租户名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="系统名称" prop="tenantSystemName">
              <el-input v-model="form.tenantSystemName" placeholder="请输入系统名称"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="可修改次数" prop="tenantNameFrequency">
              <el-input-number v-model="form.tenantNameFrequency" controls-position="right" :min="0" :precision="0"
                               :max="100" size="small"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 分配角色菜单配置对话框 -->
    <el-dialog :title="title" :visible.sync="openMenuScope" width="500px" append-to-body>
      <el-form ref="form" :model="form" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="租户账号" prop="roleName">
              <el-input v-model="form.tenantName" readonly/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单权限">
              <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event)">展开/折叠
              </el-checkbox>
              <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event)">全选/全不选
              </el-checkbox>
              <el-tree
                class="tree-border"
                :data="systemMenuOptions"
                show-checkbox
                ref="systemMenu"
                node-key="id"
                :check-strictly="false"
                empty-text="加载中，请稍后"
                :props="defaultProps"
              ></el-tree>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitMenuScope">确 定</el-button>
        <el-button @click="cancelMenuScope">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addTenant,
  delTenant,
  getMenuScope,
  getTenant,
  listTenant,
  menuScope,
  updateTenant,
  updateTenantSort
} from '@/api/tenant/tenant'
import Sortable from 'sortablejs'
import {listStrategyExclude} from '@/api/tenant/strategy'
import {treeSelectPermitAllOnlyPublic as treeSelectPermitAllOnlyPublic} from "@/api/common/temporary"

export default {
  name: 'Tenant',
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 提交状态
      submitLoading: false,
      // 选中数组
      ids: [],
      idNames: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 租户信息表格数据
      tenantList: [],
      // 租户信息表格原始数据
      oldTenantList: [],
      // 可用策略集合
      strategyList: [],
      // 状态字典
      statusOptions: [],
      // 排序保存按钮显示
      sortVisible: false,
      // 排序参数
      sortable:null,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层（菜单配置）
      openMenuScope: false,
      menuExpand: false,
      menuNodeAll: false,
      // 模块&菜单列表
      systemMenuOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tenantId: null,
        tenantName: null,
        tenantSystemName: null,
        tenantNick: null,
        status: null
      },
      // 表单参数
      form: {},
      systemMenuForm:{},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
        tenantName: [
          {required: true, message: '租户账号不能为空', trigger: 'blur'}
        ],
        tenantSystemName: [
          {required: true, message: '系统名称不能为空', trigger: 'blur'}
        ],
        tenantNick: [
          {required: true, message: '租户名称不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getDict()
  },
  methods: {
    /** 查询租户信息列表 */
    getList() {
      this.loading = true
      listTenant(this.queryParams).then(response => {
        this.tenantList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询字典信息 */
    getDict() {
      this.getDicts('sys_normal_disable').then(response => {
        this.statusOptions = response.data
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 取消按钮（菜单权限）
    cancelMenuScope() {
      this.openMenuScope = false
      this.reset()
    },
    // 表单重置
    reset() {
      if (this.$refs.systemMenu !== undefined) {
        this.$refs.systemMenu.setCheckedKeys([])
      }
      this.form = {
        tenantId: null,
        strategyId: null,
        tenantName: null,
        tenantSystemName: null,
        tenantNick: null,
        tenantLogo: null,
        tenantNameFrequency: 1,
        sort: 0,
        status: '0',
        remark: null,

        values: [],
        isChange: 0,
        hasMain: false
      }
      this.systemMenuForm = {
        enterpriseId:null,
        params:{}
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.tenantId)
      this.idNames = selection.map(item => item.name)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 树权限（展开/折叠）
    handleCheckedTreeExpand(value) {
        let treeList = this.systemMenuOptions
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.systemMenu.store.nodesMap[treeList[i].id].expanded = value
        }
    },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value) {
        this.$refs.systemMenu.setCheckedNodes(value ? this.systemMenuOptions : [])
    },
    /** 获取可用策略组 */
    getStrategyList() {
      listStrategyExclude().then(response => {
        this.strategyList = response.data
      })
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.getStrategyList()
      this.open = true
      this.title = '添加租户信息'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getStrategyList()
      getTenant({tenantId: row.tenantId}).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改租户信息'
      })
    },
    /** 修改状态按钮操作 */
    handleStatusChange(row) {
      updateTenant({tenantId: row.tenantId, isChange: row.isChange, status: row.status, updateType: '1'}).then(response => {
        this.msgSuccess('修改成功')
      }).catch(() => {
        row.status = row.status === '0' ? '1' : '0'
      })
    },
    /** 分配菜单权限操作 */
    handleMenuScope(row) {
      this.reset()
      this.getSystemMenuTreeSelect()
      const menuScope = this.getMenuScope(row.tenantId)
      getTenant({tenantId: row.tenantId}).then(response => {
        this.form = response.data
        menuScope.then(res => {
          this.$refs.systemMenu.setCheckedKeys(Array.from(res.data, x => x.systemMenuId))
        })
        this.openMenuScope = true
        this.title = "屏蔽菜单权限"
      })
    },
    /** 查询模块&菜单树结构 */
    getSystemMenuTreeSelect() {
      treeSelectPermitAllOnlyPublic({ status: '0' }).then(response => {
        this.systemMenuOptions = response.data
      })
    },
    /** 根据角色Id查询模块&菜单树结构 */
    getMenuScope(Id) {
      return getMenuScope({ enterpriseId: Id }).then(response => {
        return response
      })
    },
    // 所有模块&菜单节点数据
    getSystemMenuAllCheckedKeys() {
      // 目前被选中的菜单节点
      return this.$refs.systemMenu.getCheckedKeys()
    },
    /** 提交按钮（菜单权限） */
    submitMenuScope: function () {
      this.submitLoading = true
      this.systemMenuForm.enterpriseId = this.form.tenantId
      if (this.systemMenuForm.enterpriseId !== undefined) {
        this.systemMenuForm.params.systemMenuIds = this.getSystemMenuAllCheckedKeys()
        menuScope(this.systemMenuForm).then(response => {
          this.msgSuccess("修改成功")
          this.openMenuScope = false
          this.getList()
        })
      }
      this.submitLoading = false
    },
    /** 提交按钮 */
    submitForm() {
      this.submitLoading = true
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.tenantId != null) {
            if (this.form.isChange === 'N') {
              updateTenant(this.form).then(response => {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              })
            } else {
              this.$message({
                message: '系统租户不允许进行修改操作',
                type: 'warning'
              })
            }
          } else {
            addTenant(this.form).then(response => {
              this.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
      this.submitLoading = false
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.submitLoading = true
      const tenantIds = row.tenantId || this.ids
      const names = row.name || this.idNames
      let that = this
      this.$confirm('是否确认删除租户"' + names + '"?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        return delTenant(that.updateParamIds(tenantIds))
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(() => {
      })
      this.submitLoading = false
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('tenant/tenant/export', {
        ...this.queryParams
      }, `租户信息数据.xlsx`)
    },
    /** 保存排序按钮操作 */
    handleSort() {
      this.submitLoading = true
      this.$confirm('是否确认保存新排序?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = this.sortOrderListOnlyDynamic(this.tenantList, this.oldTenantList, 'tenantId')
        if (params.length > 0) {
          return updateTenantSort(this.updateParamIds(params))
        }
      }).then(() => {
        this.getList()
        this.sortVisible = false
        this.msgSuccess('保存成功')
      }).catch(() => {})
      this.submitLoading = false
    },
    /** 排序开关 */
    handleSortable(sortable) {
      if (!this.isMobile()) {
        this.sortable != null && this.sortable.destroy()
        const el = this.$refs.dataTable.$el.querySelectorAll(".el-table__body-wrapper > table > tbody")[0]
        this.sortable = Sortable.create(el, {
          disabled: sortable,
          handle: ".allowDrag",
          onEnd: evt => {
            const targetRow = this.tenantList.splice(evt.oldIndex, 1)[0]
            this.tenantList.splice(evt.newIndex, 0, targetRow)
            this.sortVisible = true
          }
        })
      }
    }
  },
  mounted() {
    this.handleSortable(false)
  }
}
</script>
