import { login, logout, refreshToken, getEnterpriseInfo, getUserInfo } from '@/api/login'
import { getToken, setToken, setExpiresIn, removeToken } from '@/utils/auth'
import {
  MODULE_KEY,
  SET_AVATAR_KEY, SET_ENTERPRISE_NAME_KEY, SET_EXPIRES_IN_KEY, SET_IS_LESSOR_KEY, SET_LOGO_KEY,
  SET_NAME_KEY,
  SET_PERMISSIONS_KEY,
  SET_ROLES_KEY,
  SET_ROUTE_PATH_KEY, SET_SYSTEM_NAME_KEY, SET_TOKEN_KEY,
  TenantTypeEnum
} from '@enums'

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    enterpriseName: '',
    systemName: '',
    logo: '',
    isLessor: false,
    roles: [],
    permissions: [],
    routePathMap: null
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_EXPIRES_IN: (state, time) => {
      state.expires_in = time
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ENTERPRISE_NAME: (state, enterpriseName) => {
      state.enterpriseName = enterpriseName
    },
    SET_SYSTEM_NAME: (state, systemName) => {
      state.systemName = systemName
    },
    SET_LOGO: (state, logo) => {
      state.logo = logo
    },
    SET_IS_LESSOR: (state, isLessor) => {
      state.isLessor = isLessor
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_ROUTE_PATH: (state, routePathMap) => {
      state.routePathMap = routePathMap
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const enterpriseName = userInfo.enterpriseName.trim()
      const userName = userInfo.userName.trim()
      const password = userInfo.password
      const code = userInfo.code
      const uuid = userInfo.uuid
      return new Promise((resolve, reject) => {
        login(enterpriseName, userName, password, code, uuid).then(res => {
          let data = res.data
          setToken(data.access_token)
          commit(SET_TOKEN_KEY, data.access_token)
          setExpiresIn(data.expires_in)
          commit(SET_EXPIRES_IN_KEY, data.expires_in)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo().then(res => {
          const user = res.data.user
          const avatar = (user.avatar === '' || user.avatar == null) ? require('@/assets/images/profile.jpg') : user.avatar
          if (res.data.roles && res.data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit(SET_ROLES_KEY, res.data.roles)
            commit(SET_PERMISSIONS_KEY, res.data.permissions)
          } else {
            commit(SET_ROLES_KEY, ['ROLE_DEFAULT'])
          }
          commit(SET_NAME_KEY, user.userName)
          commit(SET_AVATAR_KEY, avatar)
          commit(SET_ROUTE_PATH_KEY, res.data.routes)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
        getEnterpriseInfo().then(res => {
          const enterprise = res.data
          const systemName = enterprise.systemName
          const logo = enterprise.logo
          const isLessor = enterprise.isLessor === TenantTypeEnum.ADMIN
          commit(SET_ENTERPRISE_NAME_KEY, enterprise.enterpriseName)
          commit(SET_SYSTEM_NAME_KEY, systemName)
          commit(SET_LOGO_KEY, logo)
          commit(SET_IS_LESSOR_KEY, isLessor)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 刷新token
    RefreshToken({ commit, state }) {
      return new Promise((resolve, reject) => {
        refreshToken(state.token).then(res => {
          setExpiresIn(res.data)
          commit(SET_EXPIRES_IN_KEY, res.data)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          localStorage.removeItem(MODULE_KEY)
          commit(SET_TOKEN_KEY, '')
          commit(SET_ROLES_KEY, [])
          commit(SET_PERMISSIONS_KEY, [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit(SET_TOKEN_KEY, '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
