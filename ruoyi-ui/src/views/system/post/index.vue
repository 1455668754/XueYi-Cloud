<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="treeStructure.deptName"
            placeholder="请输入部门名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="treeStructure.deptOptions"
            :props="treeStructure.defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="search.queryParams" ref="queryForm" :inline="true" v-show="search.showSearch"
                 label-width="68px">
          <el-form-item label="岗位编码" prop="postCode">
            <el-input
              v-model="search.queryParams.postCode"
              placeholder="请输入岗位编码"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="岗位名称" prop="postName">
            <el-input
              v-model="search.queryParams.postName"
              placeholder="请输入岗位名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="search.queryParams.status" placeholder="岗位状态" clearable size="small">
              <el-option
                v-for="dict in option.statusOptions"
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
              v-hasPermi="['system:post:add']"
            >新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="search.single"
              @click="handleUpdate"
              v-hasPermi="['system:post:edit']"
            >修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="search.multiple"
              @click="handleDelete"
              v-hasPermi="['system:post:remove']"
            >删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['system:post:export']"
            >导出
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="search.showSearch" @queryTable="getList" :columns="search.columns"/>
        </el-row>

        <el-table v-loading="table.loading" :data="table.list" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column label="岗位编号" align="center">
            <template slot-scope="scope">
              {{ search.queryParams.pageSize * (search.queryParams.pageNum - 1) + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="岗位编码" align="center" prop="postCode" key="postCode" v-if="search.columns[0].visible"
                           :show-overflow-tooltip="true"/>
          <el-table-column label="岗位名称" align="center" prop="postName" key="postName" v-if="search.columns[1].visible"
                           :show-overflow-tooltip="true"/>
          <el-table-column label="部门" align="center" prop="dept.deptName" key="deptName"
                           v-if="search.columns[2].visible"
                           :show-overflow-tooltip="true"/>
          <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat" key="status"
                           v-if="search.columns[3].visible" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间" align="center" prop="createTime" width="180" key="createTime"
                           v-if="search.columns[4].visible" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:post:edit']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:post:remove']"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="table.total>0"
          :total="table.total"
          :page.sync="search.queryParams.pageNum"
          :limit.sync="search.queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- 添加或修改岗位对话框 -->
    <el-dialog :title="form.title" :visible.sync="form.open" width="600px" append-to-body>
      <el-form ref="form" :model="form.form" :rules="form.rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="归属部门" prop="deptId">
              <treeselect v-model="form.form.deptId" :options="treeStructure.deptOptions" :show-count="true"
                          placeholder="请选择归属部门"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="岗位编码" prop="postCode">
              <el-input v-model="form.form.postCode" placeholder="请输入岗位编码" :readonly="form.postId !== undefined"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="岗位名称" prop="postName">
              <el-input v-model="form.form.postName" placeholder="请输入岗位名称"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="岗位顺序" prop="postSort">
              <el-input-number v-model="form.form.postSort" controls-position="right" :min="0"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="岗位状态" prop="status">
              <el-radio-group v-model="form.form.status">
                <el-radio
                  v-for="dict in option.statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <editor v-model="form.form.remark" :min-height="192"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listPost, getPost, delPost, addPost, updatePost} from "@/api/system/post";
import {treeselect} from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import Editor from '@/components/Editor';

export default {
  name: "Post",
  components: {Treeselect, Editor},
  data() {
    return {
      //表格
      table: {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 总条数
        total: 0,
        // 表格数据
        list: [],
      },
      // 操作栏
      search: {
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 显隐列信息
        columns: [
          {key: 0, label: `岗位编码`, visible: true},
          {key: 1, label: `岗位名称`, visible: true},
          {key: 2, label: `部门`, visible: true},
          {key: 3, label: `状态`, visible: true},
          {key: 4, label: `创建时间`, visible: true},
        ],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          deptId: undefined,
          postCode: undefined,
          postName: undefined,
          status: undefined
        },
      },
      // 选项
      option: {
        // 状态数据字典
        statusOptions: [],
      },
      //树结构
      treeStructure: {
        // 部门名称
        deptName: undefined,
        // 部门树选项
        deptOptions: undefined,
        //树组织
        defaultProps: {
          children: "children",
          label: "label"
        },
      },
      //表单
      form: {
        // 表单参数
        form: {},
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 表单校验
        rules: {
          deptId: [
            {required: true, message: "部门不能为空", trigger: "change"}
          ],
          postCode: [
            {required: true, message: "岗位编码不能为空", trigger: "blur"}
          ],
          postName: [
            {required: true, message: "岗位名称不能为空", trigger: "blur"}
          ],
          postSort: [
            {required: true, message: "岗位顺序不能为空", trigger: "blur"}
          ]
        }
      }
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.getTreeselect();
    this.getDicts("sys_normal_disable").then(response => {
      this.option.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询岗位列表 */
    getList() {
      this.table.loading = true;
      listPost(this.search.queryParams).then(response => {
        this.table.list = response.rows;
        this.table.total = response.total;
        this.table.loading = false;
      });
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then(response => {
        this.treeStructure.deptOptions = response.data;
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.search.queryParams.deptId = data.id;
      this.getList();
    },
    // 岗位状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.option.statusOptions, row.status);
    },
    // 取消按钮
    cancel() {
      this.form.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form.form = {
        postId: undefined,
        postCode: undefined,
        postName: undefined,
        postSort: 0,
        status: 0,
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.search.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.table.ids = selection.map(item => item.postId)
      this.search.single = selection.length !== 1
      this.search.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      this.form.open = true;
      this.form.title = "添加岗位";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      const postId = row.postId || this.table.ids
      getPost(postId).then(response => {
        this.form.form = response.data;
        this.form.open = true;
        this.form.title = "修改岗位";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.form.postId !== undefined) {
            updatePost(this.form.form).then(response => {
              this.msgSuccess("修改成功");
              this.form.open = false;
              this.getList();
            });
          } else {
            addPost(this.form.form).then(response => {
              this.msgSuccess("新增成功");
              this.form.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const postIds = row.postId || this.table.ids;
      this.$confirm('是否确认删除选中的岗位?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delPost(postIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/post/export', {
        ...this.search.queryParams
      }, `post_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
