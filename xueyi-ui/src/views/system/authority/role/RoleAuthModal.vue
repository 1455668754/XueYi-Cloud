<template>
  <div>
    <el-checkbox v-model="menuExpand" @change="handleTreeExpand">展开/折叠</el-checkbox>
    <el-checkbox v-model="menuNodeAll" @change="handleTreeNodeAll">全选/全不选</el-checkbox>
    <el-tree
      class="tree-border"
      :data="menuOptions"
      show-checkbox
      ref="authRef"
      node-key="id"
      empty-text="加载中，请稍候"
      :props="defaultProps"
    />
  </div>
</template>

<script>
import { getAuthRoleApi } from '@/api/system/authority/role'

export default {
  name: 'RoleAuthModal',
  props: {
    // 源策略选项
    menuOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      menuExpand: false,
      menuNodeAll: false,
      authIds: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },
  methods: {
    /** 获取树权限 */
    getAuthScope(id) {
      getAuthRoleApi(id).then(response => {
        const authIds = response.data
        this.$nextTick(() => {
          authIds.forEach((v) => {
            this.$nextTick(() => {
              this.$refs.authRef.setChecked(v, true, false)
            })
          })
        })
      })
    },
    /** 树权限（展开/折叠） */
    handleTreeExpand(value) {
      let treeList = this.menuOptions
      for (let i = 0; i < treeList.length; i++) {
        this.$refs.authRef.store.nodesMap[treeList[i].id].expanded = value
      }
    },
    /** 树权限（全选/全不选） */
    handleTreeNodeAll(value) {
      this.$refs.authRef.setCheckedNodes(value ? this.menuOptions : [])
    },
    /** 所有权限节点数据 */
    getAllCheckedKeys() {
      // 目前被选中的节点
      let checkedKeys = this.$refs.authRef.getCheckedKeys()
      // 半选中的节点
      let halfCheckedKeys = this.$refs.authRef.getHalfCheckedKeys()
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
      return checkedKeys
    }
  }
}
</script>
