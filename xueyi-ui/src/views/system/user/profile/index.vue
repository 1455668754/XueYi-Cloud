<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="8" :xs="24">
        <el-card class="box-card" shadow="hover">
          <div>
            <div class="text-center avatar-group">
              <userAvatar :user="user"/>
              <div class="avatar-name">{{ user.nickName }}</div>
              <div>{{ user.profile }}</div>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="xy_user"/>
                <span class="pull-left">用户账号</span>
                <span class="pull-right">{{ user.userName }}</span>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="xy_sex"/>
                <span class="pull-left">性别</span>
                <span class="pull-right">{{ user.sex === '0' ? '男' : user.sex === '1' ? '女' : '保密' }}</span>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="xy_dept_profile"/>
                <span class="pull-left">部门</span>
                <span class="pull-right">{{ user.dept.deptName }}</span>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="xy_post_profile"/>
                <span class="pull-left">岗位</span>
                <span class="pull-right">{{ user.post.postName }}</span>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="xy_roleGroup"/>
                <span class="pull-left">角色组</span>
                <span class="pull-right">{{ roleGroup }}</span>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="xy_creatTime"/>
                <span class="pull-left">创建日期</span>
                <span class="pull-right">{{ user.createTime }}</span>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16" :xs="24">
        <el-card shadow="hover">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <el-row>
                <el-col :span="8">
                  <userInfo :user="user"/>
                </el-col>
              </el-row>

            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <el-row>
                <el-col :span="8">
                  <resetPwd :user="user"/>
                </el-col>
              </el-row>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import {getUserProfile} from "@/api/system/user";

export default {
  name: "Profile",
  components: {userAvatar, userInfo, resetPwd},
  data() {
    return {
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo"
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
        this.roleGroup = response.roleGroup;
      });
    }
  }
};
</script>
