<template>
  <div class="app-container">
    <div class="wrapper-container">
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
        <right-toolbar :showSearch.sync="showSearch" :sortVisible="false" @queryTable="getList"/>
      </el-row>

      <el-table
        v-loading="loading"
        :data="menuList"
        row-key="id"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        :indent="40"
      >
        <el-table-column prop="label" label="模块|菜单名称" :show-overflow-tooltip="true" min-width="120"/>
        <el-table-column prop="icon" label="图标" align="center" min-width="120">
          <template slot-scope="scope">
            <svg-icon :icon-class="scope.row.icon" v-if="scope.row.icon != null"/>
          </template>
        </el-table-column>
        <el-table-column prop="perms" label="权限标识" :show-overflow-tooltip="true" min-width="120"/>
        <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true" min-width="120"/>
        <el-table-column prop="status" label="状态" min-width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="STATUS.NORMAL"
              :inactive-value="STATUS.DISABLE"
              active-color="#13ce66"
              inactive-color="#ff4949"
              disabled>
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="120">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:menu:edit']"
              v-if="scope.row.type === TYPE.MENU && (scope.row.isCommon === IS_COMMON.FALSE || IS_LESSOR)"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-plus"
              @click="handleAdd(scope.row)"
              v-hasPermi="['system:menu:add']"
            >新增
              {{(scope.row.isCommon === IS_COMMON.FALSE)}}
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:menu:remove']"
              v-if="scope.row.type === TYPE.MENU && (scope.row.isCommon === IS_COMMON.FALSE || IS_LESSOR)"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-container-supplement"/>
    </div>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body v-dialogDrag v-dialogDragHeight>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
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
          <el-col :span="14">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio :label="MENU_TYPE.DIR">目录</el-radio>
                <el-radio :label="MENU_TYPE.MENU">菜单</el-radio>
                <el-radio :label="MENU_TYPE.BUTTON">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="公共菜单" v-if="IS_LESSOR">
              <el-radio-group v-model="form.isCommon">
                <el-radio
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="form.menuType !== MENU_TYPE.BUTTON" label="菜单图标">
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
            <el-form-item v-if="form.menuType !== MENU_TYPE.BUTTON">
              <span slot="label">
                <el-tooltip content="选择是外链则路由地址需要以`http(s)://`开头" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                是否外链
              </span>
              <el-radio-group v-model="form.isFrame">
                <el-radio :label="FRAME.YES">是</el-radio>
                <el-radio :label="FRAME.NO">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType !== MENU_TYPE.BUTTON" prop="path">
              <span slot="label">
                <el-tooltip content="访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                路由地址
              </span>
              <el-input v-model="form.path" placeholder="请输入路由地址"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType === MENU_TYPE.BUTTON">
            <el-form-item prop="component">
              <span slot="label">
                <el-tooltip content="访问的组件路径，如：`system/user/index`，默认在`views`目录下" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                组件路径
              </span>
              <el-input v-model="form.component" placeholder="请输入组件路径"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType !== MENU_TYPE.DIR">
              <el-input v-model="form.perms" placeholder="请输入权限标识" maxlength="100"/>
              <span slot="label">
                <el-tooltip content="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                权限字符
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType === MENU_TYPE.MENU">
              <el-input v-model="form.query" placeholder="请输入路由参数" maxlength="255" />
              <span slot="label">
                <el-tooltip content='访问路由的默认传递参数，如：`{"id": 1, "name": "xy"}`' placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                路由参数
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType === MENU_TYPE.MENU">
              <span slot="label">
                <el-tooltip content="选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                是否缓存
              </span>
              <el-radio-group v-model="form.isCache">
                <el-radio :label="CACHE.YES">缓存</el-radio>
                <el-radio :label="CACHE.NO">不缓存</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType !== MENU_TYPE.BUTTON">
              <span slot="label">
                <el-tooltip content="选择隐藏则路由将不会出现在侧边栏，但仍然可以访问" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                显示状态
              </span>
              <el-radio-group v-model="form.visible">
                <el-radio
                  v-for="dict in dict.type.sys_show_hide"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType !== MENU_TYPE.BUTTON">
              <span slot="label">
                <el-tooltip content="选择停用则路由将不会出现在侧边栏，也不能被访问" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                菜单状态
              </span>
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getMenu, delMenu, addMenu, updateMenu} from "@/api/system/menu"
import {treeSelectPermitPersonal as systemMenuTreeSelect} from "@/api/system/system"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import IconSelect from "@basicsComponents/IconSelect"
import store from "@/store"
import {STATUS, VISIBLE, IS_COMMON} from "@constant/constants"
import {TYPE, CACHE, FRAME, MENU_TYPE } from "@constant/authorityContants"

export default {
  name: "Menu",
  dicts: ['sys_show_hide', 'sys_normal_disable', 'sys_yes_no'],
  components: {Treeselect, IconSelect},
  data() {
    return {
      //常量区
      STATUS: STATUS,
      IS_COMMON:IS_COMMON,
      TYPE:TYPE,
      VISIBLE: VISIBLE,
      CACHE: CACHE,
      FRAME: FRAME,
      MENU_TYPE: MENU_TYPE,
      IS_LESSOR: store.getters.isLessor,
      enterpriseName: store.getters.enterpriseName,
      // 遮罩层
      loading: true,
      // 提交状态
      submitLoading: false,
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
  },
  methods: {
    // 选择图标
    selected(name) {
      this.form.icon = name
    },
    /** 查询菜单列表 */
    getList() {
      this.loading = true
      systemMenuTreeSelect().then(response => {
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
        label: node.type === TYPE.SYSTEM ? '模块 | ' + node.label : '菜单 | ' + node.label,
        type: node.type,
        systemId: node.systemId,
        children: node.children
      }
    },
    treeSelectSelect(node, instanceId) {
      if (node.type === TYPE.SYSTEM) {
        this.form.systemId = node.systemId
        this.checkSystem = true
      } else if (node.type === TYPE.MENU) {
        this.form.systemId = node.systemId
        this.checkSystem = false
      }
    },
    /** 查询菜单下拉树结构 */
    getTreeSelect(row) {
      let id = '0'
      if (row != null && row.type === TYPE.MENU && row.id) {
        id = row.id
      }
      systemMenuTreeSelect({Id: id}).then(response => {
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
        menuType: MENU_TYPE.DIR,
        sort: 0,
        isCommon: IS_COMMON.FALSE,
        isFrame: FRAME.NO,
        isCache: CACHE.YES,
        visible: VISIBLE.TRUE,
        status: STATUS.NORMAL
      }
      this.resetForm("form")
      this.submitLoading = false
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      this.getTreeSelect()
      if (row != null && row.type === TYPE.SYSTEM) {
        this.form.parentId = row.id
        this.form.systemId = row.id
        this.checkSystem = true
      } else if (row != null && row.type === TYPE.MENU && row.id) {
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
      this.submitLoading = true
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.checkSystem) {
            this.form.parentId = 0
            this.checkSystem = false
          }
          if (this.form.menuId != null) {
            updateMenu(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            }).catch(() => {
              this.submitLoading = false
            })
          } else {
            addMenu(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            }).catch(() => {
              this.submitLoading = false
            })
          }
        }else{
          this.submitLoading = false
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除名称为"' + row.label + '"的数据项?').then(function () {
        return delMenu({menuId: row.id})
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch((err) => {
      })
    }
  }
}
</script>
