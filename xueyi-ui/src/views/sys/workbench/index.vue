<template>
  <div class="app-container">
    <div class="system-title">工作台</div>
    <el-card class="system-card" shadow="hover" v-for="(item) in routeList">
      <div class="card-main">
        <div class="card-image">
          <el-image
            style="width: 60px; height: 60px"
            :src="item.logo !== undefined ? item.logo : ''"
            fit="cover"
          />
        </div>
        <div class="card-text">
          <div>
            <span class="card-text-main">{{ item.name }}</span>
            <el-button size="mini" class="card-text-button" @click="handleJump(item)" round>去管理></el-button>
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
import { getModuleRouters } from '@/api/menu'
import { MODULE_KEY } from '@enums'

export default {
  name: 'workbench',
  data() {
    return {
      routeList: []
    }
  },
  created() {
    this.getRoutes()
  },
  methods: {
    getRoutes() {
      getModuleRouters().then(response => {
        this.routeList = response.data
      })
    },
    handleJump(item) {
      const moduleId = localStorage.getItem(MODULE_KEY) || MODULE_KEY
      if (item.id === moduleId) {
        this.$modal.msgSuccess('当前正处于[' + item.name + ']，无需切换！')
      }else {
        this.$modal.confirm('确定要跳转到模块：【' + item.name + '】， 并重新加载其资源吗？').then(function() {
        }).then(() => {
          localStorage.setItem(MODULE_KEY, item.id)
          this.$modal.msgSuccess('已成功切换至' + item.name + '！')
          setTimeout(() => {
            location.reload()
          }, 500);

        }).catch(() => {
          this.$modal.msgWarning('切换失败！')
        })
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
