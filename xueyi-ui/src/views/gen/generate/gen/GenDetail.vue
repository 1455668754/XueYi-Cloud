<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane label="基本信息" name="basic">
        <basic-info-form ref="basicInfo" :info="info"/>
      </el-tab-pane>
      <el-tab-pane label="字段信息" name="columnInfo">
        <el-table ref="dragTable" :data="columns" row-key="columnId" :max-height="tableHeight">
          <el-table-column label="序号" type="index" min-width="50" fixed class-name="allowDrag"/>
          <el-table-column
            fixed
            label="字段列名"
            prop="name"
            min-width="130"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="字段描述" fixed min-width="180">
            <template v-slot="scope">
              <el-input v-model="scope.row.comment"></el-input>
            </template>
          </el-table-column>
          <el-table-column
            label="物理类型"
            prop="type"
            min-width="80"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="Java类型" min-width="110">
            <template v-slot="scope">
              <el-select v-model="scope.row.javaType">
                <el-option
                  v-for="dict in dict.type.gen_java_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="java属性" min-width="130">
            <template v-slot="scope">
              <el-input v-model="scope.row.javaField"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="隐藏" min-width="50">
            <template v-slot="scope">
              <el-checkbox v-model="scope.row.hide" @change="handleHideChange(scope.row)"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="新增" min-width="50">
            <template v-slot="scope">
              <el-checkbox v-model="scope.row.insert"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="编辑" min-width="50">
            <template v-slot="scope">
              <el-checkbox v-model="scope.row.edit"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="查看" min-width="50">
            <template v-slot="scope">
              <el-checkbox v-model="scope.row.view"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="导入" min-width="50">
            <template v-slot="scope">
              <el-checkbox v-model="scope.row.import"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="导出" min-width="50">
            <template v-slot="scope">
              <el-checkbox v-model="scope.row.export"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="唯一" min-width="50">
            <template v-slot="scope">
              <el-checkbox v-model="scope.row.unique"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="必填" min-width="50">
            <template v-slot="scope">
              <el-checkbox v-model="scope.row.required"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="列表" min-width="50">
            <template v-slot="scope">
              <el-checkbox v-model="scope.row.list"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="查询" min-width="50">
            <template v-slot="scope">
              <el-checkbox v-model="scope.row.query"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="查询方式" min-width="100">
            <template v-slot="scope">
              <el-select v-model="scope.row.queryType">
                <el-option
                  v-for="dict in dict.type.gen_query_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="显示类型" min-width="150">
            <template v-slot="scope">
              <el-select v-model="scope.row.htmlType">
                <el-option
                  v-for="dict in dict.type.gen_display_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="字典类型" min-width="150">
            <template v-slot="scope">
              <el-select v-model="scope.row.dictType" clearable filterable placeholder="请选择">
                <el-option
                  v-for="dict in dictOptions"
                  :key="dict.dictType"
                  :label="dict.name"
                  :value="dict.code"
                >
                  <span style="float: left">{{ dict.name }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ dict.code }}</span>
                </el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="生成信息" name="genInfo">
        <gen-info-form ref="genInfo" :info="info" :option="option" :tables="tables" :tableSubs="tableSubs"
                       :modules="modules" :menus="menus" @moduleChange="handleModuleChange"
                       @subTableChange="handleSubTableChange"
        />
      </el-tab-pane>
    </el-tabs>
    <el-form label-width="100px">
      <el-form-item style="text-align: center;margin-left:-100px;margin-top:10px;">
        <el-button type="primary" @click="submitForm()">提交</el-button>
        <el-button @click="close()">返回</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import basicInfoForm from './basicInfoForm'
import genInfoForm from './genInfoForm'
import Sortable from 'sortablejs'
import { optionDictTypeApi } from '@/api/tenant/dict/dictType'
import { editGenApi, getSubGenApi, listGenApi, listGenColumnApi } from '@/api/gen/generate/gen'
import { optionModuleApi } from '@/api/system/authority/module'
import { getMenuRouteListApi } from '@/api/system/authority/menu'
import { MenuTypeEnum } from '@enums/system'
import store from '@/store'
import { GenIndexGo } from '@enums/gen'

export default {
  name: 'GenEdit',
  dicts: ['gen_java_type', 'gen_query_type', 'gen_display_type'],
  components: {
    basicInfoForm,
    genInfoForm
  },
  data() {
    return {
      // 选中选项卡的 name
      activeName: 'columnInfo',
      // 表格的高度
      tableHeight: document.documentElement.scrollHeight - 245 + 'px',
      // 表列信息
      columns: [],
      // 字典信息
      dictOptions: [],
      // 模块信息
      modules: [],
      // 菜单信息
      menus: [],
      // 表信息
      tables: [],
      // 表字段信息
      tableSubs: [],
      // 表详细信息
      info: {},
      // 表参数详细信息
      option: {}
    }
  },
  created() {
    const id = this.$route.params && this.$route.params.id
    if (id) {
      /** 查询生成表配置详细 | 带子数据 */
      getSubGenApi(id).then(res => {
        this.info = res.data
        this.columns = this.info.subList
        this.option = JSON.parse(this.info.options)
        this.handleModuleChange(this.option.parentModuleId)
        this.handleSubTableChange(this.option.subForeignId)
      })
      /** 查询字典下拉列表 */
      optionDictTypeApi().then(response => {
        this.dictOptions = response.data.items
      })
      /** 查询模块下拉列表 */
      optionModuleApi().then(response => {
        this.modules = response.data.items
      })
      /** 查询业务表列表 */
      listGenApi().then(response => {
        this.tables = response.data.items
      })
    }
  },
  methods: {
    /** 提交按钮 */
    submitForm() {
      const basicForm = this.$refs.basicInfo.$refs.basicInfoForm
      const genForm = this.$refs.genInfo.$refs.genInfoForm
      Promise.all([basicForm, genForm].map(this.getFormPromise)).then(res => {
        const validateResult = res.every(item => !!item)
        if (validateResult) {
          this.info.options = JSON.stringify(this.option)
          editGenApi(this.info).then(res => {
            this.$modal.msgSuccess(res.msg)
            this.close()
          })
        } else {
          this.$modal.msgError('表单校验未通过，请检查提交内容!')
        }
      })
    },
    /** 模块切换操作 */
    handleModuleChange(moduleId) {
      if (moduleId !== undefined) {
        /** 查询菜单下拉列表 */
        getMenuRouteListApi(moduleId, MenuTypeEnum.DIR).then(response => {
          this.menus = response.data
        })
      } else {
        this.menus = []
      }
    },
    /** 关联子表的表名切换操作 */
    handleSubTableChange(tableId) {
      if (tableId !== undefined) {
        /** 查询生成表的字段列表 */
        listGenColumnApi(tableId).then(response => {
          this.tableSubs = response.data.items
        })
      } else {
        this.tableSubs = []
      }
    },
    /** 隐藏操作 */
    handleHideChange(row) {
      if (row.hide) {
        row.insert = false
        row.edit = false
        row.view = false
        row.import = false
        row.export = false
        row.required = false
        row.list = false
        row.query = false
      }
    },
    getFormPromise(form) {
      return new Promise(resolve => {
        form.validate(res => {
          resolve(res)
        })
      })
    },
    /** 关闭按钮 */
    close() {
      const route = store.getters.routePath[GenIndexGo]
      this.$tab.closeOpenPage(route)
    }
  },
  mounted() {
    const el = this.$refs.dragTable.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
    const sortable = Sortable.create(el, {
      handle: '.allowDrag',
      onEnd: evt => {
        const targetRow = this.columns.splice(evt.oldIndex, 1)[0]
        this.columns.splice(evt.newIndex, 0, targetRow)
        for (let index in this.columns) {
          this.columns[index].sort = parseInt(index) + 1
        }
      }
    })
  }
}
</script>
