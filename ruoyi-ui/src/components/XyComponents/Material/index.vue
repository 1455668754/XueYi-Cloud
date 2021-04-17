<template>
  <div>
    <el-dialog class="dialogTop" :visible.sync="visible" width="950px" :close-on-click-modal="false"
               :modal="true" append-to-body :before-close="handleClose">
      <template slot="title">
        <div class="title-text">
          <span class="title-main">添加图片</span><span class="title-supplement">（只能添加jpg,jpeg,bmp,png,gif,大小不超过5M。）</span>
        </div>
      </template>
      <div class="tabs-card">
        <el-tabs v-model="activeName" type="card" tabPosition="left" style="height: 500px">
          <el-tab-pane label="我的文件" name="first">
            <el-container>
              <el-header class="container-header">
                <el-row :gutter="20" type="flex" justify="space-between">
                  <el-col :xs="9" :sm="9" :md="9" :lg="9" :xl="9">
                    <el-row type="flex" justify="space-between">
                      <el-col>
                        <el-upload
                          ref="upload"
                          :limit="5"
                          accept=".jpg, .jpeg, .bmp, .png, .gif"
                          :action="upload.url"
                          :headers="upload.headers"
                          :file-list="upload.fileList"
                          :data="{'folderId':form.folderId}"
                          :before-upload="beforeUpload"
                          :on-progress="handleFileUploadProgress"
                          :on-success="handleFileSuccess"
                          :on-error="handleFileError"
                          :show-file-list="false">
                          <el-button slot="trigger" size="medium" icon="el-icon-upload2" :loading="upload.isUploading"
                                     v-hasPermi="['system:material:add']">直接上传
                          </el-button>
                        </el-upload>
                      </el-col>
                      <el-col>
                        <el-button class="hidden-xs-only" size="medium" icon="el-icon-folder-add"
                                   @click="handleAddFolder"
                                   :loading="upload.isUploading"
                                   v-hasPermi="['system:folder:add']">新增文件夹
                        </el-button>
                      </el-col>
                    </el-row>
                  </el-col>
                  <el-col :xs="4" :sm="4" :md="4" :lg="4" :xl="4" :pull="2">
                    <el-input placeholder="搜索所有文件" v-model="form.folderName"
                              class="header-search" @change="searchChange">
                      <i :class="search.searchVisible?'el-icon-search el-input__icon':'el-icon-error el-input__icon'"
                         slot="suffix" @click="searchIconClick"/>
                    </el-input>
                  </el-col>
                </el-row>
              </el-header>
              <el-main class="container-main">
                <el-breadcrumb separator-class="el-icon-arrow-right" class="div-breadcrumb">
                  <el-breadcrumb-item v-for="(item,index) in breadcrumbList" v-if="breadcrumbList.length>1">
                    <span :style="index<breadcrumbList.length-1?item.cssStyle:null"
                          :class="index<breadcrumbList.length-1?'breadcrumb-item':null"
                          @mouseenter="item.cssStyle='cursor: pointer;'"
                          @mouseleave="item.cssStyle=''"
                          @click="breadcrumbJump(item,index)">{{ item.folderName }}</span></el-breadcrumb-item>
                </el-breadcrumb>
                <el-scrollbar style="height: 425px;" :native="false" wrapStyle="" wrapClass="" viewClass="" viewStyle=""
                              :noresize="false" tag="section">
                  <el-row>
                    <el-col :xs="3" :sm="3" :md="3" :lg="3" :xl="3" v-for="(item,index) in materialFolder.children">
                      <div class="material-div">
                        <div class="image-div" @mouseenter="item.hiddenVisible=true"
                             @mouseleave="item.hiddenVisible=false">
                          <el-image
                            :class="item.hiddenVisible===true?'folder-style image-hand':'folder-style'"
                            :src="upload.folderImg"
                            fit="contain" @click="breadcrumbJump(item,index)">
                          </el-image>
                          <svg-icon slot="prefix" icon-class="xy-cancel"
                                    :class="item.hiddenVisible?'folder-cancel image-hand':'folder-cancel'"
                                    v-if="item.hiddenVisible" @click="imageDelete(0,item,index)"/>
                        </div>
                        <div class="image-box">
                          <el-input
                            class="image-input"
                            v-model="item.folderName"
                            @change="updateMaterialNick(0, item, index)"
                            ref="ImageNickInput"
                            size="mini">
                          </el-input>
                        </div>
                      </div>
                    </el-col>
                    <el-col :xs="3" :sm="3" :md="3" :lg="3" :xl="3"
                            v-for="(item,index) in materialFolder.materialList">
                      <div class="material-div">
                        <div class="image-div" @mouseenter="item.hiddenVisible=true"
                             @mouseleave="item.hiddenVisible=false"
                        >
                          <div @click="imageChoiceClick(item,index)">
                            <el-image
                              :class="item.imageChoice===true?'image-style image-border image-hand':item.hiddenVisible===true?'image-style image-border image-hand':'image-style'"
                              :src="item.materialUrl"
                              fit="contain">
                            </el-image>
                            <svg-icon slot="prefix" icon-class="xy-choice2" class="image-choice"
                                      v-if="item.imageChoice"/>
                          </div>
                          <svg-icon slot="prefix" icon-class="xy-cancel"
                                    :class="item.hiddenVisible?'image-cancel image-hand':'image-cancel'"
                                    v-if="item.hiddenVisible" @click="imageDelete(1, item, index)"/>
                        </div>
                        <div class="image-box">
                          <el-input
                            class="image-input"
                            v-model="item.materialNick"
                            @change="updateMaterialNick(1, item, index)"
                            ref="ImageNickInput"
                            size="mini">
                          </el-input>
                        </div>
                      </div>
                    </el-col>
                  </el-row>
                </el-scrollbar>
              </el-main>
            </el-container>
          </el-tab-pane>
          <el-tab-pane label="图库中心" name="second"></el-tab-pane>
        </el-tabs>
      </div>
      <div slot="footer" class="dialog-footer dialogTop-footer">
        <el-button class="dialogTop-footer-button" @click="handleClose">取 消</el-button>
        <el-button class="dialogTop-footer-button" type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog class="minor-dialogTop" :visible.sync="visible" width="149px" :close-on-click-modal="false"
               :show-close="false" :modal="false" v-if="max>1" append-to-body>
      <template slot="title">
        <div class="minor-title-text">
          <span class="minor-increase">待添加({{ choiceList.length }}/{{ max }})</span>
        </div>
      </template>
      <div class="tabs-card minor-panel">
        <el-scrollbar style="height: 500px;" :native="false" wrapStyle="" wrapClass="" viewClass=""
                      viewStyle="" :noresize="false" tag="section">
          <div style="margin-bottom: 10px;"/>
          <draggable
            :list="choiceList"
            v-bind="$attrs"
            animation="300">
            <div class="choice-div" v-for="(item,index) in choiceList">
              <div class="choice-image-div" @mouseenter="item.hiddenVisible=true"
                   @mouseleave="item.hiddenVisible=false">
                <el-image
                  :class="item.hiddenVisible===true?'choice-image-style choice-image-border choice-image-hand':'choice-image-style'"
                  :src="item.materialUrl"
                  fit="contain"/>
                <svg-icon slot="prefix" icon-class="xy-cancel"
                          :class="item.hiddenVisible?'choice-image-cancel choice-image-hand':'choice-image-cancel'"
                          v-if="item.hiddenVisible" @click="imageDeselectClick(item,index)"/>
              </div>
            </div>
          </draggable>
          <div style="margin-top: 10px;"/>
        </el-scrollbar>
      </div>
      <div slot="footer" class="dialog-footer minor-dialog-footer">
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listMaterial,
  addFolder,
  delFolder,
  delMaterial,
  parentFolder,
  updateFolder,
  updateMaterial
} from "@/api/common/material";
import {getToken} from "@/utils/auth";
import folderImg from '@/assets/images/xy-folder.png'
import 'element-ui/lib/theme-chalk/display.css';
import draggable from 'vuedraggable';

export default {
  name: "Material",
  components: {draggable},
  model: {
    prop: 'list',
    event: 'change'
  },
  props: {
    /** dialog显隐参数 */
    visible: {
      type: Boolean,
      default: false
    },
    /** 传入list参数 */
    list: {
      type: Array,
      default: []
    },
    /** 图片上传最大数量参数 默认为 1 */
    max: {
      type: Number,
      default: 1
    },
    /** 传入list是否转空参数 默认为 false */
    clear: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      //活跃单元
      activeName: "first",
      // 上传参数
      upload: {
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/material/upload",
        // 上传的文件列表
        fileList: [],
        //白名单
        whiteList: ["jpg", "jpeg", "bmp", "png", "gif"],
        //文件夹位置
        folderImg: folderImg
      },
      search: {
        searchVisible: true,
        hiddenVisible: false
      },
      materialFolder: null,
      choiceList: [],
      breadcrumbList: [],
      form: {
        folderId: 0,
        folderName: ''
      }
    }
  },
  mounted() {
    this.init()
  },
  watch: {
    list(val, oldVal) {//普通的watch监听
      this.init();
    }
  },
  methods: {
    /** 初始化方法 */
    init() {
      this.creatChoiceList();
      this.getList();
    },
    creatChoiceList() {
      this.choiceList = [];
      if (this.list !== null && this.clear !== true) {
        for (let i = 0; i < this.list.length; i++) {
          this.choiceList.push({
            "materialId": this.list[i].materialId,
            "materialUrl": this.list[i].materialUrl,
            "materialOriginalUrl": this.list[i].materialOriginalUrl,
            "hiddenVisible": false
          });
        }
      }
    },
    /** 查询素材信息列表 */
    getList() {
      listMaterial(this.form).then(response => {
        this.materialFolder = response.data;
        for (let i = 0; i < this.choiceList.length; i++) {
          for (let j = 0; j < this.materialFolder.materialList.length; j++) {
            if (this.choiceList[i].materialId === this.materialFolder.materialList[j].materialId) {
              this.materialFolder.materialList[j].imageChoice = true
            }
          }
        }
      });
      parentFolder(this.form).then(response => {
        this.breadcrumbList = response.data;
      });
    },
    /** 新建文件夹 */
    handleAddFolder() {
      this.upload.isUploading = true;
      addFolder(this.form).then(response => {
        this.msgSuccess("新增成功");
        this.getList();
        this.upload.isUploading = false;
      });
    },
    /** 搜索内容变动进行查询 */
    searchChange() {
      this.search.searchVisible = this.form.folderName === '';
      this.getList();
    },
    /** true 搜索/false 重置 */
    searchIconClick() {
      if (this.search.searchVisible) {
        this.getList();
      } else {
        this.form.folderName = null;
        this.getList();
        this.search.searchVisible = true;
      }
    },
    breadcrumbJump(item, index) {
      this.form.folderName = null;
      this.form.folderId = item.folderId;
      this.getList()
    },
    imageChoiceClick(item, index) {
      //取消选择
      if (item.imageChoice) {
        for (let i = 0; i < this.choiceList.length; i++) {
          if (this.choiceList[i].materialId === item.materialId) {
            this.choiceList.splice(i, 1);
          }
        }
        item.imageChoice = false;
      }//选择
      else {
        if (this.choiceList.length < this.max) {
          this.choiceList.push({
            "materialId": item.materialId,
            "materialNick": item.materialNick,
            "materialUrl": item.materialUrl,
            "materialOriginalUrl": item.materialOriginalUrl,
            "hiddenVisible": false
          });
          item.imageChoice = true
        }
      }
    },
    imageDeselectClick(item, index) {
      for (let i = 0; i < this.choiceList.length; i++) {
        if (this.choiceList[i].materialId === item.materialId) {
          this.choiceList.splice(i, 1);
        }
      }
      for (let i = 0; i < this.materialFolder.materialList.length; i++) {
        if (this.materialFolder.materialList[i].materialId === item.materialId) {
          this.materialFolder.materialList[i].imageChoice = false
        }
      }
    },
    /** type 0文件夹 1图片 */
    updateMaterialNick(type, item, index) {
      if (type === 0) {
        updateFolder(item).then(response => {
          this.msgSuccess("修改成功");
          this.getList();
        });
      } else if (type === 1) {
        updateMaterial(item).then(response => {
          this.msgSuccess("修改成功");
          this.getList();
        });
      }
    },
    /** type 0文件夹 1图片 */
    imageDelete(type, item, index) {
      this.$confirm(type === 0 ? '您确定将该文件夹以及该文件夹内的所有文件放入回收站吗？' : '将文件放入回收站会影响使用该文件的地方，确定放入回收站吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(function () {
        if (type === 0) {
          return delFolder(item.folderId);
        } else if (type === 1) {
          return delMaterial(item.materialId);
        }
      }).then(() => {
        this.getList();
        this.$message({
          type: 'success',
          message: '删除成功!'
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    /** 上传文件之前 */
    beforeUpload(file) {
      const fileSuffix = file.name.substring(file.name.lastIndexOf(".") + 1);
      if (this.upload.whiteList.indexOf(fileSuffix) === -1) {
        this.$message({
          message: '上传文件只能是 jpg, jpeg, bmp, png, gif格式',
          type: 'warning'
        });
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 5;
      if (!isLt2M) {
        this.$message({
          message: '上传文件大小不能超过 5MB',
          type: 'warning'
        });
        return false;
      }
    },
    /** 文件上传中处理 */
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    /** 文件上传成功处理 */
    handleFileSuccess(response, file, fileList) {
      this.upload.fileList = [];
      this.upload.isUploading = false;
      this.$message({
        message: '恭喜你，这是一条成功消息',
        type: 'success'
      });
      this.getList()
    },
    /** 文件上传失败处理 */
    handleFileError(err, file, fileList) {
      this.upload.fileList = [];
      this.upload.isUploading = false;
      this.$message.error('错了哦，这是一条错误消息');
    },
    /** 确定 */
    onSubmit() {
      this.list = []
      for (let i = 0; i < this.choiceList.length; i++) {
        this.list.push({
          "materialId": this.choiceList[i].materialId,
          "materialNick": this.choiceList[i].materialNick,
          "materialUrl": this.choiceList[i].materialUrl,
          "materialOriginalUrl": this.choiceList[i].materialOriginalUrl,
          "hiddenVisible": false
        });
      }
      this.$emit('change', this.list);
      this.$emit('update:visible', false);
    },
    /** 关闭 */
    handleClose(done) {
      this.$emit('update:visible', false);
    }
  }
}
</script>

<style lang="scss" scoped>
.title-text {
  text-align: left;
  height: 10px;

  .title-main {
    font-size: 16px;
    color: #333333;
  }

  .title-supplement {
    font-size: 12px;
    color: #666666;
  }
}

.dialogTop {
  margin-top: 9vh;
  left: 21%;
  position: fixed;
}

.minor-dialogTop {
  margin-top: 9vh;
  left: calc(21% + 960px);
  position: fixed;

  .minor-title-text {
    text-align: center;
    height: 10px;

    .minor-increase {
      font-size: 12px;
      color: #000000;
      text-align: center;
      font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
    }
  }

  .minor-panel {
    //width: auto;
    border-bottom: 1px solid #E4E7ED;

    .choice-div {
      margin: 10px 38px;

      .choice-image-div {
        width: 70px;
        height: 70px;
        position: relative;

        .choice-image-style {
          width: 70px;
          height: 70px;
          border: 1px solid #E4E7ED;
          background-color: #ffffff;
        }

        .choice-image-border {
          border: 1px solid #5874d8;
        }

        .choice-image-hand {
          cursor: pointer; //鼠标变手
        }

        .choice-image-cancel {
          position: absolute;
          width: 18px;
          height: 18px;
          top: -8px;
          right: -8px;
        }
      }
    }
  }

  .minor-dialog-footer {
    height: 37px;
  }
}


.container-header {
  padding-top: 20px;

  .header-search {
    width: 162px;
  }
}

.dialogTop-footer {
  text-align: center;

  .dialogTop-footer-button {
    position: relative;
    margin: 0 25px 0 25px;
    top: -10px
  }
}

.container-main {

  .div-breadcrumb {
    margin: 6px 0 11px 0;
  }

  .breadcrumb-item {
    color: #557ce1;
  }

  .material-div {
    margin: 10px 40px 20px 0;
  }

  .image-div {
    width: 82px;
    height: 82px;
    position: relative;

    .image-style {
      width: 82px;
      height: 82px;
      border: 1px solid #E4E7ED;
      background-color: #f3f3f3;
    }

    .folder-style {
      width: 82px;
      height: 82px;
    }

    .image-border {
      border: 1px solid #5874d8;
    }

    .image-hand {
      cursor: pointer; //鼠标变手
    }

    .image-choice {
      position: absolute;
      width: 28px;
      height: 28px;
      top: 54px;
      right: 0;
    }

    .folder-cancel {
      position: absolute;
      width: 18px;
      height: 18px;
      top: 0;
      right: -6px;
    }

    .image-cancel {
      position: absolute;
      width: 18px;
      height: 18px;
      top: -8px;
      right: -8px;
    }
  }

  .image-box {
    margin-top: 5px;

    .image-input {
      width: 82px;
      vertical-align: bottom;
    }
  }
}

.item {
  margin-top: 10px;
  margin-right: 40px;
}
</style>

<style scoped>
.image-box >>> .el-input--mini .el-input__inner {
  height: 19px !important;
  line-height: 19px !important;
}

.image-box >>> .el-input__inner {
  padding: 0 !important;
  border: 1px solid #ffffff !important;
}

.image-box >>> .el-input__inner:focus {
  border-color: #fe714b !important;
}

.image-box >>> .el-input__inner:hover {
  border-color: #5874d8 !important;
}

.image-box >>> input {
  text-align: center !important;
}

.minor-panel >>> .el-scrollbar__wrap {
  overflow-x: hidden;
}

.dialogTop >>> .el-dialog {
  margin-left: 0 !important;
  margin-right: 0 !important;
}

.minor-dialogTop >>> .el-dialog {
  margin-left: 0 !important;
  margin-right: 0 !important;
}
</style>
