<template>
  <div class="app-container">
    <div class="wrapper-container" v-show="showSearch">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="部门编码" prop="deptCode">
          <el-input
            v-model="queryParams.deptCode"
            placeholder="请输入部门编码"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input
            v-model="queryParams.deptName"
            placeholder="请输入部门名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="部门状态" clearable size="small">
            <el-option
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
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
            v-hasPermi="['system:dept:add']"
          >新增
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" :sortVisible="false" @queryTable="getList"/>
      </el-row>

      <el-table
        v-loading="loading"
        :data="deptList"
        row-key="deptId"
        default-expand-all
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
        <el-table-column prop="deptName" label="部门名称" min-width="120"/>
        <el-table-column prop="deptCode" label="部门编码" min-width="120"/>
        <el-table-column prop="status" label="状态" min-width="120">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="STATUS.NORMAL"
              :inactive-value="STATUS.DISABLE"
              @change="handleStatusChange(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" min-width="180">
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
            >部门权限
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:dept:edit']"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-plus"
              @click="handleAdd(scope.row)"
              v-hasPermi="['system:dept:add']"
            >新增
            </el-button>
            <el-button
              v-if="scope.row.parentId !== 0"
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:dept:remove']"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-container-supplement"/>
    </div>

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body v-dialogDrag v-dialogDragHeight>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="上级部门" prop="parentId">
              <treeselect v-model="form.parentId" :options="deptOptions" :normalizer="normalizer" placeholder="选择上级部门"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="部门编码" prop="deptCode">
              <el-input v-model="form.deptCode" placeholder="请输入部门编码"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入部门名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入负责人" maxlength="20"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" maxlength="11"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="0" :max="127"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
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

    <!-- 修改部门权限对话框 -->
    <el-dialog :title="title" :visible.sync="roleOpen" width="350px" center append-to-body v-dialogDrag v-dialogDragHeight>
      <el-form ref="form" :model="form" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="部门编码" prop="deptCode">
              <el-input v-model="form.deptCode" readonly/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="部门名称" prop="deptName">
              <el-input v-model="form.deptName" readonly/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="部门权限" prop="roleIds">
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
                  :label="item.name"
                  :value="item.roleId"
                  :disabled="item.status === STATUS.DISABLE">
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
import {
  listDept,
  getDept,
  delDept,
  addDept,
  updateDept,
  listDeptExcludeChild,
  changeDeptRole,
  changeDeptStatus
} from "@/api/system/dept"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import {optionSelect} from "@/api/system/role"
import {STATUS} from "@constant/constants"

export default {
  name: "Dept",
  dicts: ['sys_normal_disable'],
  components: {Treeselect},
  data() {
    return {
      //常量区
      STATUS: STATUS,
      // 遮罩层
      loading: true,
      // 提交状态
      submitLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 表格树数据
      deptList: [],
      // 部门树选项
      deptOptions: [],
      // 权限数据选项
      roleOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层/部门权限设置弹窗
      roleOpen: false,
      // 查询参数
      queryParams: {
        deptCode: undefined,
        deptName: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        parentId: [
          {required: true, message: "上级部门不能为空", trigger: "blur"}
        ],
        deptCode: [
          {required: true, message: "部门编码不能为空", trigger: "blur"}
        ],
        deptName: [
          {required: true, message: "部门名称不能为空", trigger: "blur"}
        ],
        email: [
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phone: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询部门列表 */
    getList() {
      this.loading = true
      listDept(this.queryParams).then(response => {
        this.deptList = response.data
        this.loading = false
      })
    },
    /** 转换部门数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      }
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
        deptId: undefined,
        parentId: undefined,
        deptCode: undefined,
        deptName: undefined,
        sort: 0,
        leader: undefined,
        phone: undefined,
        email: undefined,
        status: STATUS.NORMAL,
        remark: undefined,
        roleIds: []
      }
      this.resetForm("form")
      this.submitLoading = false
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      if (row !== undefined) {
        this.form.parentId = row.deptId
      }
      this.open = true
      this.title = "添加部门"
      listDept().then(response => {
        this.deptOptions = response.data
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      getDept({deptId: row.deptId}).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改部门"
      })
      listDeptExcludeChild({deptId: row.deptId}).then(response => {
        if (JSON.parse(row.parentId) === 0) {
          this.deptOptions = []
          const data = {deptId: '0', deptName: '顶级部门', parentId: '-1', children: []}
          this.deptOptions.push(data)
        } else {
          this.deptOptions = response.data
        }
      })
    },
    /** 部门权限按钮操作 */
    handleRoleUpdate(row) {
      this.reset()
      getDept({deptId: row.deptId}).then(response => {
        this.form = response.data
        this.form.roleIds = Array.from(this.form.roles, x => x.roleId)
        this.roleOpen = true
        this.title = "设置部门权限"
      })
      optionSelect().then(response => {
        this.roleOptions = response.data
      })
    },
    /** 部门状态修改 */
    handleStatusChange(row) {
      let msg = row.status === STATUS.NORMAL ? "启用" : "停用"
      let text = row.status === STATUS.NORMAL ? '启用部门"' + row.deptName + '"吗?' : '停用部门"' + row.deptName + '"吗?停用部门会同步停用所有归属岗位及用户，且归属岗位与用户将无法启用！'
      this.$modal.confirm('确认要' + text).then(function () {
        return changeDeptStatus({deptId: row.deptId, parentId: row.parentId, status: row.status})
      }).then(() => {
        this.$modal.msgSuccess(msg + "成功")
      }).catch(function () {
        row.status = row.status === STATUS.NORMAL ? STATUS.DISABLE : STATUS.NORMAL
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.submitLoading = true
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.deptId !== undefined) {
            updateDept(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            }).catch(() => {
              this.submitLoading = false
            })
          } else {
            addDept(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            }).catch(() => {
              this.submitLoading = false
            })
          }
        }else{
          this.submitLoading = false
        }
      })
    },
    /** 部门权限提交按钮 */
    submitRoleForm: function () {
      this.submitLoading = true
      if (this.form.deptId !== undefined) {
        changeDeptRole(this.form).then(response => {
          this.$modal.msgSuccess("部门权限修改成功")
          this.roleOpen = false
          this.getList()
        }).catch(() => {
          this.submitLoading = false
        })
      }else{
        this.submitLoading = false
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除部门"' + row.deptName + '"?').then(function () {
        return delDept({deptId: row.deptId})
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    }
  }
}
</script>
