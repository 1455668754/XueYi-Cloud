<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="租户Id" prop="tenantId">
        <el-input
          v-model="queryParams.tenantName"
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
          v-hasPermi="['tenant:tenant:edit']"
        >保存排序
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tenantList" @selection-change="handleSelectionChange" ref="dataTable"
              row-key="tenantId"
    >
      <el-table-column type="selection" width="55" align="center" class-name="allowDrag"/>
      <el-table-column label="租户Id" align="center" prop="tenantId" class-name="allowDrag"/>
      <el-table-column label="租户账号" align="center" prop="tenantName" class-name="allowDrag"/>
      <el-table-column label="系统名称" align="center" prop="tenantSystemName" class-name="allowDrag"/>
      <el-table-column label="租户名称" align="center" prop="tenantNick" class-name="allowDrag"/>
      <el-table-column label="租户logo" align="center" prop="tenantLogo" class-name="allowDrag">
        <template slot-scope="scope">
          <el-image
            style="width: 60px; height: 60px"
            :src="scope.row.tenantLogo"
            fit="contain"
          />
        </template>
      </el-table-column>
      <el-table-column label="可修改次数" align="center" prop="tenantNameFrequency" class-name="allowDrag"/>
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
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tenant:tenant:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改租户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="租户账号" prop="tenantName">
          <el-input v-model="form.tenantName" placeholder="请输入租户账号"/>
        </el-form-item>
        <el-form-item label="系统名称" prop="tenantSystemName">
          <el-input v-model="form.tenantSystemName" placeholder="请输入系统名称"/>
        </el-form-item>
        <el-form-item label="租户名称" prop="tenantNick">
          <el-input v-model="form.tenantNick" placeholder="请输入租户名称"/>
        </el-form-item>
        <el-form-item label="租户账号修改次数" prop="tenantNameFrequency">
          <el-input v-model="form.tenantNameFrequency" placeholder="请输入租户账号修改次数"/>
        </el-form-item>
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
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-divider content-position="center">数据源策略信息</el-divider>
        <div class="value-set">
          <div class="value-title">策略：</div>
          <div class="value-add">
            <el-button type="primary" plain @click="valueAdd">添加策略</el-button>
          </div>
          <el-table :data="form.values">
            <el-table-column label="策略名称" min-width="40%" align="center">
              <template slot-scope="scope">
                <el-select v-model="scope.row.strategyId" placeholder="请选择" @change="valueChange(scope.row.strategyId)">
                  <el-option
                    v-for="item in strategyList"
                    :key="item.strategyId"
                    :label="item.name"
                    :value="item.strategyId"
                  >
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="主策略" align="center" min-width="20%" prop="isMain">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.isMain"
                  active-value="Y"
                  inactive-value="N"
                  :disabled="form.hasMain === true && scope.row.isMain=== 'N'"
                  @change="valueMainChange(scope.row.isMain)"
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column label="策略状态" align="center" min-width="20%" prop="status">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.status"
                  active-value="0"
                  inactive-value="1"
                  disabled
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="20%" align="center">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="valueDelete(scope.row)"
                  v-hasPermi="['tenant:tenant:edit']"
                >删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTenant, getTenant, delTenant, addTenant, updateTenant, updateTenantSort } from '@/api/tenant/tenant'
import Sortable from 'sortablejs'
import { listStrategyExclude } from '@/api/tenant/strategy'
import { updateSource } from '@/api/tenant/source'

export default {
  name: 'Tenant',
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
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
      // 表单校验
      rules: {
        tenantName: [
          { required: true, message: '租户账号不能为空', trigger: 'blur' }
        ],
        tenantSystemName: [
          { required: true, message: '系统名称不能为空', trigger: 'blur' }
        ],
        tenantNick: [
          { required: true, message: '租户名称不能为空', trigger: 'blur' }
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
    // 表单重置
    reset() {
      this.form = {
        tenantId: null,
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
      getTenant({ tenantId: row.tenantId }).then(response => {
        this.form = response.data
        for (let i = 0; i < this.form.values.length; i++) {
          if (this.form.values[i].isMain === 'Y') {
            this.form.hasMain = true
          }
        }
        this.open = true
        this.title = '修改租户信息'
      })
    },
    /** 修改状态按钮操作 */
    handleStatusChange(row) {
      updateTenant(row).then(response => {
        this.msgSuccess('修改成功')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (this.valueCheck()) {
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
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const tenantIds = row.tenantId || this.ids
      const names = row.name || this.idNames
      let that = this
      this.$confirm('是否确认删除租户"' + names + '"?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delTenant(that.updateParamIds(tenantIds))
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('tenant/tenant/export', {
        ...this.queryParams
      }, `租户信息数据.xlsx`)
    },
    /** 保存排序按钮操作 */
    handleSort() {
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
      }).catch(() => {
      })
    },
    valueAdd() {
      const newData = {
        strategyId: '',
        isMain: 'N',
        status: '1'
      }
      this.form.values.push(newData)
    },
    valueChange(id) {
      const writeData = this.strategyList.filter(function(item) {
        return item.strategyId === id
      })
      let data = this.form.values.filter(function(item) {
        return item.strategyId === id
      })
      data[0].status = writeData[0].status
    },
    valueMainChange(isMain) {
      this.form.hasMain = isMain !== 'N'
    },
    valueDelete(row) {
      if (row !== undefined) {
        const index = this.form.values.indexOf(row)
        this.form.values.splice(index, 1)
      }
    },
    valueCheck() {
      let key = 0
      for (let i = 0; i < this.form.values.length; i++) {
        let increase = 0
        if (this.form.values[i].isMain === 'Y') {
          key++
          increase = 1
        }
        for (let j = 0; j < this.form.values.length; j++) {
          if (this.form.values[i].strategyId === null || this.form.values[i].strategyId === '') {
            if (this.form.values[i].isMain === 'Y') {
              this.form.hasMain = false
            }
            this.form.values.splice(i--, 1)
            if (increase === 1) {
              key--
            }
            break
          } else if (i !== j && this.form.values[i].strategyId === this.form.values[j].strategyId) {
            if (this.form.values[j].isMain === 'Y') {
              this.form.hasMain = false
            }
            this.form.values.splice(j--, 1)
          }
        }
      }
      if (this.form.values.length === 0) {
        this.$message({
          message: '有效策略数为0，请添加',
          type: 'warning'
        })
        return false
      } else if (key !== 1) {
        this.$message({
          message: '主策略有且只能有一个，请检查',
          type: 'warning'
        })
        return false
      }
      return true
    }
  },
  mounted() {
    const el = this.$refs.dataTable.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
    Sortable.create(el, {
      handle: '.allowDrag',
      onEnd: evt => {
        const targetRow = this.tenantList.splice(evt.oldIndex, 1)[0]
        this.tenantList.splice(evt.newIndex, 0, targetRow)
        this.sortVisible = true
      }
    })
  }
}
</script>

<style lang="scss" scoped>
.value-set {
  .value-title {
    float: left;
    line-height: 40px;
  }

  .value-add {
    float: right;
    margin-bottom: 10px;
  }
}

.value-input {
  text-align: center
}
</style>
