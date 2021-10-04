<template>
  <div class="app-container">
    <div class="wrapper-container" v-show="showSearch">
      <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
        <el-form-item label="系统名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入系统名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="跳转类型" prop="type">
          <el-select v-model="queryParams.type" placeholder="请选择跳转类型" clearable size="small">
            <el-option
              v-for="dict in dict.type.sys_jump_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
            <el-option
              v-for="dict in dict.type.sys_show_hide"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="wrapper-container">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['system:system:add']"
          >新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['system:system:edit']"
          >修改
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['system:system:remove']"
          >删除
          </el-button>
        </el-col>

        <right-toolbar :showSearch.sync="showSearch" :sortVisible="false" @queryTable="getList" :columns="columns"/>
      </el-row>

      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="系统名称" align="center" prop="name" v-if="columns[0].visible"
                         :show-overflow-tooltip="true" min-width="120"/>
        <el-table-column label="系统图片" align="center" prop="imageUrl" v-if="columns[1].visible"
                         :show-overflow-tooltip="true" min-width="120">
          <template slot-scope="scope">
            <el-image
              style="width: 80px; height: 80px"
              :src="scope.row.imageUrl != null?JSON.parse(scope.row.imageUrl).materialUrl:null"
              fit="contain"/>
          </template>
        </el-table-column>
        <el-table-column label="跳转类型" align="center" prop="type" v-if="columns[2].visible"
                         :show-overflow-tooltip="true" min-width="120">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_jump_type" :value="scope.row.type"/>
          </template>
        </el-table-column>
        <el-table-column label="路由|外链" align="center" prop="route" v-if="columns[3].visible"
                         :show-overflow-tooltip="true" min-width="120"/>
        <el-table-column label="状态" align="center" prop="status" v-if="columns[4].visible"
                         :show-overflow-tooltip="true" min-width="120">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="STATUS.NORMAL"
              :inactive-value="STATUS.DISABLE"
              @change="handleStatusChange(scope.row)"/>
          </template>
        </el-table-column>
        <el-table-column label="系统简介" align="center" prop="remark" v-if="columns[5].visible"
                         :show-overflow-tooltip="true" min-width="180"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="120">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:system:edit']"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:system:remove']"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 添加或修改模块对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" :close-on-click-modal="false" v-dialogDrag
               v-dialogDragHeight>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="系统名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入系统名称" maxlength="7" show-word-limit/>
        </el-form-item>
        <el-form-item label="系统简介" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" maxlength="14" show-word-limit/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="跳转类型">
              <el-radio-group v-model="form.type">
                <el-radio
                  v-for="dict in dict.type.sys_jump_type"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="跳转新页" v-if="form.type === SYSTEM_TYPE.EXTERNAL">
              <el-radio-group v-model="form.isNew">
                <el-radio
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="公共系统" v-if="IS_LESSOR">
          <el-radio-group v-model="form.isCommon">
            <el-radio
              v-for="dict in dict.type.sys_yes_no"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="form.type === SYSTEM_TYPE.INSIDE?'跳转路由':'跳转链接'" prop="route">
          <el-input v-model="form.route" :placeholder="form.type === SYSTEM_TYPE.INSIDE?'请输入跳转路由':'请输入跳转链接'"/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="显示状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_show_hide"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="sort">
              <el-input-number size="mini" v-model="form.sort" :min="0" :max="125"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="系统图片">
          <ImageBox v-model="form.imageUrl" :clear="true"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listSystem, getSystem, delSystem, addSystem, updateSystem, changeSystemStatus} from "@/api/system/system"
import ImageBox from "@customComponents/ImageBox"
import store from "@/store"
import {IS_COMMON, STATUS, VISIBLE} from "@constant/constants"
import {IS_NEW, SYSTEM_TYPE} from "@constant/authorityContants"

export default {
  name: "System",
  dicts: ['sys_jump_type', 'sys_show_hide', 'sys_yes_no'],
  components: {ImageBox},
  data() {
    return {
      //常量区
      STATUS: STATUS,
      SYSTEM_TYPE: SYSTEM_TYPE,
      VISIBLE: VISIBLE,
      IS_NEW: IS_NEW,
      IS_COMMON: IS_COMMON,
      IS_LESSOR: store.getters.isLessor,
      enterpriseName: store.getters.enterpriseName,
      // 遮罩层
      loading: true,
      // 提交状态
      submitLoading: false,
      // 选中数组
      ids: [],
      idNames: [],
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 显隐列信息
      columns: [
        {key: 0, label: `系统名称`, visible: true},
        {key: 1, label: `系统图片`, visible: true},
        {key: 2, label: `跳转类型`, visible: true},
        {key: 3, label: `跳转路由`, visible: true},
        {key: 4, label: `状态`, visible: true},
        {key: 5, label: `系统简介`, visible: true}
      ],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: null,
        status: null
      },
      // 表单参数
      form: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 表单校验
      rules: {
        name: [
          {required: true, message: "系统名称不能为空", trigger: "blur"}
        ],
        type: [
          {required: true, message: "跳转类型不能为空", trigger: "blur"}
        ],
        route: [
          {required: true, message: "路由|外链不能为空", trigger: "blur"}
        ],
        remark: [
          {required: true, message: "系统简介不能为空", trigger: "blur"}
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询模块列表 */
    getList() {
      this.loading = true
      listSystem(this.queryParams).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
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
        systemId: null,
        name: null,
        imageUrl: null,
        isCommon: IS_COMMON.FALSE,
        isNew: IS_NEW.NO,
        type: SYSTEM_TYPE.INSIDE,
        route: null,
        sort: 0,
        status: STATUS.NORMAL,
        remark: null
      }
      this.resetForm("form")
      this.submitLoading = false
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.systemId)
      this.idNames = selection.map(item => item.name)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加模块"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const systemId = row.systemId || this.ids[0]
      getSystem({systemId: systemId}).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改模块"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.submitLoading = true
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.systemId != null) {
            updateSystem(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            }).catch(() => {
              this.submitLoading = false
            })
          } else {
            addSystem(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            }).catch(() => {
              this.submitLoading = false
            })
          }
        } else {
          this.submitLoading = false
        }
      })
    },
    // 模块状态修改
    handleStatusChange(row) {
      let text = row.status === STATUS.NORMAL ? "启用" : "停用"
      this.$modal.confirm('确认要"' + text + '""' + row.name + '"模块吗?').then(function () {
        return changeSystemStatus({systemId: row.systemId, status: row.status})
      }).then(() => {
        this.$modal.msgSuccess(text + "成功")
      }).catch(function () {
        row.status = row.status === STATUS.NORMAL ? STATUS.DISABLE : STATUS.NORMAL
      }).catch((err) => {
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const systemIds = row.systemId || this.ids
      const name = row.name || this.idNames
      let $this = this
      this.$modal.confirm('是否确认删除模块"' + name + '"?', "警告").then(function () {
        return delSystem($this.updateParamIds(systemIds))
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch((err) => {
      })
    }
  }
}
</script>
