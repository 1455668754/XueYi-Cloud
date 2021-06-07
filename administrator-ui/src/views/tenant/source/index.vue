<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="数据源名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入数据源名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据源类型" prop="databaseType">
        <el-select v-model="queryParams.databaseType" placeholder="请选择数据源类型" clearable size="small">
          <el-option
            v-for="dict in databaseTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="数据源编码" prop="slave">
        <el-input
          v-model="queryParams.slave"
          placeholder="请输入数据源编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="驱动" prop="driverClassName">
        <el-input
          v-model="queryParams.driverClassName"
          placeholder="请输入驱动"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入用户名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          v-model="queryParams.password"
          placeholder="请输入密码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="读写类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择读写类型" clearable size="small">
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
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
          v-hasPermi="['tenant:source:add']"
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
          v-hasPermi="['tenant:source:edit']"
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
          v-hasPermi="['tenant:source:remove']"
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
          v-hasPermi="['tenant:source:export']"
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
          v-hasPermi="['tenant:source:edit']"
        >保存排序
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sourceList" @selection-change="handleSelectionChange" ref="dataTable"
              row-key="sourceId"
    >
      <el-table-column type="selection" width="55" align="center" class-name="allowDrag"/>
      <el-table-column label="编号" align="center" prop="sourceId" class-name="allowDrag">
        <template slot-scope="scope">
          <span>{{ (queryParams.pageNum-1)*queryParams.pageSize+1+scope.$index}}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据源名称" align="center" prop="name" class-name="allowDrag"/>

      <el-table-column label="驱动" align="center" prop="driverClassName" class-name="allowDrag"/>
      <el-table-column label="地址" align="center" prop="url" class-name="allowDrag"/>
      <el-table-column label="用户名" align="center" prop="username" class-name="allowDrag"/>
      <el-table-column label="密码" align="center" prop="password" class-name="allowDrag"/>
      <el-table-column label="读写类型" align="center" prop="type" :formatter="typeFormat" class-name="allowDrag"/>
      <el-table-column label="数据源类型" align="center" prop="databaseType" :formatter="databaseTypeFormat" class-name="allowDrag"/>
      <el-table-column label="状态" align="center" prop="status" class-name="allowDrag">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
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
            v-hasPermi="['tenant:source:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tenant:source:remove']"
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

    <!-- 添加或修改数据源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="数据源名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入数据源名称"/>
        </el-form-item>
        <el-form-item label="数据源类型">
          <el-radio-group v-model="form.databaseType">
            <el-radio
              v-for="dict in databaseTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{ dict.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="驱动" prop="driverClassName">
          <el-input v-model="form.driverClassName" placeholder="请输入驱动"/>
        </el-form-item>
        <el-form-item label="地址" prop="url">
          <el-input v-model="form.url" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="读写类型">
          <el-radio-group v-model="form.type">
            <el-radio
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{ dict.dictLabel }}
            </el-radio>
          </el-radio-group>
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
        <el-form-item label="删除标志(0正常 1删除)" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志(0正常 1删除)"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSource, getSource, delSource, addSource, updateSource, updateSourceSort } from '@/api/tenant/source'
import Sortable from 'sortablejs'
import Tenant from '@/views/system/tenant/tenant'

export default {
  name: 'Source',
  components: { Tenant },
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
      // 数据源表格数据
      sourceList: [],
      // 数据源表格原始数据
      oldSourceList: [],
      // 排序保存按钮显示
      sortVisible: false,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 数据源类型字典
      databaseTypeOptions: [],
      // 读写类型字典
      typeOptions: [],
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        slave: null,
        databaseType: null,
        driverClassName: null,
        url: null,
        username: null,
        password: null,
        type: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        driverClassName: [
          { required: true, message: '驱动不能为空', trigger: 'blur' }
        ],
        url: [
          { required: true, message: '地址不能为空', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getDict()
  },
  methods: {
    /** 查询数据源列表 */
    getList() {
      this.loading = true
      listSource(this.queryParams).then(response => {
        this.sourceList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询字典信息 */
    getDict() {
      this.getDicts('sys_tenant_resource_type').then(response => {
        this.databaseTypeOptions = response.data
      })
      this.getDicts('sys_tenant_read_type').then(response => {
        this.typeOptions = response.data
      })
      this.getDicts('sys_normal_disable').then(response => {
        this.statusOptions = response.data
      })
    },
    // 数据库(0从库 1主库)字典翻译
    databaseTypeFormat(row, column) {
      return this.selectDictLabel(this.databaseTypeOptions, row.databaseType)
    },
    // 读写类型(0读&写 1只读 2只写)字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type)
    },
    // 状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        sourceId: null,
        name: null,
        slave: null,
        databaseType: '0',
        driverClassName: null,
        url: null,
        username: null,
        password: null,
        type: '0',
        sort: 0,
        status: '0'
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
      this.ids = selection.map(item => item.sourceId)
      this.idNames = selection.map(item => item.sourceId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加数据源'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      getSource({ sourceId: row.sourceId }).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改数据源'
      })
    },
    /** 修改状态按钮操作 */
    handleStatusChange(row) {
      updateSource(row).then(response => {
        this.msgSuccess("修改成功");
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.sourceId != null) {
            updateSource(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addSource(this.form).then(response => {
              this.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const sourceIds = row.sourceId || this.ids
      const names = row.name || this.idNames
      let that = this
      this.$confirm('是否确认删除数据源"' + names + '"?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delSource(that.updateParamIds(sourceIds))
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('tenant/source/export', {
        ...this.queryParams
      }, `tenant_source.xlsx`)
    },
    /** 保存排序按钮操作 */
    handleSort() {
      this.$confirm('是否确认保存新排序?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = this.sortOrderListOnlyDynamic(this.sourceList, this.oldSourceList, 'sourceId')
        if (params.length > 0) {
          return updateSourceSort(this.updateParamIds(params))
        }
      }).then(() => {
        this.getList()
        this.sortVisible = false
        this.msgSuccess('保存成功')
      }).catch(() => {
      })
    }
  },
  mounted() {
    const el = this.$refs.dataTable.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
    Sortable.create(el, {
      handle: '.allowDrag',
      onEnd: evt => {
        const targetRow = this.sourceList.splice(evt.oldIndex, 1)[0]
        this.sourceList.splice(evt.newIndex, 0, targetRow)
        this.sortVisible = true
      }
    })
  }
}
</script>
