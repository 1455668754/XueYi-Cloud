import variables from '@/assets/styles/element-variables.scss'
import defaultSettings from '@/settings'

const { sideTheme, showSettings, topNav, tagsView, fixedHeader, sidebarLogo, libraryId, siteId, systemId, homePageName, homePageIcon, baseSystemUrl, dynamicTitle } = defaultSettings

const storageSetting = JSON.parse(localStorage.getItem('layout-setting')) || ''
const librarySetting = localStorage.getItem('library-setting') !== null ? JSON.parse(localStorage.getItem('library-setting').replace(/\"libraryId\":(\d+)/, '"libraryId": "$1"')) || '' : ''
const siteSetting = localStorage.getItem('site-setting') !== null ? JSON.parse(localStorage.getItem('site-setting').replace(/\"siteId\":(\d+)/, '"siteId": "$1"')) || '' : ''

const state = {
  title: '',
  theme: storageSetting.theme || variables.theme,
  sideTheme: storageSetting.sideTheme || sideTheme,
  showSettings: showSettings,
  topNav:  storageSetting.topNav === undefined ? topNav : storageSetting.topNav,
  tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,
  fixedHeader: storageSetting.fixedHeader === undefined ? fixedHeader : storageSetting.fixedHeader,
  sidebarLogo: storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,
  dynamicTitle: storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle,
  libraryId: librarySetting.libraryId === undefined ? libraryId : librarySetting.libraryId,
  siteId: siteSetting.siteId === undefined ? siteId : siteSetting.siteId,
  systemId: systemId,
  homePageName: homePageName,
  homePageIcon: homePageIcon,
  baseSystemUrl: baseSystemUrl
}

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  }
}

const actions = {
  // 修改布局设置
  changeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data)
  },
  // 设置网页标题
  setTitle({ commit }, title) {
    state.title = title
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
