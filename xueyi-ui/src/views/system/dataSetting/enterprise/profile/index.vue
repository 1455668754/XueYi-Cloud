<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="8" :xs="24">
        <el-card class="box-card" shadow="hover">
          <div>
            <el-button type="primary" icon="el-icon-edit" v-hasPermi="['system:enterprise:edit', 'system:enterpriseAdmin:edit']" circle></el-button>
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
      <el-col :span="8" :xs="24">
        <el-card shadow="hover">
                  <enterpriseInfo :user="enterprise"/>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import enterpriseLogo from "./enterpriseLogo";
import enterpriseInfo from "./enterpriseInfo";
import {getEnterpriseProfile} from "@/api/system/enterprise";

export default {
  name: "Profile",
  components: {enterpriseLogo, enterpriseInfo},
  data() {
    return {
      enterprise: {}
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
    }
  }
};
</script>
