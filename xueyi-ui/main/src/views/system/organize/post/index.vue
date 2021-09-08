<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="wrapper-container wrapper-container-supplement">
          <div class="head-container">
            <el-input
              v-model="deptName"
              placeholder="请输入部门名称"
              clearable
              size="small"
              prefix-icon="el-icon-search"
              style="margin-bottom: 20px"
            />
          </div>
          <div class="head-container">
            <el-tree
              :data="deptOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              ref="tree"
              default-expand-all
              @node-click="handleNodeClick"
            />
          </div>
        </div>
      </el-col>

      <!--岗位数据-->
      <el-col :span="20" :xs="24">
        <div class="wrapper-container" v-show="showSearch">
          <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
            <el-form-item label="岗位编码" prop="postCode">
              <el-input
                v-model="queryParams.postCode"
                placeholder="请输入岗位编码"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="岗位名称" prop="postName">
              <el-input
                v-model="queryParams.postName"
                placeholder="请输入岗位名称"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="岗位状态" clearable size="small">
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
        </div>

        <div class="wrapper-container">
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
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleExport"
                v-hasPermi="['system:post:export']"
              >导出
              </el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" :sortVisible="false" @queryTable="getList" :columns="columns"/>
          </el-row>

          <el-table v-loading="loading" :data="postList">
            <el-table-column label="岗位编码" align="center" prop="postCode" key="postCode" v-if="columns[0].visible" min-width="120"/>
            <el-table-column label="岗位名称" align="center" prop="postName" key="postName" v-if="columns[1].visible"
                             :show-overflow-tooltip="true" min-width="120"/>
            <el-table-column label="归属部门" align="center" prop="dept.deptName" key="dept.deptName"
                             v-if="columns[2].visible" :show-overflow-tooltip="true" min-width="120"/>
            <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat" key="status"
                             v-if="columns[3].visible" :show-overflow-tooltip="true" min-width="120">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.status"
                  active-value="0"
                  inactive-value="1"
                  @change="handleStatusChange(scope.row)"
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" min-width="180" key="createTime"
                             v-if="columns[4].visible" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="120">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleRoleUpdate(scope.row)"
                  v-hasPermi="['system:role:set']"
                >岗位权限
                </el-button>
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
            v-show="total>0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getList"
          />
        </div>
      </el-col>
    </el-row>

    <!-- 添加或修改岗位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body v-dialogDrag v-dialogDragHeight>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="归属部门" prop="deptId">
              <treeselect v-model="form.deptId" :options="deptOptions" :show-count="true" :normalizer="normalizer"
                          placeholder="请选择归属部门"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="岗位编码" prop="postCode">
              <el-input v-model="form.postCode" placeholder="请输入岗位编码"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="岗位名称" prop="postName">
              <el-input v-model="form.postName" placeholder="请输入岗位名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="0" :max="127"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="岗位状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 修改岗位权限对话框 -->
    <el-dialog :title="title" :visible.sync="roleOpen" width="350px" center append-to-body v-dialogDrag v-dialogDragHeight>
      <el-form ref="form" :model="form" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="岗位编码" prop="postCode">
              <el-input v-model="form.postCode" readonly/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="岗位名称" prop="postName">
              <el-input v-model="form.postName" readonly/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="岗位权限" prop="roleIds">
              <el-select
                v-model="form.roleIds"
                multiple
                collapse-tags
                style="margin-left: 20px;"
                placeholder="请选择"
                @change="$forceUpdate()">
                <el-option
                  v-for="item in roleOptions"
                  :key="item.roleId"
                  :label="item.roleName"
                  :value="item.roleId"
                  :disabled="item.status === 1">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitRoleForm">确 定</el-button>
        <el-button @click="roleCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listPost, getPost, delPost, addPost, updatePost, changePostRole, changePostStatus} from "@/api/system/post"
import {treeSelect} from "@/api/system/dept"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import {optionSelect} from "@/api/system/role"

export default {
  name: "Post",
  components: {Treeselect},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 提交状态
      submitLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 岗位表格数据
      postList: [],
      // 权限数据选项
      roleOptions: [],
      // 状态数据字典
      statusOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层/部门权限设置弹窗
      roleOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        postCode: undefined,
        postName: undefined,
        status: undefined
      },
      // 列信息
      columns: [
        {key: 0, label: `岗位编码`, visible: true},
        {key: 1, label: `岗位名称`, visible: true},
        {key: 2, label: `归属部门`, visible: true},
        {key: 3, label: `状态`, visible: true},
        {key: 4, label: `创建时间`, visible: true}
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deptId: [
          {required: true, message: "归属部门不能为空", trigger: "blur"}
        ],
        postCode: [
          {required: true, message: "岗位编码不能为空", trigger: "blur"}
        ],
        postName: [
          {required: true, message: "岗位名称不能为空", trigger: "blur"}
        ]
      },
      // 部门树选项
      deptOptions: undefined,
      // 部门名称
      deptName: undefined,
      defaultProps: {
        children: "children",
        label: "label"
      }
    }
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getList()
    this.getTreeSelect()
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data
    })
  },
  methods: {
    /** 查询岗位列表 */
    getList() {
      this.loading = true
      listPost(this.queryParams).then(response => {
        this.postList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询部门下拉树结构 */
    getTreeSelect() {
      treeSelect().then(response => {
        this.deptOptions = response.data
      })
    },
    /** 转换部门数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.label,
        children: node.children,
        isDisabled: node.status === '1'
      }
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id
      this.getList()
    },
    // 岗位状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    roleCancel() {
      this.roleOpen = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        postId: undefined,
        postCode: undefined,
        postName: undefined,
        sort: 0,
        status: "0",
        remark: undefined,
        roleIds: []
      }
      this.resetForm("form")
      this.submitLoading = false
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加岗位"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      getPost({postId: row.postId}).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改岗位"
      })
    },
    /** 岗位权限按钮操作 */
    handleRoleUpdate(row) {
      this.reset()
      getPost({postId: row.postId}).then(response => {
        this.form = response.data
        this.form.roleIds = Array.from(this.form.roles, x => x.roleId)
        this.roleOpen = true
        this.title = "设置岗位权限"
      })
      optionSelect().then(response => {
        this.roleOptions = response.data
      })
    },
    /** 岗位状态修改 */
    handleStatusChange(row) {
      let msg = row.status === "0" ? "启用" : "停用"
      let text = row.status === "0" ? '启用岗位"' + row.postName + '"吗?' : '停用岗位"' + row.postName + '"吗?停用岗位会同步停用所有归属用户，且归属用户将无法启用！'
      this.$confirm('确认要' + text, "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return changePostStatus({postId: row.postId, deptId: row.deptId, status: row.status})
      }).then(() => {
        this.msgSuccess(msg + "成功")
      }).catch(function () {
        row.status = row.status === "0" ? "1" : "0"
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.submitLoading = true
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.postId !== undefined) {
            updatePost(this.form).then(response => {
              this.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPost(this.form).then(response => {
              this.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }else{
          this.submitLoading = false
        }
      })
    },
    /** 岗位权限提交按钮 */
    submitRoleForm: function () {
      this.submitLoading = true
      if (this.form.postId !== undefined) {
        changePostRole(this.form).then(response => {
          this.msgSuccess("岗位权限修改成功")
          this.roleOpen = false
          this.getList()
        })
      }else{
        this.submitLoading = false
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除岗位"' + row.postName + '"?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delPost({postId: row.postId})
      }).then(() => {
        this.getList()
        this.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/post/export', {
        ...this.queryParams
      }, `post_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.wrapper-container-supplement {
  min-height: 330px;
}
</style>
