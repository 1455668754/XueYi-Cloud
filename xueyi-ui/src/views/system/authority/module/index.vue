<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模块名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模块类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.auth_frame_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="显隐状态" prop="hideModule">
        <el-select v-model="queryParams.hideModule" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_show_hide"
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
      <el-form-item label="公共模块" prop="isCommon">
        <el-select v-model="queryParams.isCommon" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_common_private"
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
          v-hasPermi="[ModuleAuth.ADD]"
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
          v-hasPermi="[ModuleAuth.EDIT]"
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
          v-hasPermi="[ModuleAuth.DELETE]"
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
      <el-table-column label="模块名称" align="center" prop="name" v-if="columns[2].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="模块logo" align="center" prop="logo" v-if="columns[3].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <el-image style="width: 80px; height: 80px" :src="scope.row.logo" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column label="模块类型" align="center" prop="type" v-if="columns[4].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.auth_frame_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="显隐状态" align="center" prop="hideModule" v-if="columns[5].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_show_hide" :value="scope.row.hideModule" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" v-if="columns[6].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="公共模块" align="center" prop="isCommon" v-if="columns[7].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_common_private" :value="scope.row.isCommon" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" v-if="columns[8].visible" class-name="small-padding fixed-width" width="180" fixed="right">
        <template v-slot="scope">
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.EDIT"
            @click="handleUpdate(scope.row)"
            v-hasPermi="[ModuleAuth.EDIT]"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.DELETE"
            @click="handleDelete(scope.row)"
            v-hasPermi="[ModuleAuth.DELETE]"
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

    <!-- 添加或修改模块对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" :before-close="handleClose" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="模块名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入模块名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="模块logo" prop="logo">
              <image-upload v-model="form.logo" :limit="1" :isShowTip="false" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路由地址" prop="path">
              <el-input v-model="form.path" placeholder="请输入路由地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路由参数" prop="paramPath">
              <el-input v-model="form.paramPath" placeholder="请输入路由参数" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模块类型" prop="type">
              <el-radio-group v-model="form.type">
                <el-radio-button
                  v-for="dict in dict.type.auth_frame_type"
                  :key="dict.value"
                  :label="dict.value"
                  :value="dict.value"
                >{{ dict.label }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显隐状态" prop="hideModule">
              <el-radio-group v-model="form.hideModule">
                <el-radio-button
                  v-for="dict in dict.type.sys_show_hide"
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
          <el-col :span="12" v-if="isLessor">
            <el-form-item label="公共模块" prop="isCommon">
              <el-radio-group v-model="form.isCommon" :disabled="form.id !== undefined">
                <el-radio-button
                  v-for="dict in dict.type.sys_common_private"
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
  </div>
</template>

<script>
import { listModuleApi, getModuleApi, addModuleApi, editModuleApi, delModuleApi } from '@/api/system/authority/module'
import { ModuleAuth } from '@auth/system'
import { DicCommonPrivateEnum, DicShowHideEnum, DicSortEnum, DicStatusEnum, IconEnum } from '@enums'
import { FrameTypeEnum } from '@enums/system'
import store from '@/store'

export default {
  name: 'ModuleManagement',
  /** 字典查询 */
  dicts: ['sys_yes_no', 'auth_frame_type', 'sys_show_hide', 'sys_normal_disable', 'sys_common_private'],
  data() {
    return {
      //权限标识
      ModuleAuth: ModuleAuth,
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
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 提交状态
      submitLoading: false,
      // 是否为租管租户
      isLessor: store.getters.isLessor,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        name: undefined,
        type: undefined,
        hideModule: undefined,
        status: undefined,
        isCommon: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `勾选列`, visible: true },
        { key: 1, label: `序号列`, visible: true },
        { key: 2, label: `模块名称`, visible: true },
        { key: 3, label: `模块logo`, visible: true },
        { key: 4, label: `模块类型`, visible: true },
        { key: 5, label: `显隐状态`, visible: true },
        { key: 6, label: `状态`, visible: true },
        { key: 7, label: `公共模块`, visible: true },
        { key: 8, label: `操作列`, visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: '模块名称不能为空', trigger: 'blur' }
        ],
        logo: [
          { required: true, message: '模块logo不能为空', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '模块类型不能为空', trigger: 'change' }
        ],
        hideModule: [
          { required: true, message: '显隐状态不能为空', trigger: 'change' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询模块列表 */
    getList() {
      this.loading = true
      listModuleApi(this.queryParams).then(response => {
        this.tableList = response.data.items
        this.total = response.data.total
        this.loading = false
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
        name: undefined,
        logo: undefined,
        path: undefined,
        paramPath: undefined,
        type: FrameTypeEnum.NORMAL,
        hideModule: DicShowHideEnum.SHOW,
        sort: DicSortEnum.ZERO,
        status: DicStatusEnum.NORMAL,
        isCommon: DicCommonPrivateEnum.PRIVATE,
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
      this.idNames = selection.map(item => item.name)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加模块'
    },
    /** 修改操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getModuleApi(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改模块'
      })
    },
    /** 提交操作 */
    submitForm: function() {
      this.submitLoading = false
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            editModuleApi(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).catch()
          } else {
            addModuleApi(this.form).then(response => {
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
        return delModuleApi(delIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功！')
      }).catch(() => {
      })
    }
  }
}
</script>
