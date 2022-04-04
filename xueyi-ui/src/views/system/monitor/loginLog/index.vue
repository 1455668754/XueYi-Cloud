<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户账号" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="userNick">
        <el-input
          v-model="queryParams.userNick"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录IP" prop="ipaddr">
        <el-input
          v-model="queryParams.ipaddr"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_message_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="登录时间">
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
          v-hasPermi="[LoginLogAuth.DELETE]"
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
          v-hasPermi="[LoginLogAuth.DELETE]"
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
      <el-table-column label="用户账号" align="center" prop="userName" v-if="columns[2].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="用户名称" align="center" prop="userNick" v-if="columns[3].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="登录IP地址" align="center" prop="ipaddr" v-if="columns[4].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="登录状态" align="center" prop="status" v-if="columns[5].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_message_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="提示信息" align="center" prop="msg" v-if="columns[6].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="访问时间" align="center" prop="accessTime" v-if="columns[7].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.accessTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" v-if="columns[8].visible" class-name="small-padding fixed-width" width="120" fixed="right">
        <template v-slot="scope">
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.DELETE"
            @click="handleDelete(scope.row)"
            v-hasPermi="[LoginLogAuth.DELETE]"
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
  </div>
</template>

<script>
import { LoginLogAuth } from '@auth/system'
import { cleanLoginLogApi, delLoginLogApi, listLoginLogApi } from '@/api/system/monitor/loginLog'
import { IconEnum } from '@enums'

export default {
  name: 'LogManagement',
  /** 字典查询 */
  dicts: ['sys_message_status'],
  data() {
    return {
      //权限标识
      LoginLogAuth: LoginLogAuth,
      // 图标标识
      IconEnum: IconEnum,
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
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        userName: undefined,
        userNick: undefined,
        ipaddr: undefined,
        status: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `勾选列`, visible: true },
        { key: 1, label: `序号列`, visible: true },
        { key: 2, label: `用户账号`, visible: true },
        { key: 3, label: `用户名称`, visible: true },
        { key: 4, label: `登录IP地址`, visible: true },
        { key: 5, label: `登录状态`, visible: true },
        { key: 6, label: `提示信息`, visible: true },
        { key: 7, label: `访问时间`, visible: true },
        { key: 8, label: `操作列`, visible: true }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true
      listLoginLogApi(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.tableList = response.data.items
          this.total = response.data.total
          this.loading = false
        }
      )
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
      this.idNames = selection.map(item => item.userName)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 删除操作 */
    handleDelete(row) {
      const delIds = row.id || this.ids
      const delNames = row.userName || this.idNames
      this.$modal.confirm('是否确定要删除' + delNames + '的登录记录？').then(function() {
        return delLoginLogApi(delIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功！')
      }).catch(() => {
      })
    },
    /** 清空操作 */
    handleClean() {
      this.$modal.confirm('是否确认清空所有登录日志？').then(function() {
        return cleanLoginLogApi()
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('清空成功！')
      }).catch(() => {
      })
    }
  }
}
</script>

