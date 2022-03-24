<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="表名称" prop="tableName">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入表名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表描述" prop="tableComment">
        <el-input
          v-model="queryParams.comment"
          placeholder="请输入表描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
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
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-download"
          size="mini"
          :disabled="single"
          @click="handleGenTable"
          v-hasPermi="['generate:gen:code']"
        >生成
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload"
          size="mini"
          @click="openImportTable"
          v-hasPermi="['generate:gen:import']"
        >导入
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleEditTable"
          v-hasPermi="['generate:gen:edit']"
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
          v-hasPermi="['generate:gen:delete']"
        >删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <el-table v-loading="loading" :data="tableList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="55"></el-table-column>
      <el-table-column label="序号" type="index" width="50" align="center">
        <template v-slot="scope">
          <span>{{ (queryParams.page - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="表名称"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
        width="120"
      />
      <el-table-column
        label="表描述"
        align="center"
        prop="comment"
        :show-overflow-tooltip="true"
        width="120"
      />
      <el-table-column
        label="实体"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
        width="120"
      />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160"/>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="160"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button
            type="text"
            size="small"
            icon="el-icon-view"
            @click="handlePreview(scope.row)"
            v-hasPermi="['generate:gen:preview']"
          >预览
          </el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleEditTable(scope.row)"
            v-hasPermi="['generate:gen:edit']"
          >编辑
          </el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['generate:gen:delete']"
          >删除
          </el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-download"
            @click="handleGenTable(scope.row)"
            v-hasPermi="['generate:gen:code']"
          >生成代码
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
    <!-- 预览界面 -->
    <el-dialog :title="preview.title" :visible.sync="preview.open" width="80%" top="5vh" append-to-body
               class="scrollbar"
    >
      <el-tabs v-model="preview.activeName">
        <el-tab-pane
          v-for="(item, key) in preview.data"
          :label="item.name"
          :name="item.name"
          :key="key"
        >
          <el-link :underline="false" icon="el-icon-document-copy" v-clipboard:copy="item.template"
                   v-clipboard:success="clipboardSuccess" style="float:right"
          >复制
          </el-link>
          <pre><code class="hljs" v-html="highlightedCode(item)"></code></pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <import-table ref="import" @ok="handleQuery"/>
  </div>
</template>

<script>
import importTable from './importTable'
import hljs from 'highlight.js/lib/highlight'
import 'highlight.js/styles/github-gist.css'
import { delForceGenApi, generateGenApi, listGenApi, previewGenApi } from '@/api/gen/generate/gen'
import { GenerationModeEnum, GenGenerateDetailGo } from '@enums/gen'
import store from '@/store'

hljs.registerLanguage('java', require('highlight.js/lib/languages/java'))
hljs.registerLanguage('xml', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('html', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('vue', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('js', require('highlight.js/lib/languages/javascript'))
hljs.registerLanguage('ts', require('highlight.js/lib/languages/typescript'))
hljs.registerLanguage('sql', require('highlight.js/lib/languages/sql'))

export default {
  name: 'Gen',
  components: { importTable },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 唯一标识符
      uniqueId: '',
      // 选中数组
      ids: [],
      // 选中表数组
      names: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表数据
      tableList: [],
      // 日期范围
      dateRange: '',
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        name: undefined,
        comment: undefined
      },
      // 预览参数
      preview: {
        open: false,
        title: '代码预览',
        data: {},
        activeName: 'domain.java'
      }
    }
  },
  created() {
    this.getList()
  },
  activated() {
    const time = this.$route.query.t
    if (time != null && time !== this.uniqueId) {
      this.uniqueId = time
      this.queryParams.page = Number(this.$route.query.page)
      this.getList()
    }
  },
  methods: {
    /** 查询表集合 */
    getList() {
      this.loading = true
      listGenApi(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.tableList = response.data.items
          this.total = response.data.total
          this.loading = false
        }
      )
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    /** 打开导入表弹窗 */
    openImportTable() {
      this.$refs.import.show()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 预览按钮 */
    handlePreview(row) {
      previewGenApi(row.id).then(response => {
        this.preview.data = response.data
        this.preview.open = true
        this.preview.activeName = 'dto.java'
      })
    },
    /** 高亮显示 */
    highlightedCode(item) {
      const result = hljs.highlight(item.language, item.template || '', true)
      return result.value || '&nbsp;'
    },
    /** 复制代码成功 */
    clipboardSuccess() {
      this.$modal.msgSuccess('复制成功')
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.names = selection.map(item => item.name)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleEditTable(row) {
      const tableId = row.id || this.ids[0]
      const route = store.getters.routePath[GenGenerateDetailGo]
      this.$router.push(route + tableId)
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      const names = row.name || this.names
      this.$modal.confirm('是否确认删除表"' + names + '"？').then(function() {
        return delForceGenApi(ids.toString())
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    /** 生成代码操作 */
    handleGenTable(row) {
      if (row.genType === GenerationModeEnum.CUSTOM) {
        generateGenApi(row.id).then(() => {
          this.$modal.msgSuccess('成功生成到自定义路径：\n后端：' + row.genPath+'\n前端：'+row.uiPath)
        })
      } else {
        this.$download.zip('/code/gen/download/' + row.id, 'xueyi')
      }
    },
  }
}

</script>
