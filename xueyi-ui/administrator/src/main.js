import Vue from 'vue'

import Cookies from 'js-cookie'

import Element from 'element-ui'

import '@assets/styles/element-variables.scss'
import '@assets/styles/index.scss' // global css
import '@assets/styles/ruoyi.scss' // ruoyi css
import '@assets/styles/xueyi.scss' //xueyi css element-UI重置

import '@assets/icons' // icon
import '@utils/element' //js element-UI重置
import './permission' // permission control
import App from './App'
import store from './store'
import router from './router'
import directive from '@common/directive' //directive
import {download} from '@utils/request'

import {getDicts, getConfigKey} from "@api/common/common"
import {parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree} from "@utils/ruoyi"
import {acquisition, updateParamIds, sortOrderListOnlyDynamic, excludeRepeatList, excludeEmptyList, sortOrderList, mergeTableRow, isMobile} from "@utils/xueyi"
import RightToolbar from "@basicsComponents/RightToolbar"   // 自定义表格工具组件
import ImageUpload from "@basicsComponents/ImageUpload"   // 图片上传组件
import FileUpload from "@basicsComponents/FileUpload"   // 文件上传组件
import Pagination from "@basicsComponents/Pagination"
import ImageBox from "@customComponents/ImageBox"   // 图片管理组件
import DictTag from '@basicsComponents/DictTag'   // 字典标签组件
import Editor from "@basicsComponents/Editor"   // 富文本组件
import VueMeta from 'vue-meta'    // 头部标签组件

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree

Vue.prototype.acquisition = acquisition
Vue.prototype.updateParamIds = updateParamIds

Vue.prototype.sortOrderList = sortOrderList
Vue.prototype.excludeEmptyList = excludeEmptyList
Vue.prototype.excludeRepeatList = excludeRepeatList
Vue.prototype.sortOrderListOnlyDynamic = sortOrderListOnlyDynamic
Vue.prototype.mergeTableRow = mergeTableRow
Vue.prototype.isMobile = isMobile

Vue.prototype.msgSuccess = function (msg) {
  this.$message({showClose: true, message: msg, type: "success"})
}

Vue.prototype.msgError = function (msg) {
  this.$message({showClose: true, message: msg, type: "error"})
}

Vue.prototype.msgWarning = function (msg) {
  this.$message({showClose: true, message: msg, type: "warning"})
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg)
}

// 全局组件挂载
Vue.component('ImageBox', ImageBox)
Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('FileUpload', FileUpload)
Vue.component('ImageUpload', ImageUpload)

Vue.use(directive)
Vue.use(VueMeta)
/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
