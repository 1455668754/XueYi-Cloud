<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:menu:add']"
        >新增
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    {{menuList}}
    <el-table
      v-loading="loading"
      :data="menuList"
      row-key="id"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      :indent="40"
    >
      <el-table-column prop="label" label="模块|菜单名称" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="icon" label="图标" align="center">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon" v-if="scope.row.icon !== null"/>
        </template>
      </el-table-column>
      <el-table-column prop="perms" label="权限标识" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            active-color="#13ce66"
            inactive-color="#ff4949"
            disabled>
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:menu:edit']"
            v-if="scope.row.type === '1' && scope.row.isMain === '0'"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['system:menu:add']"
          >新增
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:menu:remove']"
            v-if="scope.row.type === '1' && scope.row.isMain === '0'"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <treeselect
                v-model="form.parentId"
                :options="menuOptions"
                :normalizer="normalizer"
                :show-count="true"
                @select="treeSelectSelect"
                placeholder="选择上级菜单"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">目录</el-radio>
                <el-radio label="C">菜单</el-radio>
                <el-radio label="F">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="form.menuType != 'F'" label="菜单图标">
              <el-popover
                placement="bottom-start"
                width="460"
                trigger="click"
                @show="$refs['iconSelect'].reset()"
              >
                <IconSelect ref="iconSelect" @selected="selected"/>
                <el-input slot="reference" v-model="form.icon" placeholder="点击选择图标" readonly>
                  <svg-icon
                    v-if="form.icon"
                    slot="prefix"
                    :icon-class="form.icon"
                    class="el-input__icon"
                    style="height: 32px;width: 16px;"
                  />
                  <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="form.menuName" placeholder="请输入菜单名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="0" :max="127"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" label="是否外链">
              <el-radio-group v-model="form.isFrame">
                <el-radio label="0">是</el-radio>
                <el-radio label="1">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" label="路由地址" prop="path">
              <el-input v-model="form.path" placeholder="请输入路由地址"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item label="组件路径" prop="component">
              <el-input v-model="form.component" placeholder="请输入组件路径"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'M'" label="权限标识">
              <el-input v-model="form.perms" placeholder="请输入权限标识" maxlength="100"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" label="显示状态">
              <el-radio-group v-model="form.visible">
                <el-radio
                  v-for="dict in visibleOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" label="菜单状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType == 'C'" label="是否缓存">
              <el-radio-group v-model="form.isCache">
                <el-radio label="0">缓存</el-radio>
                <el-radio label="1">不缓存</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getMenu, delMenu, addMenu, updateMenu} from "@/api/system/menu"
import {treeSelect as systemMenuTreeSelect} from "@/api/system/system"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import IconSelect from "@/components/IconSelect"

export default {
  name: "Menu",
  components: {Treeselect, IconSelect},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 菜单表格树数据
      menuList: [],
      // 菜单树选项
      menuOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 显示状态数据字典
      visibleOptions: [],
      // 菜单状态数据字典
      statusOptions: [],
      //本参数用于判断当前父级是模块Id还是菜单Id
      checkSystem: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        menuName: [
          {required: true, message: "菜单名称不能为空", trigger: "blur"}
        ],
        path: [
          {required: true, message: "路由地址不能为空", trigger: "blur"}
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getDicts("sys_show_hide").then(response => {
      this.visibleOptions = response.data
    })
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data
    })
  },
  methods: {
    // 选择图标
    selected(name) {
      this.form.icon = name
    },
    /** 查询菜单列表 */
    getList() {
      this.loading = true
      systemMenuTreeSelect({searchValue: '1'}).then(response => {
        this.menuList = response.data
        this.loading = false
      })
    },
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.type === '0' ? '模块 | ' + node.label : '菜单 | ' + node.label,
        type: node.type,
        systemId: node.systemId,
        children: node.children
      }
    },
    treeSelectSelect(node, instanceId) {
      if (node.type === '0') {
        this.form.systemId = node.systemId
        this.checkSystem = true
      } else if (node.type === '1') {
        this.form.systemId = node.systemId
        this.checkSystem = false
      }
    },
    /** 查询菜单下拉树结构 */
    getTreeSelect(row) {
      let id = '0'
      if (row != null && row.type === '1' && row.id) {
        id = row.id
      }
      systemMenuTreeSelect({Id: id, searchValue: '1'}).then(response => {
        this.menuOptions = response.data
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        menuId: undefined,
        systemId: 0,
        parentId: 0,
        menuName: undefined,
        icon: undefined,
        menuType: "M",
        sort: 0,
        isFrame: "1",
        isCache: "0",
        visible: "0",
        status: "0"
      }
      this.resetForm("form")
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      this.getTreeSelect()
      if (row != null && row.type === '0') {
        this.form.parentId = row.id
        this.form.systemId = row.id
        this.checkSystem = true
      } else if (row != null && row.type === '1' && row.id) {
        this.form.parentId = row.id
        this.form.systemId = row.systemId
        this.checkSystem = false
      }
      this.open = true
      this.title = "添加菜单"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getTreeSelect(row)
      getMenu({menuId: row.id}).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改菜单"
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.checkSystem) {
            this.form.parentId = 0
            this.checkSystem = false
          }
          if (this.form.menuId != undefined) {
            updateMenu(this.form).then(response => {
              this.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMenu(this.form).then(response => {
              this.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除名称为"' + row.label + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delMenu({menuId: row.id})
      }).then(() => {
        this.getList()
        this.msgSuccess("删除成功")
      }).catch((err) => {
      })
    }
  }
}
</script>
