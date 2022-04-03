<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="字典编码" prop="code">
        <el-select v-model="queryParams.code" placeholder="请选择" clearable>
          <el-option
            v-for="dict in codeOptions"
            :key="dict.code"
            :label="dict.name"
            :value="dict.code"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="数据标签" prop="label">
        <el-input
          v-model="queryParams.label"
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
          v-hasPermi="[DictDataAuth.DICT]"
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
          v-hasPermi="[DictDataAuth.DICT]"
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
          v-hasPermi="[DictDataAuth.DICT]"
        >删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"/>
    </el-row>

    <el-table v-loading="loading" :data="tableList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" v-if="columns[0].visible" min-width="55"/>
      <el-table-column label="序号" align="center" v-if="columns[1].visible" min-width="80">
        <template v-slot="scope">
          <span>{{ queryParams.pageSize * (queryParams.page - 1) + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="字典编码" align="center" prop="code" v-if="columns[2].visible" :show-overflow-tooltip="true"
                       min-width="100"
      />
      <el-table-column label="数据键值" align="center" prop="value" v-if="columns[3].visible" :show-overflow-tooltip="true"
                       min-width="100"
      />
      <el-table-column label="数据标签" align="center" prop="label" v-if="columns[4].visible" :show-overflow-tooltip="true"
                       min-width="100"
      />
      <el-table-column label="状态" align="center" prop="status" v-if="columns[5].visible" :show-overflow-tooltip="true"
                       min-width="100"
      >
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[6].visible"
                       :show-overflow-tooltip="true" min-width="100"
      >
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" v-if="columns[7].visible" class-name="small-padding fixed-width"
                       width="120" fixed="right"
      >
        <template v-slot="scope">
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.EDIT"
            @click="handleUpdate(scope.row)"
            v-hasPermi="[DictDataAuth.DICT]"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.DELETE"
            @click="handleDelete(scope.row)"
            v-hasPermi="[DictDataAuth.DICT]"
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

    <!-- 添加或修改字典数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" :before-close="handleClose" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="字典编码" prop="code">
              <el-select v-model="form.code" placeholder="请选择" disabled>
                <el-option
                  v-for="dict in codeOptions"
                  :key="dict.code"
                  :label="dict.name"
                  :value="dict.code"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据键值" prop="value">
              <el-input v-model="form.value" placeholder="请输入数据键值"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据标签" prop="label">
              <el-input v-model="form.label" placeholder="请输入数据标签"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="样式属性" prop="cssClass">
              <el-select v-model="form.cssClass" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.sys_dict_color"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表格回显样式" prop="listClass">
              <el-select v-model="form.listClass" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.sys_dict_color"
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
          <el-col :span="12" v-if="form.id !== undefined">
            <el-form-item label="是否默认" prop="isDefault">
              <el-radio-group v-model="form.isDefault" disabled>
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
            <el-form-item label="显示顺序" prop="sort">
              <el-input-number v-model="form.sort" :max="65535"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" type="textarea"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listDictDataApi,
  getDictDataApi,
  addDictDataApi,
  editDictDataApi,
  delDictDataApi
} from '@/api/tenant/dict/dictData'
import { DictTypeAuth } from '@auth/tenant'
import { DicSortEnum, DicStatusEnum, IconEnum } from '@enums'
import { optionDictTypeApi } from '@/api/tenant/dict/dictType'

export default {
  name: 'DictDataManagement',
  /** 字典查询 */
  dicts: ['sys_yes_no', 'sys_dict_color', 'sys_normal_disable'],
  data() {
    return {
      //权限标识
      DictDataAuth: DictTypeAuth,
      // 图标标识
      IconEnum: IconEnum,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中数组名称
      idNames: [],
      // 字典编码选项
      codeOptions: [],
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
      // 提交状态
      submitLoading: false,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        code: undefined,
        label: undefined,
        status: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `勾选列`, visible: true },
        { key: 1, label: `序号列`, visible: true },
        { key: 2, label: `字典编码`, visible: true },
        { key: 3, label: `数据键值`, visible: true },
        { key: 4, label: `数据标签`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `创建时间`, visible: true },
        { key: 7, label: `操作列`, visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        value: [
          { required: true, message: '数据键值不能为空', trigger: 'blur' }
        ],
        label: [
          { required: true, message: '数据标签不能为空', trigger: 'blur' }
        ],
        isDefault: [
          { required: true, message: '是否默认不能为空', trigger: 'change' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getOptions()
    const code = this.$route.params && this.$route.params.code
    if (code !== undefined) {
      this.queryParams.code = code
    }
    this.getList()
  },
  methods: {
    /** 查询字典数据列表 */
    getList() {
      this.loading = true
      listDictDataApi(this.queryParams).then(response => {
        this.tableList = response.data.items
        this.total = response.data.total
        this.loading = false
      })
    },
    /** 查询选项列表 */
    getOptions() {
      this.codeOptions = []
      optionDictTypeApi().then(response => {
        this.codeOptions = response.data.items
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
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        code: undefined,
        value: undefined,
        label: undefined,
        sort: DicSortEnum.ZERO,
        status: DicStatusEnum.NORMAL,
        isDefault: undefined,
        cssClass: undefined,
        listClass: undefined,
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
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.idNames = selection.map(item => item.label)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加字典数据'
    },
    /** 修改操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getDictDataApi(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改字典数据'
      })
    },
    /** 提交操作 */
    submitForm: function() {
      this.submitLoading = false
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            editDictDataApi(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).catch()
          } else {
            addDictDataApi(this.form).then(response => {
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
      const delNames = row.label || this.idNames
      this.$modal.confirm('是否确定要删除' + delNames + '？').then(function() {
        return delDictDataApi(delIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功！')
      }).catch(() => {
      })
    }
  }
}
</script>
