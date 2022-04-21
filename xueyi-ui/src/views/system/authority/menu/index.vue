<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="归属模块" prop="moduleId">
        <el-select v-model="queryParams.moduleId" placeholder="请选择" clearable>
          <el-option
            v-for="dict in moduleOptions"
            :key="dict.id"
            :label="dict.name"
            :value="dict.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="菜单标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="菜单类型" prop="menuType">
        <el-select v-model="queryParams.menuType" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.auth_menu_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="公共菜单" prop="isCommon">
        <el-select v-model="queryParams.isCommon" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.sys_common_private"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="IconEnum.SEARCH" size="mini" @click="handleQuery">搜索</el-button>
        <el-button :icon="IconEnum.RESET" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          :icon="IconEnum.ADD"
          size="mini"
          @click="handleAdd"
          v-hasPermi="[MenuAuth.ADD]"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          :icon="IconEnum.EDIT"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="[MenuAuth.EDIT]"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          :icon="IconEnum.DELETE"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="[MenuAuth.DELETE]"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns" />
    </el-row>

    <el-table v-if="refreshTable" v-loading="loading" :data="tableList" :indent="30" row-key="id" :default-expand-all="isExpandAll" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" v-if="columns[0].visible" min-width="55" />
      <el-table-column label="菜单标题" align="left" prop="title" v-if="columns[1].visible" :show-overflow-tooltip="true" min-width="220" />
      <el-table-column label="菜单图标" align="center" prop="icon" v-if="columns[2].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <svg-icon :icon-class="scope.row.icon === undefined ? '':scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column label="菜单类型" align="center" prop="menuType" v-if="columns[3].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.auth_menu_type" :value="scope.row.menuType" />
        </template>
      </el-table-column>
      <el-table-column label="归属模块" align="center" prop="moduleId" v-if="columns[4].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="状态" align="center" prop="status" v-if="columns[5].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="公共菜单" align="center" prop="isCommon" v-if="columns[6].visible" :show-overflow-tooltip="true" min-width="100">
        <template v-slot="scope">
          <dict-tag :options="dict.type.sys_common_private" :value="scope.row.isCommon" />
        </template>
      </el-table-column>
      <el-table-column label="显示顺序" align="center" prop="sort" v-if="columns[7].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="权限标识" align="center" prop="perms" v-if="columns[8].visible" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="操作" align="center" v-if="columns[9].visible" class-name="small-padding fixed-width" width="180" fixed="right">
        <template v-slot="scope">
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.EDIT"
            v-if="handleMenu(scope.row)"
            @click="handleUpdate(scope.row)"
            v-hasPermi="[MenuAuth.EDIT]"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            :icon="IconEnum.DELETE"
            v-if="handleMenu(scope.row)"
            @click="handleDelete(scope.row)"
            v-hasPermi="[MenuAuth.DELETE]"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" :before-close="handleClose" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="归属模块" prop="moduleId">
              <el-select v-model="form.moduleId" placeholder="请选择" @change="selectModuleChange">
                <el-option
                  v-for="dict in moduleOptions"
                  :key="dict.id"
                  :label="dict.name"
                  :value="dict.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上级菜单" prop="parentId">
              <treeselect v-model="form.parentId" :options="treeOptions" :normalizer="normalizer" placeholder="选择上级菜单" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="title">
              <span slot="label">
                <el-tooltip content="显示的菜单名称，如：`用户管理" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                菜单标题
              </span>
              <el-input v-model="form.title" placeholder="请输入菜单标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="sort">
              <el-input-number v-model="form.sort" :max="65535" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="isLessor">
            <el-form-item label="公共菜单" prop="isCommon">
              <span slot="label">
                <el-tooltip content="是否可以被其他租户使用" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                公共菜单
              </span>
              <el-radio-group v-model="form.isCommon" :disabled="form.id !== undefined">
                <el-radio-button
                  v-for="dict in dict.type.sys_common_private"
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
              <el-radio-group v-model="form.status">
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
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType" @change="selectRuleChange">
                <el-radio-button
                  v-for="dict in dict.type.auth_menu_type"
                  :key="dict.value"
                  :label="dict.value"
                  :value="dict.value"
                >{{ dict.label }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-show="isMenu()">
            <el-form-item label="页面类型" prop="frameType">
              <el-radio-group v-model="form.frameType" @change="selectRuleChange">
                <el-radio-button
                  v-for="dict in dict.type.auth_frame_type"
                  :key="dict.value"
                  :label="dict.value"
                  :value="dict.value"
                >{{ dict.label }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-show="!(isExternalLinksMenu() || isButton())">
            <el-form-item label="路由名称" prop="path">
              <span slot="label">
                <el-tooltip content="访问的路由，如：`user`" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                路由名称
              </span>
              <el-input v-model="form.path" placeholder="请输入路由名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-show="isDir() || isMenu()">
            <el-form-item label="菜单图标" prop="icon">
              <el-popover
                placement="bottom-start"
                width="460"
                trigger="click"
                @show="$refs['iconSelect'].reset()"
              >
                <IconSelect ref="iconSelect" @selected="selected" />
                <el-input slot="reference" v-model="form.icon" placeholder="点击选择图标" readonly>
                  <svg-icon
                    v-if="form.icon"
                    slot="prefix"
                    :icon-class="form.icon"
                    class="el-input__icon"
                    style="height: 32px;width: 16px;"
                  />
                  <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-show="isDetails() || isNormalMenu()">
            <el-form-item label="组件路径" prop="component">
              <span slot="label">
                <el-tooltip content="访问的组件路径，如：`system/user/index`，默认在`views`目录下" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                组件路径
              </span>
              <el-input v-model="form.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-show="isEmbeddedMenu() || isExternalLinksMenu()">
            <el-form-item label="外链路径" prop="frameSrc">
              <span slot="label">
                <el-tooltip content="访问的外网地址，以`http(s)://`开头" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                外链路径
              </span>
              <el-input v-model="form.frameSrc" placeholder="请输入外链地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-show="isDetails() || isMenu()">
            <el-form-item label="路由参数" prop="paramPath">
              <span slot="label">
                <el-tooltip content="访问菜单时传递的参数" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                路由参数
              </span>
              <el-input v-model="form.paramPath" placeholder="请输入路由参数" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-show="!isDir() && !isExternalLinksMenu()">
            <el-form-item label="权限标识" prop="perms">
              <span slot="label">
                <el-tooltip content="控制器中定义的权限字符，如：`system:user:list`" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                权限标识
              </span>
              <el-input v-model="form.perms" placeholder="请输入权限标识" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-show="(isMenu() || isDetails()) && !isExternalLinksMenu()">
            <el-form-item label="页面缓存" prop="isCache">
              <span slot="label">
                <el-tooltip content="选择是则该页面切换不会自动刷新" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                页面缓存
              </span>
              <el-radio-group v-model="form.isCache">
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
          <el-col :span="12" v-show="(isMenu() || isDetails()) && !isExternalLinksMenu()">
            <el-form-item label="固定标签" prop="isAffix">
              <span slot="label">
                <el-tooltip content="选择是则该标签会始终固定在页签中" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                固定标签
              </span>
              <el-radio-group v-model="form.isAffix">
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
          <el-col :span="12" v-show="(isMenu() || isDetails()) && !isExternalLinksMenu()">
            <el-form-item label="标签状态" prop="hideTab">
              <span slot="label">
                <el-tooltip content="选择隐藏则打开该将页面不会出现" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                标签状态
              </span>
              <el-radio-group v-model="form.hideTab">
                <el-radio-button
                  v-for="dict in dict.type.sys_show_hide"
                  :key="dict.value"
                  :label="dict.value"
                  :value="dict.value"
                >{{ dict.label }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-show="(isDir() || isMenu())">
            <el-form-item label="菜单状态" prop="hideMenu">
              <span slot="label">
                <el-tooltip content="选择隐藏则菜单将不会出现在侧边栏" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                菜单状态
              </span>
              <el-radio-group v-model="form.hideMenu">
                <el-radio-button
                  v-for="dict in dict.type.sys_show_hide"
                  :key="dict.value"
                  :label="dict.value"
                  :value="dict.value"
                >{{ dict.label }}
                </el-radio-button>
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
import {
  listMenuApi,
  getMenuApi,
  addMenuApi,
  editMenuApi,
  delMenuApi,
  getMenuRouteListExNodesApi,
  getMenuRouteListApi
} from '@/api/system/authority/menu'
import { MenuAuth } from '@auth/system'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {
  COMMON_MODULE,
  DicCommonPrivateEnum,
  DicShowHideEnum,
  DicSortEnum,
  DicStatusEnum,
  DicYesNoEnum,
  IconEnum
} from '@enums'
import { optionModuleApi } from '@/api/system/authority/module'
import { COMMON_MENU, FrameTypeEnum, MenuTypeEnum } from '@enums/system'
import store from '@/store'
import IconSelect from '@/components/IconSelect'

export default {
  name: 'MenuManagement',
  components: { IconSelect, Treeselect },
  /** 字典查询 */
  dicts: ['auth_menu_type', 'sys_yes_no', 'auth_frame_type', 'sys_show_hide', 'sys_normal_disable', 'sys_common_private'],
  data() {
    return {
      //权限标识
      MenuAuth: MenuAuth,
      // 图标标识
      IconEnum: IconEnum,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中数组名称
      idNames: [],
      // 上级树选项
      treeOptions: [],
      // 模块选项
      moduleOptions: [],
      // 是否为租管租户
      isLessor: store.getters.isLessor,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 表格数据
      tableList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 提交状态
      submitLoading: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        title: undefined,
        menuType: undefined,
        status: undefined,
        isCommon: undefined,
        moduleId: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `勾选列`, visible: true },
        { key: 1, label: `菜单标题`, visible: true },
        { key: 2, label: `菜单图标`, visible: true },
        { key: 3, label: `菜单类型`, visible: true },
        { key: 4, label: `归属模块`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `公共菜单`, visible: true },
        { key: 7, label: `显示顺序`, visible: true },
        { key: 8, label: `权限标识`, visible: true },
        { key: 9, label: `操作列`, visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      baseRules: {
        title: [
          { required: true, message: '菜单标题不能为空', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '显示顺序不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ],
        isCommon: [
          { required: true, message: '公共菜单不能为空', trigger: 'change' }
        ],
        moduleId: [
          { required: true, message: '归属模块不能为空', trigger: 'change' }
        ],
        parentId: [
          { required: true, message: '上级菜单不能为空', trigger: 'change' }
        ],
        menuType: [
          { required: true, message: '菜单类型不能为空', trigger: 'change' }
        ]
      },
      // 目录表单校验
      dirRules: {
        icon: [
          { required: true, message: '菜单图标不能为空', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '路由名称不能为空', trigger: 'blur' }
        ],
        hideMenu: [
          { required: true, message: '菜单状态不能为空', trigger: 'change' }
        ]
      },
      detailsRules: {
        path: [
          { required: true, message: '路由名称不能为空', trigger: 'blur' }
        ],
        perms: [
          { required: true, message: '权限标识不能为空', trigger: 'blur' }
        ],
        component: [
          { required: true, message: '组件路径不能为空', trigger: 'blur' }
        ],
        isCache: [
          { required: true, message: '页面缓存不能为空', trigger: 'change' }
        ],
        isAffix: [
          { required: true, message: '固定标签不能为空', trigger: 'change' }
        ],
        hideTab: [
          { required: true, message: '标签状态不能为空', trigger: 'change' }
        ]
      },
      buttonRules: {
        perms: [
          { required: true, message: '权限标识不能为空', trigger: 'blur' }
        ]
      },
      menuRules: {
        hideMenu: [
          { required: true, message: '菜单状态不能为空', trigger: 'change' }
        ],
        icon: [
          { required: true, message: '菜单图标不能为空', trigger: 'blur' }
        ],
        frameType: [
          { required: true, message: '页面类型不能为空', trigger: 'change' }
        ]
      },
      normalMenuRules: {
        path: [
          { required: true, message: '路由名称不能为空', trigger: 'blur' }
        ],
        component: [
          { required: true, message: '组件路径不能为空', trigger: 'blur' }
        ],
        perms: [
          { required: true, message: '权限标识不能为空', trigger: 'blur' }
        ],
        isCache: [
          { required: true, message: '页面缓存不能为空', trigger: 'change' }
        ],
        isAffix: [
          { required: true, message: '固定标签不能为空', trigger: 'change' }
        ],
        hideTab: [
          { required: true, message: '标签状态不能为空', trigger: 'change' }
        ]
      },
      embeddedMenuRules: {
        path: [
          { required: true, message: '路由名称不能为空', trigger: 'blur' }
        ],
        frameSrc: [
          { required: true, message: '外链路径', trigger: 'blur' },
          {
            type: 'url',
            message: '请输入正确的外链路径',
            trigger: ['change', 'blur']
          }
        ],
        perms: [
          { required: true, message: '权限标识不能为空', trigger: 'blur' }
        ],
        isCache: [
          { required: true, message: '页面缓存不能为空', trigger: 'change' }
        ],
        isAffix: [
          { required: true, message: '固定标签不能为空', trigger: 'change' }
        ],
        hideTab: [
          { required: true, message: '标签状态不能为空', trigger: 'change' }
        ]
      },
      externalLinksMenuRules: {
        frameSrc: [
          { required: true, message: '外链路径', trigger: 'blur' },
          {
            type: 'url',
            message: '请输入正确的外链路径',
            trigger: ['change', 'blur']
          }
        ]
      },
      rules: {}
    }
  },
  created() {
    this.getList()
    this.getOptions()
  },
  methods: {
    /** 查询菜单列表 */
    getList() {
      this.loading = true
      listMenuApi(this.queryParams).then(response => {
        this.tableList = response.data
        this.loading = false
      })
    },
    /** 查询选项列表 */
    getOptions() {
      this.moduleOptions = []
      optionModuleApi().then(response => {
        this.moduleOptions = response.data.items
      })
    },
    /** 转换树数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.title,
        isDisabled: false,
        children: node.children
      }
    },
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
      this.form = {
        id: undefined,
        moduleId: COMMON_MODULE,
        parentId: COMMON_MENU,
        title: undefined,
        path: undefined,
        frameSrc: undefined,
        component: undefined,
        paramPath: undefined,
        isCache: DicYesNoEnum.NO,
        isAffix: DicYesNoEnum.NO,
        isDisabled: undefined,
        frameType: FrameTypeEnum.NORMAL,
        menuType: MenuTypeEnum.DIR,
        hideTab: DicShowHideEnum.SHOW,
        hideMenu: DicShowHideEnum.SHOW,
        perms: undefined,
        icon: undefined,
        sort: DicSortEnum.ZERO,
        status: DicStatusEnum.NORMAL,
        remark: undefined,
        isCommon: DicCommonPrivateEnum.PRIVATE
      }
      this.resetForm('form')
      this.submitLoading = false
    },
    /** 搜索操作 */
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    /** 重置操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.idNames = selection.map(item => item.name)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false
      this.isExpandAll = !this.isExpandAll
      this.$nextTick(() => {
        this.refreshTable = true
      })
    },
    /** 新增操作 */
    handleAdd(row) {
      this.reset()
      this.selectRuleChange()
      if (row !== undefined && row.moduleId) {
        this.form.moduleId = row.moduleId
        this.form.parentId = row.id
      }
      this.open = true
      this.selectModuleChange()
      this.title = '添加菜单'
    },
    /** 修改操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMenuApi(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改菜单'
        this.selectRuleChange()
        this.selectModuleChange()
      })
    },
    /** 提交操作 */
    submitForm: function() {
      this.submitLoading = false
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            editMenuApi(this.initialize()).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).catch()
          } else {
            addMenuApi(this.initialize()).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }).catch()
          }
        }
      })
      this.submitLoading = false
    },
    /** 删除操作 */
    handleDelete(row) {
      const delIds = row.id || this.ids
      const delNames = row.name || this.idNames
      this.$modal.confirm('是否确定要删除' + delNames + '？').then(function() {
        return delMenuApi(delIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功！')
      }).catch(() => {
      })
    },
    /** 菜单类型校验 */
    isDir() {
      return this.form.menuType === MenuTypeEnum.DIR
    },
    isMenu() {
      return this.form.menuType === MenuTypeEnum.MENU
    },
    isDetails() {
      return this.form.menuType === MenuTypeEnum.DETAILS
    },
    isButton() {
      return this.form.menuType === MenuTypeEnum.BUTTON
    },
    /** 页面类型校验 */
    isNormalMenu() {
      return this.isMenu() && this.form.frameType === FrameTypeEnum.NORMAL
    },
    isEmbeddedMenu() {
      return this.isMenu() && this.form.frameType === FrameTypeEnum.EMBEDDED
    },
    isExternalLinksMenu() {
      return this.isMenu() && this.form.frameType === FrameTypeEnum.EXTERNAL_LINKS
    },
    /** 选择图标 */
    selected(name) {
      this.form.icon = name
    },
    /** 菜单操作权限检验 */
    handleMenu(row) {
      return row.id !== COMMON_MENU && (row.common === DicCommonPrivateEnum.PRIVATE || this.isLessor)
    },
    /** 模块 - 菜单联动 */
    selectModuleChange() {
      this.form.moduleId === undefined
        ? this.treeOptions = []
        : this.form.id === undefined
          ? getMenuRouteListApi(this.form.moduleId, MenuTypeEnum.DETAILS).then(response => {
            this.treeOptions = response.data
          })
          : getMenuRouteListExNodesApi(this.form.id, this.form.moduleId, MenuTypeEnum.DETAILS).then(response => {
            this.treeOptions = response.data
          })
    },
    /** 校验规则变更 */
    selectRuleChange() {
      if (this.isDir()) {
        this.rules = Object.assign({}, this.baseRules, this.dirRules)
      } else if (this.isDetails()) {
        this.rules = Object.assign({}, this.baseRules, this.detailsRules)
      } else if (this.isButton()) {
        this.rules = Object.assign({}, this.baseRules, this.buttonRules)
      } else if (this.isNormalMenu()) {
        this.rules = Object.assign({}, this.baseRules, this.menuRules, this.normalMenuRules)
      } else if (this.isEmbeddedMenu()) {
        this.rules = Object.assign({}, this.baseRules, this.menuRules, this.embeddedMenuRules)
      } else if (this.isExternalLinksMenu()) {
        this.rules = Object.assign({}, this.baseRules, this.menuRules, this.externalLinksMenuRules)
      }
    },
    /** 生成提交参数 */
    initialize() {
      const menu = {}
      menu.id = this.form.id
      menu.name = this.form.name
      menu.title = this.form.title
      menu.moduleId = this.form.moduleId
      menu.parentId = this.form.parentId
      menu.menuType = this.form.menuType
      menu.frameType = FrameTypeEnum.NORMAL
      menu.sort = this.form.sort
      menu.isCommon = this.form.isCommon
      menu.status = this.form.status
      switch (this.form.menuType) {
        case MenuTypeEnum.DIR:
          menu.path = this.form.path
          menu.icon = this.form.icon
          menu.hideMenu = this.form.hideMenu
          break
        case MenuTypeEnum.MENU:
          menu.frameType = this.form.frameType
          menu.icon = this.form.icon
          menu.paramPath = this.form.paramPath
          menu.perms = this.form.perms
          if (this.isExternalLinksMenu()) {
            menu.frameSrc = this.form.frameSrc
          } else {
            menu.path = this.form.path
            menu.isCache = this.form.isCache
            menu.isAffix = this.form.isAffix
            menu.hideTab = this.form.hideTab
            menu.hideMenu = this.form.hideMenu
            if (this.isNormalMenu()) {
              menu.component = this.form.component
            } else if (this.isEmbeddedMenu()) {
              menu.frameSrc = this.form.frameSrc
            }
          }
          break
        case MenuTypeEnum.DETAILS:
          menu.path = this.form.path
          menu.component = this.form.component
          menu.paramPath = this.form.paramPath
          menu.perms = this.form.perms
          menu.hideTab = this.form.hideTab
          break
        case MenuTypeEnum.BUTTON:
          menu.perms = this.form.perms
          break
      }
      return menu
    }
  }
}
</script>
