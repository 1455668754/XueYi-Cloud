<template>
  <div>
    <el-checkbox v-model="menuExpand" @change="handleTreeExpand">展开/折叠</el-checkbox>
    <el-checkbox v-model="menuNodeAll" @change="handleTreeNodeAll">全选/全不选</el-checkbox>
    <el-tree
      class="tree-border"
      :data="dataOptions"
      show-checkbox
      ref="dataRef"
      node-key="id"
      empty-text="加载中，请稍候"
      :props="defaultProps"
    />
  </div>
</template>

<script>
import { getOrganizeRoleApi } from '@/api/system/authority/role'

export default {
  name: 'RoleDataModal',
  props: {
    // 源策略选项
    dataOptions: {
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
    getDataScope(id) {
      getOrganizeRoleApi(id).then(response => {
        const authIds = response.data
        this.$nextTick(() => {
          authIds.forEach((v) => {
            this.$nextTick(() => {
              this.$refs.dataRef.setChecked(v, true, false)
            })
          })
        })
      })
    },
    /** 树权限（展开/折叠） */
    handleTreeExpand(value) {
      let treeList = this.dataOptions
      for (let i = 0; i < treeList.length; i++) {
        this.$refs.dataRef.store.nodesMap[treeList[i].id].expanded = value
      }
    },
    /** 树权限（全选/全不选） */
    handleTreeNodeAll(value) {
      this.$refs.dataRef.setCheckedNodes(value ? this.dataOptions : [])
    },
    /** 所有权限节点数据 */
    getAllCheckedKeys() {
      // 目前被选中的节点
      let checkedKeys = this.$refs.dataRef.getCheckedKeys()
      // 半选中的节点
      let halfCheckedKeys = this.$refs.dataRef.getHalfCheckedKeys()
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
      return checkedKeys
    }
  }
}
</script>
