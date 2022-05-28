<template>
  <!-- 添加对话框 -->
  <el-dialog :title="title" :visible.sync="open" width="780px" :before-close="handleClose" append-to-body>
    <div>
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="角色信息"/>
        <el-step title="功能权限"/>
        <el-step title="数据权限"/>
      </el-steps>
    </div>

    <div style="height: 500px;margin-top:20px">
      <el-row>
        <el-form ref="roleForm" :model="form" :rules="roleRules" label-width="100px" v-show="active === 0">
          <el-col :span="12">
            <el-form-item label="角色名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入角色名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入角色编码"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio-button
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                  :value="dict.value"
                >{{ dict.label }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="sort">
              <el-input-number v-model="form.sort" :max="65535"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" type="textarea"/>
            </el-form-item>
          </el-col>
        </el-form>

        <el-form ref="authForm" :model="form" :rules="authRules" label-width="100px" v-show="active === 1">
          <el-col :span="24">
            <el-form-item label="菜单分配" prop="authIds">
              <RoleAuthModal ref="authRef" :menuOptions="menuOptions"/>
            </el-form-item>
          </el-col>
        </el-form>

        <el-form ref="dataForm" :model="form" :rules="dataRules" label-width="100px" v-show="active === 2">
          <el-col :span="12">
            <el-form-item label="角色名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入角色名称" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限字符串" prop="roleKey">
              <el-input v-model="form.roleKey" placeholder="请输入权限字符串"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据范围" prop="dataScope">
              <el-select v-model="form.dataScope" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.auth_data_scope"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-show="form.dataScope === DataScopeEnum.CUSTOM">
            <el-form-item label="数据权限" prop="organizeIds">
              <RoleDataModal ref="dataRef" :dataOptions="dataOptions"/>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button style="margin-top: 12px;" @click="handleBack" v-if="active > 0">上一步</el-button>
      <el-button style="margin-top: 12px;" @click="handleNext" v-if="active < 2">下一步</el-button>
      <el-button type="primary" :loading="submitLoading" @click="submitForm" v-if="active === 2">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { DicSortEnum, DicStatusEnum } from '@enums'
import { addRoleApi } from '@/api/system/authority/role'
import { DataScopeEnum } from '@enums/system'
import RoleAuthModal from './RoleAuthModal'
import RoleDataModal from './RoleDataModal'

export default {
  name: 'RoleInitModal',
  components: { RoleDataModal, RoleAuthModal },
  /** 字典查询 */
  dicts: ['sys_normal_disable', 'auth_data_scope'],
  props: {
    // 菜单权限树选项
    menuOptions: {
      type: Array
    },
    // 数据权限树选项
    dataOptions: {
      type: Array
    }
  },
  data() {
    return {
      // 数据范围
      DataScopeEnum: DataScopeEnum,
      // 活跃页
      active: 0,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 提交状态
      submitLoading: false,
      // 表单参数
      form: {},
      // 角色表单校验
      roleRules: {
        name: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '角色编码不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      },
      // 功能权限表单校验
      authRules: {},
      // 数据权限表单校验
      dataRules: {
        roleKey: [
          { required: true, message: '权限字符串不能为空', trigger: 'blur' }
        ],
        dataScope: [
          { required: true, message: '数据范围不能为空', trigger: 'change' }
        ]
      }
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
      this.active = 0
      this.form = {
        id: undefined,
        code: undefined,
        name: undefined,
        roleKey: undefined,
        dataScope: undefined,
        sort: DicSortEnum.ZERO,
        status: DicStatusEnum.NORMAL,
        remark: undefined,
        authIds: [],
        organizeIds: []
      }
      this.resetForm('roleForm')
      this.resetForm('authForm')
      this.resetForm('dataForm')
      this.submitLoading = false
    },
    /** 上一步操作 */
    handleBack() {
      this.active--
    },
    /** 下一步操作 */
    handleNext() {
      if (this.active === 0) {
        this.$refs['roleForm'].validate(valid => {
          if (valid) {
            this.active++
          }
        })
      } else if (this.active === 1) {
        this.$refs['authForm'].validate(valid => {
          if (valid) {
            this.active++
          }
        })
      } else if (this.active === 2) {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            this.active++
          }
        })
      }
    },
    /** 新增操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加角色'
      this.$nextTick(() => {
        this.$refs.authRef.handleTreeNodeAll()
        this.$refs.dataRef.handleTreeNodeAll()
      })
    },
    /** 提交操作 */
    submitForm: function() {
      this.submitLoading = false
      this.form.authIds = this.$refs.authRef.getAllCheckedKeys()
      if (this.form.dataScope === DataScopeEnum.CUSTOM) {
        this.form.organizeIds = this.$refs.dataRef.getAllCheckedKeys()
      }
      this.$refs['roleForm'].validate(roleValid => {
        if (roleValid) {
          this.$refs['authForm'].validate(authValid => {
            if (authValid) {
              this.$refs['dataForm'].validate(dataValid => {
                if (dataValid) {
                  addRoleApi(this.form).then(response => {
                    this.$modal.msgSuccess('修改成功')
                    this.open = false
                    this.$emit('getList')
                  }).catch()
                } else {
                  this.active = 2
                }
              })
            } else {
              this.active = 1
            }
          })
        } else {
          this.active = 0
        }
      })
      this.submitLoading = false
    }
  }
}
</script>

