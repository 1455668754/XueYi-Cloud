<template>
  <div class="app-container">
    <div class="wrapper-container" v-show="showSearch">
      <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
        <el-form-item label="登录地址" prop="ipaddr">
          <el-input
            v-model="queryParams.ipaddr"
            placeholder="请输入登录地址"
            clearable
            style="width: 240px;"
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="用户账号" prop="userName">
          <el-input
            v-model="queryParams.userName"
            placeholder="请输入用户账号"
            clearable
            style="width: 240px;"
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="用户名称" prop="userNick">
          <el-input
            v-model="queryParams.userNick"
            placeholder="请输入用户名称"
            clearable
            style="width: 240px;"
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="queryParams.status"
            placeholder="登录状态"
            clearable
            size="small"
            style="width: 240px"
          >
            <el-option
              v-for="dict in dict.type.sys_common_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="登录时间">
          <el-date-picker
            v-model="dateRange"
            size="small"
            style="width: 240px"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
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
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['system:loginInfo:remove']"
          >删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            @click="handleClean"
            v-hasPermi="['system:loginInfo:remove']"
          >清空
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['system:loginInfo:export']"
          >导出
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" :sortVisible="false" @queryTable="getList"/>
      </el-row>

      <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange"
                :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="用户账号 | 用户名称" align="center" :show-overflow-tooltip="true" sortable="custom"
                         :sort-orders="['descending', 'ascending']" min-width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.userName }} | {{ scope.row.userNick }}</span>
          </template>
        </el-table-column>
        <el-table-column label="地址" align="center" prop="ipaddr" width="130" :show-overflow-tooltip="true" min-width="120"/>
        <el-table-column label="状态" align="center" prop="status" min-width="120">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_common_status" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="描述" align="center" prop="msg" min-width="180"/>
        <el-table-column label="访问时间" align="center" prop="accessTime" sortable="custom"
                         :sort-orders="['descending', 'ascending']" min-width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.accessTime) }}</span>
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
  </div>
</template>

<script>
import {list, delLoginInfo, cleanLoginInfo} from "@/api/system/loginInfo"

export default {
  name: "LoginInfo",
  dicts: ['sys_common_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 提交状态
      submitLoading: false,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 日期范围
      dateRange: [],
      // 默认排序
      defaultSort: {prop: 'loginTime', order: 'descending'},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ipaddr: undefined,
        userName: undefined,
        userNick: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true
      list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.list = response.data.items
          this.total = response.data.total
          this.loading = false
        }
      )
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm("queryForm")
      this.$refs.tables.sort(this.defaultSort.prop, this.defaultSort.order)
      this.handleQuery()
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.infoId)
      this.multiple = !selection.length
    },
    /** 排序触发事件 */
    handleSortChange(column, prop, order) {
      this.queryParams.orderByColumn = column.prop
      this.queryParams.isAsc = column.order
      this.getList()
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const infoIds = row.infoId || this.ids
      let $this = this
      this.$modal.confirm('是否确认删除访问编号为"' + infoIds + '"的数据项?').then(function () {
        return delLoginInfo($this.updateParamIds(infoIds))
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal.confirm('是否确认清空所有登录日志?').then(function () {
        return cleanLoginInfo()
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("清空成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/loginInfo/export', {
        ...this.queryParams
      }, `登录日志_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

