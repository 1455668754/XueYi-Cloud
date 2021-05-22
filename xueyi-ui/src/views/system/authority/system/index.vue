<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="系统名称" prop="systemName">
        <el-input
          v-model="queryParams.systemName"
          placeholder="请输入系统名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="跳转类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择跳转类型" clearable size="small">
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="跳转路由" prop="route">
        <el-input
          v-model="queryParams.route"
          placeholder="请输入跳转路由"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

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

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"/>
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="系统名称" align="center" prop="systemName" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="系统图片" align="center" prop="imageUrl" v-if="columns[1].visible"
                       :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-image
            style="width: 80px; height: 80px"
            :src="scope.row.imageUrl != null?JSON.parse(scope.row.imageUrl).materialUrl:null"
            fit="contain"/>
        </template>
      </el-table-column>
      <el-table-column label="跳转类型" align="center" prop="type" v-if="columns[2].visible"
                       :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-tag :type="scope.row.type==='0'?'success':'warning'">{{ typeFormat(scope.row) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="路由|外链" align="center" prop="route" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="status" v-if="columns[4].visible"
                       :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="系统简介" align="center" prop="remark" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
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
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改子系统模块对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="系统名称" prop="systemName">
          <el-input v-model="form.systemName" placeholder="请输入系统名称" maxlength="7" show-word-limit/>
        </el-form-item>
        <el-form-item label="系统简介" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" maxlength="14" show-word-limit/>
        </el-form-item>
        <el-form-item label="跳转类型">
          <el-radio-group v-model="form.type">
            <el-radio
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{ dict.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="form.type==='0'?'跳转路由':'跳转链接'" prop="route">
          <el-input v-model="form.route" :placeholder="form.type==='0'?'请输入跳转路由':'请输入跳转链接'"/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="显示状态">
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
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>

import {listSystem, getSystem, delSystem, addSystem, updateSystem, changeSystemStatus} from "@/api/system/system";
import ImageBox from "@/components/XyComponents/ImageBox";

export default {
  name: "System",
  components: {ImageBox},
  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
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
        {key: 5, label: `系统简介`, visible: true},
      ],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        systemName: null,
        type: null,
        route: null,
        status: null,
      },
      // 跳转类型字典
      typeOptions: [],
      // 状态字典
      statusOptions: [],
      // 表单参数
      form: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 表单校验
      rules: {
        systemName: [
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
    };
  },
  created() {
    this.getList();
    this.getDict();
  },
  methods: {
    /** 查询子系统模块列表 */
    getList() {
      this.loading = true;
      listSystem(this.queryParams).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询字典 */
    getDict() {
      this.getDicts("sys_jump_type").then(response => {
        this.typeOptions = response.data;
      });
      this.getDicts("sys_show_hide").then(response => {
        this.statusOptions = response.data;
      });
    },
    /** 字典内容转化 */
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        systemId: null,
        systemName: null,
        imageUrl: null,
        type: "0",
        route: null,
        sort: null,
        status: "0",
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.systemId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加子系统模块";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const systemId = row.systemId || this.ids
      getSystem(systemId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改子系统模块";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.systemId != null) {
            updateSystem(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSystem(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    // 模块状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$confirm('确认要"' + text + '""' + row.systemName + '"模块吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return changeSystemStatus(row.systemId, row.status);
      }).then(() => {
        this.msgSuccess(text + "成功");
      }).catch(function () {
        row.status = row.status === 0 ? 1 : 0;
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const systemIds = row.systemId || this.ids;
      this.$confirm('是否确认删除子系统模块编号为"' + systemIds + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delSystem(systemIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    }
  }
};
</script>
