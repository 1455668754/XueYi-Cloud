<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="jobId">
        <el-select v-model="queryParams.jobId" placeholder="请选择" clearable>
          <el-option
            v-for="dict in jobIdOptions"
            :key="dict.id"
            :label="dict.name"
            :value="dict.id"
          />
        </el-select>
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
      <el-form-item label="执行状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_message_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="执行时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="IconEnum.SEARCH" size="mini" @click="handleQuery">搜索</el-button>
        <el-button :icon="IconEnum.RESET" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          :icon="IconEnum.DELETE"
          size="mini"
          @click="handleClean"
          v-hasPermi="[JobAuth.DELETE]"
        >清空
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-close"
          size="mini"
          @click="handleClose"
        >关闭
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns" />
    </el-row>

    <el-table v-loading="loading" :data="tableList">
      <el-table-column label="序号" align="center" v-if="columns[0].visible" width="80">
        <template v-slot="scope">
          <span>{{ queryParams.pageSize * (queryParams.page - 1) + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务名称" align="center" prop="name" v-if="columns[1].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="任务组名" align="center" prop="jobGroup" v-if="columns[2].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_job_group" :value="scope.row.jobGroup" />
        </template>
      </el-table-column>
      <el-table-column label="调用目标字符串" align="center" prop="invokeTarget" v-if="columns[3].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="执行状态" align="center" prop="status" v-if="columns[4].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_message_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="执行时间" align="center" prop="createTime" v-if="columns[5].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="日志信息" align="center" prop="jobMessage" v-if="columns[6].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="操作" align="center" v-if="columns[7].visible" class-name="small-padding fixed-width" width="120" fixed="right">
        <template v-slot="scope">
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.VIEW"
            @click="handleView(scope.row)"
            v-hasPermi="[JobAuth.SINGLE]"
          >查看
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

    <!-- 调度日志详细 -->
    <el-dialog title="调度日志详细" :visible.sync="open" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="任务名称">{{ form.name }}</el-descriptions-item>
        <el-descriptions-item label="任务分组">{{ form.jobGroup }}</el-descriptions-item>
        <el-descriptions-item :span="2" label="调用方法">{{ form.invokeTarget }}</el-descriptions-item>
        <el-descriptions-item label="执行状态">
          <dict-tag :options="dict.type.sys_message_status" :value="form.status" />
        </el-descriptions-item>
        <el-descriptions-item label="执行时间">{{ form.createTime }}</el-descriptions-item>
        <el-descriptions-item :span="2" label="日志信息">{{ form.jobMessage }}</el-descriptions-item>
        <el-descriptions-item :span="2" label="异常信息" v-if="form.status === StatusEnum.EXCEPTION">{{ form.exceptionInfo }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { JobAuth } from '@auth/system'
import { optionJobApi } from '@/api/system/system/job'
import { cleanJobLogApi, listJobLogApi } from '@/api/system/system/jobLog'
import store from '@/store'
import { JobIndexGo } from '@enums/system'
import { DicStatusEnum, IconEnum } from '@enums'

export default {
  name: 'JobLogManagement',
  /** 字典查询 */
  dicts: ['sys_job_group', 'sys_message_status'],
  data() {
    return {
      //权限标识
      JobAuth: JobAuth,
      // 图标标识
      IconEnum: IconEnum,
      // 状态字典
      StatusEnum: DicStatusEnum,
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      tableList: [],
      // 调度任务选项
      jobIdOptions: [],
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        jobId: undefined,
        jobGroup: undefined,
        status: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `序号列`, visible: true },
        { key: 1, label: `任务名称`, visible: true },
        { key: 2, label: `任务组名`, visible: true },
        { key: 3, label: `调用目标字符串`, visible: true },
        { key: 4, label: `执行状态`, visible: true },
        { key: 5, label: `执行时间`, visible: true },
        { key: 6, label: `日志信息`, visible: true },
        { key: 7, label: `操作列`, visible: true }
      ]
    }
  },
  created() {
    this.getJobOption()
    const jobId = this.$route.params && this.$route.params.id
    if (jobId !== undefined && jobId !== 'undefined') {
      this.queryParams.jobId = jobId
    }
    this.getList()
  },
  methods: {
    /** 查询调度日志列表 */
    getList() {
      this.loading = true
      listJobLogApi(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.tableList = response.data.items
          this.total = response.data.total
          this.loading = false
        }
      )
    },
    /** 获取调度任务选项 */
    getJobOption() {
      optionJobApi().then(response => {
        this.jobIdOptions = response.data.items
      })
    },
    /** 返回操作 */
    handleClose() {
      const route = store.getters.routePath[JobIndexGo]
      this.$tab.closeOpenPage(route)
    },
    /** 搜索操作 */
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    /** 重置操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 详细操作 */
    handleView(row) {
      this.open = true
      this.form = row
    },
    /** 清空操作 */
    handleClean() {
      this.$modal.confirm('是否确认清空所有调度日志？').then(function() {
        return cleanJobLogApi()
      }).then(() => {
        this.$modal.msgSuccess('清空成功!')
        this.getList()
      }).catch(() => {
      })
    }
  }
}
</script>
