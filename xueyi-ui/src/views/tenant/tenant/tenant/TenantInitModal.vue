<template>
  <!-- 添加对话框 -->
  <el-dialog :title="title" :visible.sync="open" width="780px" :before-close="handleClose" append-to-body>
    <div>
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="策略源"/>
        <el-step title="租户信息"/>
        <el-step title="租户数据"/>
        <el-step title="租户权限"/>
      </el-steps>
    </div>
    <div style="height: 500px;margin-top:20px">
      <el-form ref="strategyForm" :model="tenant" :rules="strategyRules" label-width="100px" v-show="active === 0">
        <el-row>
          <el-col :span="24">
            <el-form-item label="源策略" prop="strategyId">
              <el-select v-model="tenant.strategyId" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in strategyOptions"
                  :key="dict.id"
                  :label="dict.name"
                  :value="dict.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-row>
        <el-form ref="tenantForm" :model="tenant" :rules="tenantRules" label-width="100px" v-show="active === 1">
          <el-col :span="12">
            <el-form-item label="租户名称" prop="nick">
              <el-input v-model="tenant.nick" placeholder="请输入租户名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租户账号" prop="name">
              <el-input v-model="tenant.name" placeholder="请输入租户账号"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="系统名称" prop="systemName">
              <el-input v-model="tenant.systemName" placeholder="请输入系统名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账号修改次数" prop="nameFrequency">
              <el-input-number v-model="tenant.nameFrequency" :max="65535"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="租户logo" prop="logo">
              <image-upload v-model="tenant.logo" :limit="1" :isShowTip="false"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="超管租户" prop="isLessor">
              <el-radio-group v-model="tenant.isLessor">
                <el-radio-button
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.value"
                  :value="dict.value"
                >{{ dict.label }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="tenant.status">
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
              <el-input-number v-model="tenant.sort" :max="65535"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="tenant.remark" placeholder="请输入备注" type="textarea"/>
            </el-form-item>
          </el-col>
        </el-form>

        <el-form ref="deptForm" :model="dept" :rules="deptRules" label-width="100px" v-show="active === 2">
          <el-col :span="24">
            <el-divider>组织信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="name">
              <el-input v-model="dept.name" placeholder="请输入部门名称"/>
            </el-form-item>
          </el-col>
        </el-form>

        <el-form ref="postForm" :model="post" :rules="postRules" label-width="100px" v-show="active === 2">
          <el-col :span="12">
            <el-form-item label="岗位名称" prop="name">
              <el-input v-model="post.name" placeholder="请输入岗位名称"/>
            </el-form-item>
          </el-col>
        </el-form>

        <el-form ref="userForm" :model="user" :rules="userRules" label-width="100px" v-show="active === 2">
          <el-col :span="24">
            <el-divider>管理员信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账号" prop="userName">
              <el-input v-model="user.userName" placeholder="请输入账号"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="user.nickName" placeholder="请输入昵称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="user.password" placeholder="请输入密码" show-password/>
            </el-form-item>
          </el-col>
        </el-form>

        <el-form ref="authForm" :model="form" label-width="100px" v-show="active === 3">
          <el-col :span="24">
            <el-form-item label="菜单分配" prop="authIds">
              <TenantAuthModal ref="authRef" :menuOptions="menuOptions"/>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button style="margin-top: 12px;" @click="handleBack" v-if="active > 0">上一步</el-button>
      <el-button style="margin-top: 12px;" @click="handleNext" v-if="active < 3">下一步</el-button>
      <el-button type="primary" :loading="submitLoading" @click="submitForm" v-if="active === 3">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addTenantApi } from '@/api/tenant/tenant/tenant'
import { DicSortEnum, DicStatusEnum, DicYesNoEnum } from '@enums'
import TenantAuthModal from './TenantAuthModal'

export default {
  name: 'TenantInitModal',
  components: { TenantAuthModal },
  /** 字典查询 */
  dicts: ['sys_yes_no', 'sys_normal_disable'],
  props: {
    // 源策略选项
    strategyOptions: {
      type: Array
    },
    // 权限树选项
    menuOptions: {
      type: Array
    }
  },
  data() {
    return {
      // 活跃页
      active: 0,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 提交状态
      submitLoading: false,
      // 租户表单参数
      tenant: {},
      // 部门表单参数
      dept: {},
      // 岗位表单参数
      post: {},
      // 用户表单参数
      user: {},
      // 表单参数
      form: {},
      // 源策略表单校验
      strategyRules: {
        strategyId: [
          { required: true, message: '策略Id不能为空', trigger: 'change' }
        ]
      },
      // 租户表单校验
      tenantRules: {
        name: [
          { required: true, message: '租户账号不能为空', trigger: 'blur' }
        ],
        nick: [
          { required: true, message: '租户名称不能为空', trigger: 'blur' }
        ],
        systemName: [
          { required: true, message: '系统名称不能为空', trigger: 'blur' }
        ],
        nameFrequency: [
          { required: true, message: '账号修改次数不能为空', trigger: 'blur' }
        ],
        isLessor: [
          { required: true, message: '超管租户不能为空', trigger: 'change' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      },
      // 部门表单校验
      deptRules: {
        name: [
          { required: true, message: '部门名称不能为空', trigger: 'blur' }
        ]
      },
      // 岗位表单校验
      postRules: {
        name: [
          { required: true, message: '岗位名称不能为空', trigger: 'blur' }
        ]
      },
      // 用户表单校验
      userRules: {
        userName: [
          { required: true, message: '账号不能为空', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: '昵称不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' }
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
      this.tenant = {
        id: undefined,
        strategyId: undefined,
        name: undefined,
        systemName: undefined,
        nick: undefined,
        logo: undefined,
        nameFrequency: DicSortEnum.FIVE,
        isLessor: DicYesNoEnum.NO,
        sort: DicSortEnum.ZERO,
        status: DicStatusEnum.NORMAL,
        remark: undefined
      }
      this.dept = { name: undefined }
      this.form = { tenant: {}, dept: {}, post: {}, user: {}, authIds: [] }
      this.post = { name: undefined }
      this.user = { userName: undefined, nickName: undefined, password: undefined }
      this.resetForm('strategyForm')
      this.resetForm('tenantForm')
      this.resetForm('deptForm')
      this.resetForm('postForm')
      this.resetForm('userForm')
      this.submitLoading = false
    },
    /** 上一步操作 */
    handleBack() {
      this.active--
    },
    /** 下一步操作 */
    handleNext() {
      if (this.active === 0) {
        this.$refs['strategyForm'].validate(strategyValid => {
          if (strategyValid) {
            this.active++
          }
        })
      } else if (this.active === 1) {
        this.$refs['tenantForm'].validate(strategyValid => {
          if (strategyValid) {
            this.form.tenant = this.tenant
            this.active++
          }
        })
      } else if (this.active === 2) {
        this.$refs['deptForm'].validate(deptValid => {
          this.$refs['postForm'].validate(postValid => {
            this.$refs['userForm'].validate(userValid => {
              if (deptValid && postValid && userValid) {
                this.form.dept = this.dept
                this.form.post = this.post
                this.form.user = this.user
                this.active++
              }
            })
          })
        })
      }
    },
    /** 新增操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加租户'
      this.$nextTick(() => {
        this.$refs.authRef.handleTreeNodeAll()
      })
    },
    /** 提交操作 */
    submitForm: function() {
      this.submitLoading = false
      this.form.authIds = this.$refs.authRef.getAllCheckedKeys()
      this.$refs['strategyForm'].validate(strategyValid => {
        if (strategyValid) {
          this.$refs['tenantForm'].validate(tenantValid => {
            if (tenantValid) {
              this.$refs['deptForm'].validate(deptValid => {
                this.$refs['postForm'].validate(postValid => {
                  this.$refs['userForm'].validate(userValid => {
                    if (deptValid && postValid && userValid) {
                      addTenantApi(this.form).then(response => {
                        this.$modal.msgSuccess('修改成功')
                        this.open = false
                        this.$emit('getList')
                      }).catch()
                    } else {
                      this.active = 2
                    }
                  })
                })
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

