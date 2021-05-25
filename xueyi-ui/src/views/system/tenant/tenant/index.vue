<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="租户账号" prop="tenantName">
        <el-input
          v-model="queryParams.tenantName"
          placeholder="请输入租户账号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="系统名称" prop="tenantSystemName">
        <el-input
          v-model="queryParams.tenantSystemName"
          placeholder="请输入系统名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="租户名称" prop="tenantNick">
        <el-input
          v-model="queryParams.tenantNick"
          placeholder="请输入租户名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="归属数据库" prop="attributionDatabase">
        <el-select v-model="queryParams.attributionDatabase" placeholder="请选择归属数据库" clearable size="small">
          <el-option
            v-for="dict in attributionDatabaseOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="数据库名称" prop="datasource">
        <el-input
          v-model="queryParams.datasource"
          placeholder="请输入数据库名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:tenant:add']"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tenantList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="租户账号" align="center" prop="tenantName" />
      <el-table-column label="租户名称" align="center" prop="tenantNick" />
      <el-table-column label="租户系统名称" align="center" prop="tenantSystemName" />
<!--      <el-table-column label="租户logo" align="center" prop="logo" />-->
      <el-table-column label="归属数据库" align="center" prop="attributionDatabase" :formatter="attributionDatabaseFormat" />
      <el-table-column label="数据库名称" align="center" prop="datasource" />
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:tenant:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tenant:remove']"
          >删除</el-button>
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

    <!-- 添加或修改租户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="租户账号" prop="tenantName">
          <el-input v-model="form.tenantName" placeholder="请输入租户账号" />
        </el-form-item>
        <el-form-item label="系统名称" prop="tenantSystemName">
          <el-input v-model="form.tenantSystemName" placeholder="请输入系统名称" />
        </el-form-item>
        <el-form-item label="租户名称" prop="tenantNick">
          <el-input v-model="form.tenantNick" placeholder="请输入租户名称" />
        </el-form-item>
<!--        <el-form-item label="租户logo" prop="logo">-->
<!--          <el-input v-model="form.logo" placeholder="请输入租户logo" />-->
<!--        </el-form-item>-->
        <el-form-item label="归属数据库">
          <el-radio-group v-model="form.attributionDatabase">
            <el-radio
              v-for="dict in attributionDatabaseOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="数据库名称" prop="datasource">
          <el-input v-model="form.datasource" placeholder="请输入数据库名称" />
        </el-form-item>
        <el-form-item label="数据源url" prop="datasourceUrl">
          <el-input v-model="form.datasourceUrl" placeholder="请输入数据源url" />
        </el-form-item>
        <el-form-item label="数据源用户名" prop="datasourceUsername">
          <el-input v-model="form.datasourceUsername" placeholder="请输入数据源用户名" />
        </el-form-item>
        <el-form-item label="数据源密码" prop="datasourcePassword">
          <el-input v-model="form.datasourcePassword" placeholder="请输入数据源密码" />
        </el-form-item>
        <el-form-item label="数据源驱动" prop="datasourceDriver">
          <el-input v-model="form.datasourceDriver" placeholder="请输入数据源驱动" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入显示顺序" :min="0" :max="125"/>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTenant, getTenant, delTenant, addTenant, updateTenant } from "@/api/system/tenant";

export default {
  name: "Tenant",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 租户信息表格数据
      tenantList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态字典
      statusOptions: [],
      // 归属数据库字典
      attributionDatabaseOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tenantName: null,
        tenantSystemName: null,
        tenantNick: null,
        attributionDatabase: null,
        datasource: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        tenantName: [
          { required: true, message: "租户账号不能为空", trigger: "blur" }
        ],
        tenantNick: [
          { required: true, message: "租户名称不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDict();
  },
  methods: {
    /** 查询租户信息列表 */
    getList() {
      this.loading = true;
      listTenant(this.queryParams).then(response => {
        this.tenantList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询字典信息 */
    getDict(){
      this.getDicts("sys_normal_disable").then(response => {
        this.statusOptions = response.data;
      });
      this.getDicts("sys_parameter_type").then(response => {
        this.attributionDatabaseOptions = response.data;
      });
    },
    // 状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 归属数据库(字典翻译
    attributionDatabaseFormat(row, column) {
      return this.selectDictLabel(this.attributionDatabaseOptions, row.attributionDatabase);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        tenantId: null,
        tenantName: null,
        tenantSystemName: null,
        tenantNick: null,
        logo: null,
        attributionDatabase: '0',
        datasource: null,
        datasourceUrl: null,
        datasourceUsername: null,
        datasourcePassword: null,
        datasourceDriver: null,
        enterpriseNameFrequency: 0,
        tenantDatabaseStatus: 0,
        sort: 0,
        status: "0",
        remark: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加租户信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getTenant({tenantId: row.tenantId}).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改租户信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.tenantId != null) {
            updateTenant(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTenant(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除租户"' + row.tenantName + '"?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delTenant({tenantId:row.tenantId});
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch((err)=>{})
    }
  }
};
</script>
