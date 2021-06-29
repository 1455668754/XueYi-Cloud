<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="配置名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入配置名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="配置类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择配置类型" clearable size="small">
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
          v-hasPermi="['tenant:nacos:add']"
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
          v-hasPermi="['tenant:nacos:edit']"
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
          v-hasPermi="['tenant:nacos:remove']"
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
          v-hasPermi="['tenant:nacos:export']"
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
          v-hasPermi="['tenant:nacos:edit']"
        >保存排序
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="nacosList" @selection-change="handleSelectionChange" ref="dataTable"
              row-key="dataId"
    >
      <el-table-column type="selection" width="55" align="center" class-name="allowDrag"/>
      <el-table-column label="编号" align="center" prop="sourceId" class-name="allowDrag">
        <template slot-scope="scope">
          <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + 1 + scope.$index }}</span>
        </template>
      </el-table-column>
      <el-table-column label="配置名称" align="center" prop="name" class-name="allowDrag"/>
      <el-table-column label="配置类型" align="center" prop="type" :formatter="typeFormat" class-name="allowDrag"/>
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat" class-name="allowDrag"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width allowDrag">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['tenant:nacos:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tenant:nacos:remove']"
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

    <!-- 添加或修改Nacos配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="配置名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入配置名称"/>
        </el-form-item>
        <el-form-item label="头部配置信息" prop="prefixStr">
          <el-input v-model="form.prefixStr" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="数据源配置信息" prop="slaveStr">
          <el-input v-model="form.slaveStr" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="尾部配置信息" prop="suffixStr">
          <el-input v-model="form.suffixStr" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="配置类型">
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listNacos, getNacos, delNacos, addNacos, updateNacos, updateNacosSort } from '@/api/tenant/nacos'
import Sortable from 'sortablejs'

export default {
  name: 'Nacos',
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
      // Nacos配置表格数据
      nacosList: [],
      // Nacos配置表格原始数据
      oldNacosList: [],
      // 排序保存按钮显示
      sortVisible: false,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 配置类型字典
      typeOptions: [],
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        prefixStr: null,
        slaveStr: null,
        suffixStr: null,
        type: null,
        sort: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: '配置名称不能为空', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '配置类型不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getDict()
  },
  methods: {
    /** 查询Nacos配置列表 */
    getList() {
      this.loading = true
      listNacos(this.queryParams).then(response => {
        this.nacosList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询字典信息 */
    getDict() {
      this.getDicts('sys_tenant_configuration_type').then(response => {
        this.typeOptions = response.data
      })
      this.getDicts('sys_normal_disable').then(response => {
        this.statusOptions = response.data
      })
    },
    // 配置类型字典翻译
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
        dataId: null,
        name: null,
        prefixStr: null,
        slaveStr: null,
        suffixStr: null,
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
      this.ids = selection.map(item => item.dataId)
      this.idNames = selection.map(item => item.name)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加Nacos配置'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      getNacos({ dataId: row.dataId }).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改Nacos配置'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.dataId != null) {
            updateNacos(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addNacos(this.form).then(response => {
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
      const dataIds = row.dataId || this.ids
      const names = row.name || this.idNames
      let that = this
      this.$confirm('是否确认删除Nacos配置"' + names + '"?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delNacos(that.updateParamIds(dataIds))
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('tenant/nacos/export', {
        ...this.queryParams
      }, `Nacos配置数据.xlsx`)
    },
    /** 保存排序按钮操作 */
    handleSort() {
      this.$confirm('是否确认保存新排序?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = this.sortOrderListOnlyDynamic(this.nacosList, this.oldNacosList, 'dataId')
        if (params.length > 0) {
          return updateNacosSort(this.updateParamIds(params))
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
        const targetRow = this.nacosList.splice(evt.oldIndex, 1)[0]
        this.nacosList.splice(evt.newIndex, 0, targetRow)
        this.sortVisible = true
      }
    })
  }
}
</script>
