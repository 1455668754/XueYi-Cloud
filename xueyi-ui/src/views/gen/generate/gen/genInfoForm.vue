<template>
  <el-form ref="genInfoForm" :model="info" :rules="rules" label-width="170px">
    <el-row>
      <el-col :span="12">
        <el-form-item prop="tplCategory">
          <span slot="label">生成模板</span>
          <el-select v-model="info.tplCategory">
            <el-option
              v-for="dict in dict.type.gen_template_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="packageName">
          <span slot="label">
            生成包路径
            <el-tooltip content="生成在哪个java包下，例如 com.xueyi.system" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.packageName"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="moduleName">
          <span slot="label">
            生成模块名
            <el-tooltip content="可理解为子系统名，例如 system" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.moduleName"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="authorityName">
          <span slot="label">
            生成权限名
            <el-tooltip content="权限标识，例如 organize；最终生成的功能权限标识为： 生成权限名:生成业务名:功能名，如：organize:module:add" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.authorityName"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="businessName">
          <span slot="label">
            生成业务名
            <el-tooltip content="可理解为功能英文名，例如 module；最终业务生成的前端为： 生成模块名.生成权限名.生成业务名，如：system/organize/module"
                        placement="top"
            >
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.businessName"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="functionName">
          <span slot="label">
            生成功能名
            <el-tooltip content="用作类描述，例如 用户" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.functionName"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="genType">
          <span slot="label">
            生成代码方式
            <el-tooltip content="默认为zip压缩包下载，也可以自定义生成路径" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-radio-group v-model="info.genType">
            <el-radio
              v-for="dict in dict.type.gen_generation_mode"
              :key="dict.value"
              :label="dict.value"
              :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>

      <el-col :span="12" v-if="info.genType === '1'">
        <el-form-item prop="genPath">
          <span slot="label">
            后端生成路径
          </span>
          <el-input v-model="info.genPath"/>
        </el-form-item>
      </el-col>
      <el-col :span="12" v-if="info.genType === '1'">
        <el-form-item prop="uiPath">
          <span slot="label">
            前端生成路径
          </span>
          <el-input v-model="info.uiPath"/>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <h4 class="form-header">参数配置</h4>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            多租户模式
          </span>
          <el-radio-group v-model="option.isTenant">
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

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            源策略模式
          </span>
          <el-radio-group v-model="option.sourceMode">
            <el-radio-button
              v-for="dict in dict.type.gen_source_mode"
              :key="dict.value"
              :label="dict.value"
              :value="dict.value"
            >{{ dict.label }}
            </el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row v-show="!isMergeTpl(info.tplCategory)">
      <h4 class="form-header">主表配置</h4>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
             归属模块
          </span>
          <el-select v-model="option.parentModuleId" placeholder="请选择" @change="handleModuleChange">
            <el-option
              v-for="item in modules"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
             上级菜单
          </span>
          <treeselect
            :append-to-body="true"
            v-model="option.parentMenuId"
            :options="menus"
            :normalizer="normalizer"
            :show-count="true"
            placeholder="请选择"
          />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            主键字段
            <el-tooltip content="主键字段， 默认为id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="option.id" placeholder="请选择" clearable>
            <el-option
              v-for="item in info.subList"
              :key="item.id"
              :label="item.name + '：' + item.comment"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            名称字段
            <el-tooltip content="名称字段， 默认为name，LIKE查询" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="option.name" placeholder="请选择" clearable>
            <el-option
              v-for="item in info.subList"
              :key="item.id"
              :label="item.name + '：' + item.comment"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            状态字段
            <el-tooltip content="状态字段， 默认为status（0 启用 1 禁用）" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="option.status" placeholder="请选择" clearable>
            <el-option
              v-for="item in info.subList"
              :key="item.id"
              :label="item.name + '：' + item.comment"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            排序字段
            <el-tooltip content="排序字段， 默认为sort" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="option.sort" placeholder="请选择" clearable>
            <el-option
              v-for="item in info.subList"
              :key="item.id"
              :label="item.name + '：' + item.comment"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row v-show="isTreeTpl(info.tplCategory)">
      <h4 class="form-header">树表配置</h4>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            树编码字段
            <el-tooltip content="树显示的编码字段名， 默认设置为Id主键" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="option.treeCode" placeholder="请选择" clearable>
            <el-option
              v-for="item in info.subList"
              :key="item.id"
              :label="item.name + '：' + item.comment"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            树父编码字段
            <el-tooltip content="树的父编码字段名， 默认为parentId" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="option.parentId" placeholder="请选择" clearable>
            <el-option
              v-for="item in info.subList"
              :key="item.id"
              :label="item.name + '：' + item.comment"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            树名称字段
            <el-tooltip content="树显示的编码字段名， 默认为name" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="option.treeName" placeholder="请选择" clearable>
            <el-option
              v-for="item in info.subList"
              :key="item.id"
              :label="item.name + '：' + item.comment"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            祖籍列表字段
            <el-tooltip content="树的祖籍列表字段名， 默认为ancestors" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="option.ancestors" placeholder="请选择" clearable>
            <el-option
              v-for="item in info.subList"
              :key="item.id"
              :label="item.name + '：' + item.comment"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row v-show="isSubTpl(info.tplCategory)">
      <h4 class="form-header">主子表配置</h4>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            外键关联的主表字段
            <el-tooltip content="外键对应的主表字段， 如：id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="option.foreignId" placeholder="请选择" clearable>
            <el-option
              v-for="item in info.subList"
              :key="item.id"
              :label="item.name + '：' + item.comment"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            关联子表的表名
            <el-tooltip content="关联子表的表名， 如：sys_user" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="option.subTableId" placeholder="请选择" @change="handleSubTableChange" clearable>
            <el-option
              v-for="item in tables"
              :key="item.id"
              :label="item.name + '：' + item.comment"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            关联子表的外键字段
            <el-tooltip content="子表关联的外键字段， 如：dept_id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="option.subForeignId" placeholder="请选择" clearable>
            <el-option
              v-for="item in tableSubs"
              :key="item.id"
              :label="item.name + '：' + item.comment"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>

    </el-row>

    <el-row v-show="!isMergeTpl(info.tplCategory)">
      <h4 class="form-header">主子表配置</h4>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            列表查询
          </span>
          <el-radio-group v-model="option.apiList">
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

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            详情查询
          </span>
          <el-radio-group v-model="option.apiGetInfo">
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

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            新增
          </span>
          <el-radio-group v-model="option.apiAdd">
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

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            强制新增
          </span>
          <el-radio-group v-model="option.apiAddForce">
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

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            修改
          </span>
          <el-radio-group v-model="option.apiEdit">
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

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            强制修改
          </span>
          <el-radio-group v-model="option.apiEditForce">
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

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            修改状态
          </span>
          <el-radio-group v-model="option.apiEditStatus">
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

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            强制修改状态
          </span>
          <el-radio-group v-model="option.apiEditStatusForce">
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

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            批量删除
          </span>
          <el-radio-group v-model="option.apiBatchRemove">
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

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            强制批量删除
          </span>
          <el-radio-group v-model="option.apiBatchRemoveForce">
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

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            导出
          </span>
          <el-radio-group v-model="option.apiExport">
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

    </el-row>
  </el-form>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { TemplateTypeEnum } from '@enums/gen'

export default {
  components: { Treeselect },
  dicts: ['gen_template_type', 'gen_generation_mode', 'sys_yes_no', 'gen_source_mode'],
  props: {
    info: {
      type: Object,
      default: null
    },
    option: {
      type: Object,
      default: null
    },
    tables: {
      type: Array,
      default: null
    },
    tableSubs: {
      type: Array,
      default: null
    },
    modules: {
      type: Array,
      default: []
    },
    menus: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      subColumns: [],
      rules: {
        tplCategory: [
          { required: true, message: '请选择生成模板', trigger: 'blur' }
        ],
        packageName: [
          { required: true, message: '请输入生成包路径', trigger: 'blur' }
        ],
        moduleName: [
          { required: true, message: '请输入生成模块名', trigger: 'blur' }
        ],
        businessName: [
          { required: true, message: '请输入生成业务名', trigger: 'blur' }
        ],
        functionName: [
          { required: true, message: '请输入生成功能名', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
  },
  methods: {
    /** 关联表类型校验 */
    isMergeTpl(tplType) {
      return tplType === TemplateTypeEnum.MERGE
    },
    /** 单表类型校验 */
    isBaseTpl(tplType) {
      return tplType === TemplateTypeEnum.BASE
    },
    /** 树型表类型校验 */
    isTreeTpl(tplType) {
      return tplType === TemplateTypeEnum.TREE || tplType === TemplateTypeEnum.SUB_TREE
    },
    /** 主子型表类型校验 */
    isSubTpl(tplType) {
      return tplType === TemplateTypeEnum.SUB_BASE || tplType === TemplateTypeEnum.SUB_TREE
    },
    /** 转换菜单数据结构 */
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
    /** 模块切换操作 */
    handleModuleChange(e) {
      this.$emit('moduleChange', e)
      this.option.parentMenuId = undefined
    },
    /** 关联子表的表名切换操作 */
    handleSubTableChange(e) {
      this.$emit('subTableChange', e)
      this.option.treeName = undefined
    }
  }
}
</script>
