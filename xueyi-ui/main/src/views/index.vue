<template>
  <div class="app-container">
    <div class="system-title">{{ homePageName }}</div>
    <el-card class="system-card" shadow="hover" v-for="(item,index) in list">
      <div class="card-main">
        <div class="card-image">
          <el-image
            style="width: 60px; height: 60px"
            :src="item.imageUrl != null ? JSON.parse(item.imageUrl).materialUrl : null"
            fit="cover"/>
        </div>
        <div class="card-text">
          <div>
            <span class="card-text-main">{{ item.systemName }}</span>
            <el-button size="mini" class="card-text-button" @click="jumpClick(item)" round>去管理></el-button>
          </div>
          <div class="card-text-remark">
            <span>{{ item.remark }}</span>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { viewListSystem } from '@api/login';

export default {
  name: "Index",
  data() {
    return {
      list: [],
      homePageName: this.$store.state.settings.homePageName,
    }
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询可展示子系统模块列表 */
    getList() {
      viewListSystem().then(response => {
        this.list = response.rows;
      });
    },
    jumpClick(item) {
      //内部路由
      if (item.type === '0') {
        this.$router.push(item.route)
      }
      //外部链接
      else if (item.type === '1') {
        if (item.isNew === 'N'){
          window.location.href = item.route;  //在本页面打开外部链接
        }else {
          window.open(item.route, '_blank') // 在新窗口打开外链接
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.system-title {
  margin-bottom: 30px;
  margin-left: 30px;
  font-size: 20px;
  font-weight: 700;
  color: #606067;
  font-family: 微软雅黑, serif;
}

.system-card {
  width: 360px;
  height: 120px;
  margin-bottom: 30px;
  margin-left: 30px;
  display: inline-flex;

  .card-main {
    margin-top: 13px;
    display: inline-flex;

    .card-image {
      margin-left: 20px;
    }

    .card-text {
      margin-left: 40px;

      .card-text-main {
        font-size: 16px;
      }

      .card-text-button {
        margin-left: 20px;
      }

      .card-text-remark {
        height: 38px;
        line-height: 38px;
        font-size: 14px;
        color: #999999;
      }
    }
  }
}

</style>

<style scoped>
.card-text >>> .el-button--mini.is-round {
  padding: 5px 9px !important;
}
</style>
