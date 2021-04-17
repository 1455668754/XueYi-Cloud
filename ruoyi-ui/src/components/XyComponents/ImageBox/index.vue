<template>
  <div class="ImageBox">
    <div :class="imageChoice.focus?'choice-box choice-box-focus':'choice-box'"
         @mouseenter="imageChoice.focus=true"
         @mouseleave="imageChoice.focus=false" @click="imageChoiceClick">
      <i class="el-icon-plus choice-icon"/>
    </div>
    <el-scrollbar :style="'height:100px; width:'+ width+';'" :native="false" :noresize="false" tag="section"
                  v-show="imageList.length>0">
      <draggable
        :list="imageList"
        v-bind="$attrs"
        class="main-box"
        animation="300">
        <div :class="item.hiddenVisible?'image-box image-box-focus':'image-box'" v-for="item in imageList"
             :key="item.materialId" @mouseenter="item.hiddenVisible=true"
             @mouseleave="item.hiddenVisible=false">
          <el-image
            class="image-view"
            :src="item.materialUrl"
            fit="contain"/>
          <svg-icon slot="prefix" icon-class="xy-cancel"
                    class="image-cancel image-cancel-focus"
                    v-if="item.hiddenVisible" @click="imageCancel(item)"/>
        </div>
      </draggable>
    </el-scrollbar>
    <Material visible="true" v-model="imageList" :visible.sync="imageChoice.visible" :max="max" :clear="clear"/>
  </div>
</template>

<script>
import Material from "@/components/XyComponents/Material/index";
import draggable from 'vuedraggable';

export default {
  name: "ImageBox",
  components: {Material, draggable},
  model: {
    prop: 'list',
    event: 'change'
  },
  props: {
    /** 传入list参数 */
    list: {
      type: String,
      default: []
    },
    /** 图片上传最大数量参数 默认为 1 */
    width: {
      type: String,
      default: '450px'
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
      imageChoice: {
        focus: false,
        visible: false
      },
      imageList: []
    }
  },
  watch: {
    imageList(val, oldVal) {//普通的watch监听
      this.changeList();
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    /** 初始化方法 */
    init() {
      this.imageList = [];
      if (this.list !== null && this.list !== '') {
        this.imageList = JSON.parse(this.list);
      }
    },
    imageCancel(item) {
      for (let i = 0; i < this.imageList.length; i++) {
        if (this.imageList[i].materialId === item.materialId) {
          this.imageList.splice(i, 1);
        }
      }
    },
    changeList() {
      this.$emit('change', JSON.stringify(this.imageList));
    },
    imageChoiceClick() {
      this.imageChoice.visible = true
    }
  },
}
</script>

<style lang="scss" scoped>

.main-box {
  display: inline-flex;

  .image-box {
    width: 80px;
    height: 80px;
    border: 1px solid #e3e3e3;
    margin: 6px 5px;
    display: inline-flex;
    position: relative;

    .image-view {
      width: 80px;
      height: 80px;
      padding: 4px;
    }

    .image-cancel {
      position: absolute;
      width: 18px;
      height: 18px;
      top: -8px;
      right: -8px;
    }

    .image-cancel-focus {
      cursor: pointer;
    }
  }
}

.choice-box {
  width: 80px;
  height: 80px;
  border: 1px dashed #e3e3e3;
  margin: 6px 5px;
  display: inline-flex;

  .choice-icon {
    text-align: center;
    line-height: 80px;
    width: 80px;
    height: 80px;
  }
}

.choice-box-focus {
  cursor: pointer;
  border-color: #387ed6;
}

.image-box-focus {
  cursor: move;
}

.ImageBox {

}
</style>

<style scoped>
.ImageBox >>> .el-scrollbar .el-scrollbar__wrap {
  overflow-x: hidden;
}

.ImageBox >>> .el-scrollbar .el-scrollbar__wrap .el-scrollbar__view {
  white-space: nowrap;
  display: inline-block;
}
</style>
