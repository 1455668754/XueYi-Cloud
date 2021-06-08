<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
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
          v-hasPermi="['tenant:strategy:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['tenant:strategy:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['tenant:strategy:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['tenant:strategy:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="handleSort"
          v-show="sortVisible"
          v-hasPermi="['tenant:strategy:edit']"
        >保存排序</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="strategyList" @selection-change="handleSelectionChange" ref="dataTable" row-key="strategyId">
      <el-table-column type="selection" width="55" align="center" class-name="allowDrag"/>
      <el-table-column label="策略Id" align="center" prop="strategyId" class-name="allowDrag"/>
      <el-table-column label="策略名称" align="center" prop="name" class-name="allowDrag"/>
      <el-table-column label="数据源数量" align="center" prop="amount" class-name="allowDrag"/>
      <el-table-column label="显示顺序" align="center" prop="sort" class-name="allowDrag"/>
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat" class-name="allowDrag"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width allowDrag">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['tenant:strategy:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tenant:strategy:remove']"
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

    <!-- 添加或修改数据源策略对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="策略名称" prop="name">
          <el-input v-model="form.name" type="textarea" placeholder="请输入内容" />
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
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
        </el-form-item>
        <el-divider content-position="center">数据源信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddTenantSource">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteTenantSource">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="tenantSourceList" :row-class-name="rowTenantSourceIndex" @selection-change="handleTenantSourceSelectionChange" ref="tenantSource">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="数据源名称" prop="name">
            <template slot-scope="scope">
              <el-input v-model="scope.row.name" placeholder="请输入数据源名称" />
            </template>
          </el-table-column>
          <el-table-column label="数据源类型" prop="databaseType">
            <template slot-scope="scope">
              <el-input v-model="scope.row.databaseType" placeholder="请输入数据源类型" />
            </template>
          </el-table-column>
          <el-table-column label="数据源编码" prop="slave">
            <template slot-scope="scope">
              <el-input v-model="scope.row.slave" placeholder="请输入数据源编码" />
            </template>
          </el-table-column>
          <el-table-column label="驱动" prop="driverClassName">
            <template slot-scope="scope">
              <el-input v-model="scope.row.driverClassName" placeholder="请输入驱动" />
            </template>
          </el-table-column>
          <el-table-column label="地址" prop="url">
            <template slot-scope="scope">
              <el-input v-model="scope.row.url" placeholder="请输入地址" />
            </template>
          </el-table-column>
          <el-table-column label="用户名" prop="username">
            <template slot-scope="scope">
              <el-input v-model="scope.row.username" placeholder="请输入用户名" />
            </template>
          </el-table-column>
          <el-table-column label="密码" prop="password">
            <template slot-scope="scope">
              <el-input v-model="scope.row.password" placeholder="请输入密码" />
            </template>
          </el-table-column>
          <el-table-column label="读写类型" prop="type">
            <template slot-scope="scope">
              <el-input v-model="scope.row.type" placeholder="请输入读写类型" />
            </template>
          </el-table-column>
          <el-table-column label="显示顺序" prop="sort">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sort" placeholder="请输入显示顺序" />
            </template>
          </el-table-column>
          <el-table-column label="状态" prop="status">
            <template slot-scope="scope">
              <el-input v-model="scope.row.status" placeholder="请输入状态" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStrategy, getStrategy, delStrategy, addStrategy, updateStrategy, updateStrategySort } from "@/api/tenant/strategy";
import Sortable from "sortablejs";

export default {
  name: "Strategy",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      idNames:[],
      // 子表选中数据
      checkedTenantSource: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 数据源策略表格数据
      strategyList: [],
      // 数据源策略表格原始数据
      oldStrategyList: [],
      // 排序保存按钮显示
      sortVisible: false,
      // 数据源表格数据
      tenantSourceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        amount: null,
        sort: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "策略名称不能为空", trigger: "blur" }
        ],
        amount: [
          { required: true, message: "数据源数量不能为空", trigger: "blur" }
        ],
        sort: [
          { required: true, message: "显示顺序不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ],
        delFlag: [
          { required: true, message: "删除标志不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDict();
  },
  methods: {
    /** 查询数据源策略列表 */
    getList() {
      this.loading = true;
      listStrategy(this.queryParams).then(response => {
        this.strategyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询字典信息 */
    getDict(){
      this.getDicts("sys_normal_disable").then(response => {
        this.statusOptions = response.data;
      });
    },
    // 状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        strategyId: null,
        name: null,
        amount: null,
        sort: null,
        status: "0",
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      };
      this.tenantSourceList = [];
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.strategyId)
      this.idNames = selection.map(item => item.strategyId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加数据源策略";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getStrategy({strategyId: row.strategyId}).then(response => {
        this.form = response.data;
        this.tenantSourceList = response.data.tenantSourceList;
        this.open = true;
        this.title = "修改数据源策略";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.tenantSourceList = this.tenantSourceList;
          if (this.form.strategyId != null) {
            updateStrategy(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStrategy(this.form).then(response => {
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
      const strategyIds = row.strategyId || this.ids;
      const names = row.name || this.idNames;
      let that = this;
      this.$confirm('是否确认删除数据源策略"' + names + '"?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delStrategy(that.updateParamIds(strategyIds));
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 数据源序号 */
    rowTenantSourceIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 数据源添加按钮操作 */
    handleAddTenantSource() {
      let obj = {};
      obj.name = "";
      obj.databaseType = "";
      obj.slave = "";
      obj.driverClassName = "";
      obj.url = "";
      obj.username = "";
      obj.password = "";
      obj.type = "";
      obj.sort = "";
      obj.status = "";
      this.tenantSourceList.push(obj);
    },
    /** 数据源删除按钮操作 */
    handleDeleteTenantSource() {
      if (this.checkedTenantSource.length == 0) {
        this.$alert("请先选择要删除的数据源数据", "提示", { confirmButtonText: "确定", });
      } else {
        this.tenantSourceList.splice(this.checkedTenantSource[0].index - 1, 1);
      }
    },
    /** 单选框选中数据 */
    handleTenantSourceSelectionChange(selection) {
      if (selection.length > 1) {
        this.$refs.tenantSource.clearSelection();
        this.$refs.tenantSource.toggleRowSelection(selection.pop());
      } else {
        this.checkedTenantSource = selection;
      }
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('tenant/strategy/export', {
        ...this.queryParams
      }, `tenant_strategy.xlsx`)
    },
    /** 保存排序按钮操作 */
    handleSort(){
      this.$confirm('是否确认保存新排序?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        let params = this.sortOrderListOnlyDynamic(this.strategyList, this.oldStrategyList, "strategyId");
        if(params.length>0) {
          return updateStrategySort(this.updateParamIds(params));
        }
      }).then(() => {
        this.getList();
        this.sortVisible = false;
        this.msgSuccess("保存成功");
      }).catch(() => {});
    }
  },
  mounted() {
    const el = this.$refs.dataTable.$el.querySelectorAll(".el-table__body-wrapper > table > tbody")[0];
    Sortable.create(el, {
      handle: ".allowDrag",
      onEnd: evt => {
        const targetRow = this.strategyList.splice(evt.oldIndex, 1)[0];
        this.strategyList.splice(evt.newIndex, 0, targetRow);
        this.sortVisible = true;
      }
    });
  }
};
</script>
