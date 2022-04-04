<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模块标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="业务类型" prop="businessType">
        <el-select v-model="queryParams.businessType" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_operate_business"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="方法名称" prop="method">
        <el-input
          v-model="queryParams.method"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="请求方式" prop="requestMethod">
        <el-input
          v-model="queryParams.requestMethod"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作时间">
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
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="[OperateLogAuth.DELETE]"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          :icon="IconEnum.DELETE"
          size="mini"
          @click="handleClean"
          v-hasPermi="[OperateLogAuth.DELETE]"
        >清空
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
      <el-table-column label="模块标题" align="center" prop="title" v-if="columns[2].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="业务类型" align="center" prop="businessType" v-if="columns[3].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_operate_business" :value="scope.row.businessType" />
        </template>
      </el-table-column>
      <el-table-column label="方法名称" align="center" prop="method" v-if="columns[4].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="请求方式" align="center" prop="requestMethod" v-if="columns[5].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="操作类别" align="center" prop="operateType" v-if="columns[6].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_operate_type" :value="scope.row.operateType" />
        </template>
      </el-table-column>
      <el-table-column label="操作人员账号" align="center" prop="userName" v-if="columns[7].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="操作人员名称" align="center" prop="userNick" v-if="columns[8].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="主机地址" align="center" prop="ip" v-if="columns[9].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="操作状态" align="center" prop="status" v-if="columns[10].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_operate_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作时间" align="center" prop="operateTime" v-if="columns[11].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.operateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" v-if="columns[12].visible" class-name="small-padding fixed-width" width="120" fixed="right">
        <template v-slot="scope">
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.VIEW"
            @click="handleUpdate(scope.row)"
            v-hasPermi="[OperateLogAuth.SINGLE]"
          >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.DELETE"
            @click="handleDelete(scope.row)"
            v-hasPermi="[OperateLogAuth.DELETE]"
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

    <!-- 添加或修改公告对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="模块标题">{{ form.title }}</el-descriptions-item>
        <el-descriptions-item label="业务类型">
          <dict-tag :options="dict.type.sys_operate_business" :value="form.businessType" />
        </el-descriptions-item>
        <el-descriptions-item :span="2" label="方法名称">{{ form.method }}</el-descriptions-item>
        <el-descriptions-item label="请求方式">{{ form.requestMethod }}</el-descriptions-item>
        <el-descriptions-item label="操作类别">
          <dict-tag :options="dict.type.sys_operate_type" :value="form.operateType" />
        </el-descriptions-item>
        <el-descriptions-item label="操作人员账号">{{ form.userName }}</el-descriptions-item>
        <el-descriptions-item label="操作人员名称">{{ form.userNick }}</el-descriptions-item>
        <el-descriptions-item label="主机地址">{{ form.ip }}</el-descriptions-item>
        <el-descriptions-item label="操作地点">{{ form.location }}</el-descriptions-item>
        <el-descriptions-item :span="2" label="请求URL">{{ form.url }}</el-descriptions-item>
        <el-descriptions-item label="操作状态">
          <dict-tag :options="dict.type.sys_operate_status" :value="form.status" />
        </el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ form.operateTime }}</el-descriptions-item>
        <el-descriptions-item :span="2" label="错误消息" v-if="form.status === StatusEnum.EXCEPTION">{{ form.errorMsg }}</el-descriptions-item>
        <el-descriptions-item :span="2" label="请求参数">{{ form.param }}</el-descriptions-item>
        <el-descriptions-item :span="2" label="返回参数">{{ form.jsonResult }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { OperateLogAuth } from '@auth/system'
import {
  listOperateLogApi,
  getOperateLogApi,
  delOperateLogApi,
  cleanOperateLogApi
} from '@/api/system/monitor/operateLog'
import { DicStatusEnum, IconEnum } from '@enums'

export default {
  name: 'OperateLogManagement',
  /** 字典查询 */
  dicts: ['sys_operate_type', 'sys_operate_status', 'sys_operate_business'],
  data() {
    return {
      //权限标识
      OperateLogAuth: OperateLogAuth,
      // 图标标识
      IconEnum: IconEnum,
      // 状态字典
      StatusEnum: DicStatusEnum,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中数组名称
      idNames: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      tableList: [],
      // 日期范围
      dateRange: [],
      // 是否显示弹出层
      open: false,
      // 弹出层标题
      title: '',
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        title: undefined,
        businessType: undefined,
        method: undefined,
        requestMethod: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `勾选列`, visible: true },
        { key: 1, label: `序号列`, visible: true },
        { key: 2, label: `模块标题`, visible: true },
        { key: 3, label: `业务类型`, visible: true },
        { key: 4, label: `方法名称`, visible: true },
        { key: 5, label: `请求方式`, visible: true },
        { key: 6, label: `操作类别`, visible: true },
        { key: 7, label: `操作人员账号`, visible: true },
        { key: 8, label: `操作人员名称`, visible: true },
        { key: 9, label: `主机地址`, visible: true },
        { key: 10, label: `操作状态`, visible: true },
        { key: 11, label: `操作时间`, visible: true },
        { key: 12, label: `操作列`, visible: true }
      ],
      // 表单参数
      form: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true
      listOperateLogApi(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.tableList = response.data.items
          this.total = response.data.total
          this.loading = false
        }
      )
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
        title: undefined,
        businessType: undefined,
        method: undefined,
        requestMethod: undefined,
        operateType: undefined,
        userName: undefined,
        userNick: undefined,
        url: undefined,
        ip: undefined,
        param: undefined,
        location: undefined,
        jsonResult: undefined,
        status: undefined,
        errorMsg: undefined,
        operateTime: undefined
      }
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
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.idNames = selection.map(item => item.title)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 修改操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getOperateLogApi(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '查看操作日志'
      })
    },
    /** 删除操作 */
    handleDelete(row) {
      const delIds = row.id || this.ids
      const delNames = row.title || this.idNames
      this.$modal.confirm('是否确定要删除' + delNames + '？').then(function() {
        return delOperateLogApi(delIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功！')
      }).catch(() => {
      })
    },
    /** 清空操作 */
    handleClean() {
      this.$modal.confirm('是否确认清空所有操作日志？').then(function() {
        return cleanOperateLogApi()
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('清空成功！')
      }).catch(() => {
      })
    }
  }
}
</script>

