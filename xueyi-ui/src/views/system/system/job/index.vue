<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务组名" prop="jobGroup">
        <el-select v-model="queryParams.jobGroup" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_job_group"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_job_status"
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
          v-hasPermi="[JobAuth.ADD]"
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
          v-hasPermi="[JobAuth.EDIT]"
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
          v-hasPermi="[JobAuth.DELETE]"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-s-operation"
          size="mini"
          @click="handleJobLog"
          v-hasPermi="[JobAuth.LIST]"
        >日志
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns" />
    </el-row>

    <el-table v-loading="loading" :data="tableList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" v-if="columns[0].visible" width="55" />
      <el-table-column label="序号" align="center" v-if="columns[1].visible" width="80">
        <template v-slot="scope">
          <span>{{ queryParams.pageSize * (queryParams.page - 1) + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务名称" align="center" prop="name" v-if="columns[2].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="任务组名" align="center" prop="jobGroup" v-if="columns[3].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_job_group" :value="scope.row.jobGroup" />
        </template>
      </el-table-column>
      <el-table-column label="调用目标字符串" align="center" prop="invokeTarget" v-if="columns[4].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="cron执行表达式" align="center" prop="cronExpression" v-if="columns[5].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="计划执行错误策略" align="center" prop="misfirePolicy" v-if="columns[6].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_job_policy" :value="scope.row.misfirePolicy" />
        </template>
      </el-table-column>
      <el-table-column label="是否并发执行" align="center" prop="concurrent" v-if="columns[7].visible" :show-overflow-tooltip="true" min-width="80">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_job_concurrent" :value="scope.row.concurrent" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" v-if="columns[8].visible" :show-overflow-tooltip="true" min-width="60">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_job_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160" fixed="right">
        <template v-slot="scope">
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.EDIT"
            @click="handleUpdate(scope.row)"
            v-hasPermi="[JobAuth.EDIT]"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.DELETE"
            @click="handleDelete(scope.row)"
            v-hasPermi="[JobAuth.DELETE]"
          >删除
          </el-button>
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)"
                       v-hasPermi="[JobAuth.LIST, JobAuth.EDIT]"
          >
            <span class="el-dropdown-link">
              <i class="el-icon-d-arrow-right el-icon--right"></i>更多
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="handleRun" icon="el-icon-caret-right"
                                v-hasPermi="[JobAuth.EDIT]"
              >执行一次
              </el-dropdown-item>
              <el-dropdown-item command="handleJobLog" icon="el-icon-s-operation"
                                v-hasPermi="[JobAuth.LIST]"
              >调度日志
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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

    <!-- 添加或修改定时任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" :before-close="handleClose" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务组名" prop="jobGroup">
              <el-select v-model="form.jobGroup" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.sys_job_group"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="invokeTarget">
              <span slot="label">
                调用方法
                <el-tooltip placement="top">
                  <div slot="content">
                    Bean调用示例：ryTask.ryParams('xy')
                    <br />Class类调用示例：com.xueyi.quartz.task.RyTask.ryParams('xy')
                    <br />参数说明：支持字符串，布尔类型，长整型，浮点型，整型
                  </div>
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-input v-model="form.invokeTarget" placeholder="请输入调用目标字符串" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="cron表达式" prop="cronExpression">
              <el-input v-model="form.cronExpression" placeholder="请输入cron执行表达式">
                <template slot="append">
                  <el-button type="primary" @click="handleShowCron">
                    生成表达式
                    <i class="el-icon-time el-icon--right"></i>
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划执行错误策略" prop="misfirePolicy">
              <el-select v-model="form.misfirePolicy" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.sys_job_policy"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否并发执行" prop="concurrent">
              <el-select v-model="form.concurrent" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.sys_job_concurrent"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.sys_job_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
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

    <el-dialog title="Cron表达式生成器" :visible.sync="openCron" append-to-body destroy-on-close class="scrollbar">
      <Crontab @hide="openCron=false" @fill="crontabFill" :expression="expression"></Crontab>
    </el-dialog>
  </div>
</template>

<script>
import { listJobApi, getJobApi, addJobApi, editJobApi, delJobApi, runJobApi } from '@/api/system/system/job'
import { JobConcurrentEnum, JobLogIndexGo, JobMisfireEnum, JobStatusEnum } from '@enums/system'
import Crontab from '@/components/Crontab'
import { JobAuth } from '@auth/system'
import store from '@/store'
import { IconEnum } from '@enums'

export default {
  name: 'JobManagement',
  components: { Crontab },
  /** 字典查询 */
  dicts: ['sys_job_concurrent', 'sys_job_group', 'sys_job_policy', 'sys_job_status'],
  data() {
    return {
      //权限标识
      JobAuth: JobAuth,
      // 图标标识
      IconEnum: IconEnum,
      // 遮罩层
      loading: true,
      // 提交状态
      submitLoading: false,
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
      // 是否显示Cron表达式弹出层
      openCron: false,
      // 传入的表达式
      expression: '',
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        name: undefined,
        jobGroup: undefined,
        status: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `勾选列`, visible: true },
        { key: 1, label: `序号列`, visible: true },
        { key: 2, label: `任务名称`, visible: true },
        { key: 3, label: `任务组名`, visible: true },
        { key: 4, label: `调用目标字符串`, visible: true },
        { key: 5, label: `cron执行表达式`, visible: true },
        { key: 6, label: `计划执行错误策略`, visible: true },
        { key: 7, label: `是否并发执行`, visible: true },
        { key: 8, label: `状态`, visible: true },
        { key: 9, label: `操作列`, visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: '任务名称不能为空', trigger: 'blur' }
        ],
        jobGroup: [
          { required: true, message: '任务组名不能为空', trigger: 'change' }
        ],
        invokeTarget: [
          { required: true, message: '调用目标字符串不能为空', trigger: 'blur' }
        ],
        cronExpression: [
          { required: true, message: 'cron执行表达式不能为空', trigger: 'blur' }
        ],
        misfirePolicy: [
          { required: true, message: '计划执行错误策略不能为空', trigger: 'change' }
        ],
        concurrent: [
          { required: true, message: '是否并发执行不能为空', trigger: 'change' }
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
    /** 查询调度任务列表 */
    getList() {
      this.loading = true
      listJobApi(this.queryParams).then(response => {
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
        jobGroup: undefined,
        invokeTarget: undefined,
        cronExpression: undefined,
        misfirePolicy: JobMisfireEnum.DEFAULT,
        concurrent: JobConcurrentEnum.FORBID,
        status: JobStatusEnum.PAUSE,
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
    /** 更多操作触发 */
    handleCommand(command, row) {
      switch (command) {
        case 'handleRun':
          this.handleRun(row)
          break
        case 'handleJobLog':
          this.handleJobLog(row)
          break
        default:
          break
      }
    },
    /** 立即执行一次 */
    handleRun(row) {
      this.$modal.confirm('确认要立即执行一次"' + row.name + '"任务吗？').then(function() {
        return runJobApi(row.id)
      }).then(() => {
        this.$modal.msgSuccess('执行成功')
      }).catch(() => {
      })
    },
    /** cron表达式按钮操作 */
    handleShowCron() {
      this.expression = this.form.cronExpression
      this.openCron = true
    },
    /** 确定后回传值 */
    crontabFill(value) {
      this.form.cronExpression = value
    },
    /** 调度日志列表查询 */
    handleJobLog(row) {
      const id = row.id || this.ids[0]
      const route = store.getters.routePath[JobLogIndexGo]
      this.$router.push(route + id)
    },
    /** 新增操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加调度任务'
    },
    /** 修改操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getJobApi(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改调度任务'
      })
    },
    /** 提交操作 */
    submitForm: function() {
      this.submitLoading = true
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            editJobApi(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).catch()
          } else {
            addJobApi(this.form).then(response => {
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
        return delJobApi(delIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功！')
      }).catch(() => {
      })
    }
  }
}
</script>
