<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="8" :xs="24">
        <el-card class="box-card" shadow="hover">
          <div>
            <el-button type="primary" icon="el-icon-edit"
                       v-hasPermi="['system:enterprise:edit', 'system:enterpriseAdmin:edit']" @click="changeVisible"
                       circle></el-button>
            <div class="text-center avatar-group">
              <enterpriseLogo :enterprise="enterprise"/>
              <div class="avatar-name">{{ enterprise.enterpriseNick }}</div>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="xy_user"/>
                <span class="pull-left">企业账号</span>
                <span class="pull-right">{{ enterprise.enterpriseName }}</span>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="xy_sex"/>
                <span class="pull-left">系统名称</span>
                <span class="pull-right">{{ enterprise.enterpriseSystemName }}</span>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="xy_dept_profile"/>
                <span class="pull-left">企业名称</span>
                <span class="pull-right">{{ enterprise.enterpriseNick }}</span>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="xy_creatTime"/>
                <span class="pull-left">创建日期</span>
                <span class="pull-right">{{ enterprise.createTime }}</span>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" :xs="24" v-if="visible">
        <el-card shadow="hover">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="企业账号" prop="enterpriseName">
              <el-row>
                <el-col :span="12">
                  <el-input v-model="form.enterpriseName" maxlength="20" show-word-limit :disabled="status"/>
                </el-col>
                <el-col :span="12" v-if="editTimes-enterprise.enterpriseNameFrequency>0"
                        v-hasPermi="['system:enterpriseAdmin:edit']">
                  <el-popover
                    placement="top-start"
                    trigger="hover"
                    :content="'还有'+(editTimes-enterprise.enterpriseNameFrequency)+'次修改次数'">
                    <svg-icon class="name-edit" icon-class="xy_edit" slot="reference" @click="editNameClick"/>
                  </el-popover>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item label="企业名称" prop="enterpriseNick">
              <el-row>
                <el-col :span="12">
                  <el-input v-model="form.enterpriseNick" maxlength="50" show-word-limit/>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item label="系统名称" prop="enterpriseSystemName">
              <el-row>
                <el-col :span="12">
                  <el-input v-model="form.enterpriseSystemName" maxlength="50" show-word-limit/>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="submit" :loading="loading">保存</el-button>
              <el-button type="danger" size="mini" @click="close">关闭</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import enterpriseLogo from "./enterpriseLogo";
import {changeEnterpriseName, updateEnterprise} from "@/api/system/enterprise";
import { getEnterpriseProfile } from '@/api/login';
import store from "@/store";

export default {
  name: "Profile",
  components: {enterpriseLogo},
  data() {
    return {
      enterprise: {},
      visible: false,
      loading: false,
      editTimes: 2,//企业账号最多编辑次数
      status: true,//企业账号编辑状态
      enterpriseName: store.getters.enterpriseName,
      form: {},
      // 表单校验
      rules: {
        enterpriseName: [
          {required: true, message: "企业账号", trigger: "blur"}
        ],
        enterpriseNick: [
          {required: true, message: "企业名称", trigger: "blur"}
        ],
        enterpriseSystemName: [
          {required: true, message: "系统名称", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getEnterprise();
  },
  methods: {
    getEnterprise() {
      getEnterpriseProfile().then(response => {
        this.enterprise = response.data;
      });
    },
    // 表单重置
    reset() {
      this.form = {
        enterpriseName: undefined,
        enterpriseSystemName: undefined,
        enterpriseNick: undefined
      };
      this.resetForm("form");
    },
    changeVisible() {
      this.getEnterprise();
      this.visible = !this.visible;
      if (this.visible) {
        this.form = this.enterprise;
      } else {
        this.reset();
      }
    },
    editNameClick() {
      this.status = !this.status
    },
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.status === false && this.form.enterpriseName !== this.enterpriseName) {
            changeEnterpriseName(this.form).then(response => {
              this.msgSuccess("企业账号修改成功");
            });

          }
          updateEnterprise(this.form).then(response => {
            this.msgSuccess("企业资料修改成功");
          });
          store.commit('SET_ENTERPRISENAME', this.form.enterpriseName);
          this.enterpriseName = this.form.enterpriseName;
          store.commit('SET_ENTERPRISESYSTEMNAME', this.form.enterpriseSystemName);
        }
        this.getEnterprise();
        this.status = true;
        this.loading = false;
      });
    },
    close() {
      this.reset();
      this.visible = false;
      this.status = true;
    }
  }
};
</script>

<style lang="scss" scoped>
.name-edit {
  margin-left: 10px;
}

.name-edit:hover {
  cursor: pointer
}
</style>
