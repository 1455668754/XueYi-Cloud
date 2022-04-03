<template>
  <!-- 导入表 -->
  <el-dialog title="导入表" :visible.sync="visible" width="800px" top="5vh" append-to-body>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
      <el-form-item label="表名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入表名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表描述" prop="comment">
        <el-input
          v-model="queryParams.comment"
          placeholder="请输入表描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="IconEnum.SEARCH" size="mini" @click="handleQuery">搜索</el-button>
        <el-button :icon="IconEnum.RESET" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row>
      <el-table @row-click="clickRow" ref="table" :data="dbTableList" @selection-change="handleSelectionChange"
                height="260px"
      >
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="name" label="表名称" :show-overflow-tooltip="true"/>
        <el-table-column prop="comment" label="表描述" :show-overflow-tooltip="true"/>
        <el-table-column prop="createTime" label="创建时间"/>
      </el-table>

      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.page"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleImportTable">确 定</el-button>
      <el-button @click="visible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { importDBGenApi, listDBGenApi } from '@/api/gen/generate/gen'
import { IconEnum } from '@enums'

export default {
  data() {
    return {
      // 图标标识
      IconEnum: IconEnum,
      // 遮罩层
      visible: false,
      // 选中数组值
      tables: [],
      // 总条数
      total: 0,
      // 表数据
      dbTableList: [],
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        name: undefined,
        comment: undefined
      }
    }
  },
  methods: {
    // 显示弹框
    show() {
      this.getList()
      this.visible = true
    },
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row)
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.tables = selection.map(item => item.name)
    },
    // 查询表数据
    getList() {
      listDBGenApi(this.queryParams).then(res => {
        this.dbTableList = res.data.items
        this.total = res.data.total
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 导入按钮操作 */
    handleImportTable() {
      if (this.tables.length === 0) {
        this.$modal.msgError('请选择要导入的表')
        return
      }
      importDBGenApi(this.tables).then(res => {
        this.$modal.msgSuccess(res.msg)
        this.visible = false
        this.$emit('ok')
      })
    }
  }
}
</script>
