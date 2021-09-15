<template>
  <div class="app-container">
    <div class="wrapper-container" v-show="showSearch">
      <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
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
            v-hasPermi="['tenant:source:add']"
          >新增
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
        <right-toolbar :showSearch.sync="showSearch" @controlSortable="handleSortable" @queryTable="getList"/>
      </el-row>

      <el-table v-loading="loading" :data="sourceList" ref="dataTable" row-key="sourceId">
        <el-table-column label="编号" align="center" prop="sourceId" class-name="allowDrag" min-width="70">
          <template slot-scope="scope">
            <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + 1 + scope.$index }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据源名称" align="center" prop="name" class-name="allowDrag" min-width="120"/>
        <el-table-column label="数据源编码" align="center" prop="slave" class-name="allowDrag" min-width="120">
          <template slot-scope="scope">
            <el-tag type="info">{{ scope.row.slave }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="读写类型" align="center" prop="type" class-name="allowDrag" min-width="120">
          <template slot-scope="scope">
            <dict-tag :options="typeOptions" :value="scope.row.type"/>
          </template>
        </el-table-column>
        <el-table-column label="数据源类型" align="center" prop="databaseType" class-name="allowDrag" min-width="120">
          <template slot-scope="scope">
            <dict-tag :options="databaseTypeOptions" :value="scope.row.databaseType"/>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" class-name="allowDrag" min-width="120">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="0"
              inactive-value="1"
              @change="handleStatusChange(scope.row)"
              :disabled="scope.row.isChange === SYSTEM_DEFAULT.TRUE"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width allowDrag" min-width="120">
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
              icon="el-icon-setting"
              @click="handleDeploy(scope.row)"
              v-hasPermi="['tenant:separation:edit']"
              v-if="scope.row.type !== SOURCE_TYPE.SOURCE_READ"
            >主从配置
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['tenant:source:remove']"
              v-if="scope.row.isChange === SYSTEM_DEFAULT.FALSE"
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

    <!-- 添加或修改数据源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body v-dialogDrag v-dialogDragHeight>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="源名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入数据源名称"/>
        </el-form-item>
        <el-form-item label="源编码" prop="slave" v-if="form.sourceId != undefined">
          <el-input v-model="form.slave" readonly/>
        </el-form-item>
        <el-form-item label="源类型">
          <el-radio-group v-model="form.databaseType" disabled>
            <el-radio
              v-for="dict in databaseTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{ dict.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="源驱动" prop="driverClassName">
          <el-input v-model="form.driverClassName" placeholder="请输入数据源驱动" :readonly="form.sourceId != undefined"/>
        </el-form-item>
        <el-form-item label="连接地址" prop="urlPrepend" @input.native="databaseUrlChange">
          <el-input v-model="form.urlPrepend" type="urlPrepend" :autosize="{ minRows: 3, maxRows: 6}"
                    placeholder="请输入连接地址" :readonly="form.sourceId != undefined"/>
        </el-form-item>
        <el-form-item label="连接参数" prop="urlAppend" @input.native="databaseUrlChange">
          <el-input v-model="form.urlAppend" type="textarea" :autosize="{ minRows: 3, maxRows: 6}"
                    placeholder="请输入连接参数" :readonly="form.sourceId != undefined"/>
        </el-form-item>
        <el-form-item label="连接信息" prop="url">
          <el-input v-model="form.url" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" disabled/>
        </el-form-item>
        <el-form-item label="源账号" prop="username">
          <el-input v-model="form.username" placeholder="请输入数据源账号" :readonly="form.sourceId != undefined"/>
        </el-form-item>
        <el-form-item label="源密码" prop="password">
          <el-input v-model="form.password" :show-password="true" placeholder="请输入数据源密码"
                    :readonly="form.sourceId != undefined"/>
        </el-form-item>
        <el-form-item label="读写类型">
          <el-radio-group v-model="form.type" :disabled="form.sourceId != undefined" @change="TypeChange">
            <el-radio
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{ dict.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status" :disabled="form.sourceId != undefined">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
              :disabled="form.isChange === SYSTEM_DEFAULT.TRUE"
            >{{ dict.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改数据源对话框 -->
    <el-dialog :title="title" :visible.sync="openSeparation" width="650px" append-to-body v-dialogDrag
               v-dialogDragHeight>
      <el-transfer
        v-model="separationList"
        :props="separationProps"
        :data="containReadList"
        :titles="separationTitles"/>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitSeparationForm">确 定</el-button>
        <el-button @click="separationCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Sortable from 'sortablejs'
import {
  listSource,
  getSource,
  delSource,
  addSource,
  updateSource,
  updateSourceSort,
  updateSourceStatus
} from '@/api/tenant/source'
import {getSeparation, readSeparation, updateSeparation} from "@/api/tenant/separation"
import {SYSTEM_DEFAULT, STATUS, STATUS_UPDATE_OPERATION} from '@constant/constants'
import {DATABASE_TYPE, SOURCE_TYPE} from '@constant/tenantConstants'

export default {
  name: 'Source',
  components: {},
  data() {
    return {
      //常量区
      SYSTEM_DEFAULT: SYSTEM_DEFAULT,
      STATUS: STATUS,
      STATUS_UPDATE_OPERATION: STATUS_UPDATE_OPERATION,
      DATABASE_TYPE: DATABASE_TYPE,
      SOURCE_TYPE: SOURCE_TYPE,
      // 遮罩层
      loading: true,
      // 提交状态
      submitLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 数据源表格数据
      sourceList: [],
      // 数据源表格原始数据
      oldSourceList: [],
      // 具备读 数据源集合
      containReadList: [],
      // 源分离配置传输数组
      separationList: [],
      //源分离配置穿梭框标题
      separationTitles: ["未使用读源", "当前读源"],
      // 排序保存按钮显示
      sortVisible: false,
      // 排序参数
      sortable: null,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      openSeparation: false,
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
        type: null,
        status: null
      },
      // 表单参数
      form: {},
      //分离设置字段别名
      separationProps: {
        key: 'sourceId',
        label: 'name'
      },
      // 表单校验
      rules: {
        name: [
          {required: true, message: '数据源名称不能为空', trigger: 'blur'}
        ],
        driverClassName: [
          {required: true, message: '驱动不能为空', trigger: 'blur'}
        ],
        urlPrepend: [
          {required: true, message: '地址不能为空', trigger: 'blur'}
        ],
        username: [
          {required: true, message: '用户名不能为空', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'}
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
      this.getDicts('sys_database_type').then(response => {
        this.databaseTypeOptions = response.data
      })
      this.getDicts('sys_source_type').then(response => {
        this.typeOptions = response.data
      })
      this.getDicts('sys_normal_disable').then(response => {
        this.statusOptions = response.data
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    separationCancel() {
      this.openSeparation = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        sourceId: null,
        name: null,
        slave: null,
        databaseType: DATABASE_TYPE.SLAVE_SOURCE,
        driverClassName: 'com.mysql.cj.jdbc.Driver',
        url: null,
        urlPrepend: 'jdbc:mysql://localhost:3306/',
        urlAppend: '?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8',
        username: null,
        password: null,
        type: SOURCE_TYPE.SOURCE_READ_WRITE,
        sort: 0,
        status: STATUS.NORMAL
      }
      this.separationList = []
      this.resetForm('form')
      this.submitLoading = false
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加数据源'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      getSource({sourceId: row.sourceId}).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改数据源'
      })
    },
    /** 配置按钮操作 */
    handleDeploy(row) {
      this.reset()
      readSeparation({sourceId: row.sourceId, type: row.type}).then(response => {
        this.containReadList = []
        for (let data of response.data) {
          this.containReadList.push({
            sourceId: data.sourceId,
            name: data.name,
            value: {sourceId: data.sourceId, slave: data.slave},
            disabled: data.sourceId === row.sourceId
          })
        }
      })
      getSeparation({sourceId: row.sourceId}).then(response => {
        this.form = response.data
        for (let data of response.data.values) {
          this.separationList.push(data.sourceId)
        }
        this.openSeparation = true
        this.title = this.form.name + '分离策略'
      })
    },
    /** 修改数据源地址 */
    databaseUrlChange() {
      this.form.url = this.form.urlPrepend + this.form.urlAppend
    },
    /** 修改数据源读写类型 | 仅新增 */
    TypeChange() {
      if (this.form.sourceId == undefined && this.form.type === SOURCE_TYPE.SOURCE_WRITE) {
        this.form.status = STATUS.DISABLE
      }else{
        this.form.status = STATUS.NORMAL
      }
    },
    /** 修改状态按钮操作 */
    handleStatusChange(row) {
      updateSourceStatus({sourceId: row.sourceId, status: row.status}).then(response => {
        this.msgSuccess('修改成功')
      }).catch(() => {
        row.status = row.status === STATUS.NORMAL ? STATUS.DISABLE : STATUS.NORMAL
      })
    },
    submitSeparationForm() {
      this.form.values = []
      for (let index of this.separationList) {
        for (let data of this.containReadList) {
          if (index === data.sourceId) {
            this.form.values.push(data.value)
          }
        }
      }
      if (this.form.values.length > 0) {
        updateSeparation(this.form).then(response => {
          this.msgSuccess('配置成功')
          this.openSeparation = false
          this.getList()
        }).catch(() => {
          this.submitLoading = false
        })
      } else {
        this.msgWarning('请添加读数据源后再保存')
        this.submitLoading = false
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.submitLoading = true
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.sourceId != null) {
            updateSource(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.op = false
              this.getList()
            }).catch(() => {
              this.submitLoading = false
            })
          } else {
            addSource(this.form).then(response => {
              this.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }).catch(() => {
              this.submitLoading = false
            })
          }
        } else {
          this.submitLoading = false
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除数据源"' + row.name + '"?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        return delSource({sourceId: row.sourceId})
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(() => {
      })
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
            const targetRow = this.sourceList.splice(evt.oldIndex, 1)[0]
            this.sourceList.splice(evt.newIndex, 0, targetRow)
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
