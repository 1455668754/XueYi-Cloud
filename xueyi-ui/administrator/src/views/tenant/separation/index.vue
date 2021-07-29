<template>
  <div class="app-container">
    <div class="wrapper-container">
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="100px">
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
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="wrapper-container">

      <el-table v-loading="loading" :data="separationList">
        <el-table-column label="编号" align="center" prop="sourceId">
          <template slot-scope="scope">
            <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + 1 + scope.$index }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据源名称" align="center" prop="name"/>
        <el-table-column label="数据源编码" align="center" prop="slave">
          <template slot-scope="scope">
            <el-tag type="info">{{ scope.row.slave }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="读写类型" align="center" prop="type">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === '0'">读&写</el-tag>
            <el-tag type="success" v-else-if="scope.row.type === '1'">只读</el-tag>
            <el-tag type="warning" v-else-if="scope.row.type === '2'">只写</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="数据源类型" align="center" prop="databaseType">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.databaseType === '0'">从数据源</el-tag>
            <el-tag type="danger" v-else-if="scope.row.databaseType === '1'">主数据源</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['tenant:separation:edit']"
            >配置
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 添加或修改数据源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="写数据源" prop="name">
          <el-input v-model="form.name" placeholder="请输入写数据源" readonly/>
        </el-form-item>
        <el-divider content-position="center">读数据源信息</el-divider>
        <div class="value-set">
          <div class="value-title">读数据源：</div>
          <div class="value-add">
            <el-button type="primary" plain @click="valueAdd">添加读数据源</el-button>
          </div>
          <el-table :data="form.values">
            <el-table-column label="读数据源" min-width="30%" align="center">
              <template slot-scope="scope">
                <el-select v-model="scope.row.sourceId" :disabled="scope.row.sourceId === form.sourceId"
                           placeholder="请选择">
                  <el-option
                    v-for="item in containReadList"
                    :key="item.sourceId"
                    :label="item.name"
                    :value="item.sourceId"
                    :disabled="item.sourceId === form.sourceId">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="30%" align="center">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="valueDelete(scope.row)"
                  v-hasPermi="['tenant:separation:edit']"
                  v-if="scope.row.type !== '0'"
                >删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listSeparation, getSeparation, updateSeparation, readSeparation} from '@/api/tenant/separation'

export default {
  name: 'Separation',
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 数据源表格数据
      separationList: [],
      // 具备读 数据源集合
      containReadList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 数据源类型字典
      databaseTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        slave: null,
        databaseType: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
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
      listSeparation(this.queryParams).then(response => {
        this.separationList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询字典信息 */
    getDict() {
      this.getDicts('sys_tenant_resource_type').then(response => {
        this.databaseTypeOptions = response.data
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        sourceId: null,
        name: null,
        databaseType: null,
        type: null,
        status: null,
        values: []
      }
      this.resetForm('form')
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
    /** 配置按钮操作 */
    handleUpdate(row) {
      this.reset()
      readSeparation({sourceId: row.sourceId, type: row.type}).then(response => {
        this.containReadList = response.data
      })
      getSeparation({sourceId: row.sourceId}).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改数据源'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.sourceId != null) {
            if (this.valueCheck()) {
              updateSeparation(this.form).then(response => {
                this.msgSuccess('配置成功')
                this.open = false
                this.getList()
              })
            }
          }
        }
      })
    },
    valueAdd() {
      const newData = {
        sourceId: ''
      }
      this.form.values.push(newData)
    },
    valueDelete(row) {
      if (row !== undefined) {
        const index = this.form.values.indexOf(row)
        this.form.values.splice(index, 1)
      }
    },
    valueCheck() {
      for (let i = 0; i < this.form.values.length; i++) {
        for (let j = 0; j < this.form.values.length; j++) {
          if (this.form.values[i].sourceId === null || this.form.values[i].sourceId === '') {
            this.form.values.splice(i--, 1)
          }
          if (i !== j && this.form.values[i].sourceId === this.form.values[j].sourceId) {
            this.form.values.splice(j--, 1)
          }
        }
        if (this.form.values.length === 0) {
          this.$message({
            message: '必须配置读数据源，请添加',
            type: 'warning'
          })
          return false
        }
      }
      return true
    }
  }
}
</script>

<style lang="scss" scoped>
.value-set {
  .value-title {
    float: left;
    line-height: 40px;
  }

  .value-add {
    float: right;
    margin-bottom: 10px;
  }
}

.value-input {
  text-align: center
}
</style>
