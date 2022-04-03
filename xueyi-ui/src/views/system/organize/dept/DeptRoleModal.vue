<template>
  <el-dialog title="角色分配" :visible.sync="open" width="780px" :before-close="handleClose" append-to-body>
    <el-form ref="authForm" :model="form" label-width="100px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="部门名称" prop="name">
            <el-input v-model="form.name" disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="角色分配" prop="authIds">
            <el-transfer v-model="form.roleIds" :props="{ key: 'id', label: 'name'}" :titles="['待分配', '已分配']"
                         :data="roleOptions"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { editAuthDeptScopeApi, getAuthDeptApi } from '@/api/system/organize/dept'

export default {
  name: 'DeptRoleModal',
  props: {
    roleOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      // 是否显示弹出层
      open: false,
      // 提交状态
      submitLoading: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    }
  },
  methods: {
    /** 模态框取消操作 */
    handleClose() {
      this.$modal.confirm('确认关闭？').then(_ => {
        this.cancel()
      }).catch(_ => {
      })
    },
    /** 取消操作 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        roleIds: []
      }
      this.resetForm('form')
      this.submitLoading = false
    },
    /** 分配权限操作 */
    handleAuth(row) {
      this.reset()
      this.form.id = row.id
      this.form.name = row.name
      getAuthDeptApi(row.id).then(response => {
        this.form.roleIds = response.data
        this.open = true
      })
    },
    /** 提交操作 */
    submitForm: function() {
      this.submitLoading = false
      this.$refs['authForm'].validate(valid => {
        if (valid && this.form.id !== undefined) {
          editAuthDeptScopeApi(this.form.id, this.form.roleIds).then(response => {
            this.$modal.msgSuccess('角色分配成功')
            this.open = false
          }).catch()
        }
      })
      this.submitLoading = false
    }
  }
}
</script>
