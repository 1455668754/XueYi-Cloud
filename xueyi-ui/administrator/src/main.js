import Vue from 'vue'

import Cookies from 'js-cookie'

import Element from 'element-ui'

import './public-path';//微应用js

import 'common/src/assets/styles/element-variables.scss'
import 'common/src/assets/styles/index.scss' // global css
import 'common/src/assets/styles/ruoyi.scss' // ruoyi css
import 'common/src/assets/styles/xueyi.scss' //xueyi css element-UI重置

import './utils/element' //js element-UI重置
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' //directive
import { download } from './utils/request'

import './assets/icons' // icon
import './permission' // permission control
import { getDicts } from "./api/common/common";
import { getConfigKey } from "./api/common/common";
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree } from "./utils/ruoyi";
import { addSystem, updateParamIds } from "./utils/xueyi";  // 控制方法(必须)
import { sortOrderListOnlyDynamic, sortOrderList, mergeTableRow, isMobile } from "./utils/xueyi";  // 普通方法
import Pagination from "./components/Pagination";
// 自定义表格工具组件
import RightToolbar from "./components/RightToolbar"
// 富文本组件
import Editor from "./components/Editor"
// 文件上传组件
import FileUpload from "./components/FileUpload"
// 图片上传组件
import ImageUpload from "./components/ImageUpload"
// 字典标签组件
import DictTag from './components/DictTag'
// 头部标签组件
import VueMeta from 'vue-meta'

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

Vue.prototype.addSystem = addSystem
Vue.prototype.updateParamIds = updateParamIds

Vue.prototype.sortOrderList = sortOrderList
Vue.prototype.sortOrderListOnlyDynamic = sortOrderListOnlyDynamic
Vue.prototype.mergeTableRow = mergeTableRow
Vue.prototype.isMobile = isMobile

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}

// 全局组件挂载
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

/** 用于保存vue实例 */
let instance = null;

/** * 渲染函数 * 两种情况：主应用生命周期钩子中运行 / 微应用单独启动时运行 */
function render(props = {}) {
  const { container } = props;

  instance = new Vue({
    router,
    store,
    render: h => h(App),
  }).$mount(container ? container.querySelector('#app') : '#app');
}

/**  独立运行时，直接挂载应用 */
if (!window.__POWERED_BY_QIANKUN__) {
  render();
}

/**
 * bootstrap 只会在微应用初始化的时候调用一次，
 下次微应用重新进入时会直接调用 mount 钩子，不会再重复触发 bootstrap。
 * 通常我们可以在这里做一些全局变量的初始化，比如不会在 unmount 阶段被销毁的应用级别的缓存等。
 */
export async function bootstrap() {
  console.log('[vue] vue app bootstraped');
}

/**
 * 应用每次进入都会调用 mount 方法，通常我们在这里触发应用的渲染方法
 */
export async function mount(props) {
  console.log('[vue] props from main framework', props);
  render(props);
}

/**
 * 应用每次 切出/卸载 会调用的方法，通常在这里我们会卸载微应用的应用实例
 */
export async function unmount() {
  instance.$destroy();
  instance.$el.innerHTML = '';//解决子项目内容泄露问题
  instance = null;
}
