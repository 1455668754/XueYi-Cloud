import { login, logout, getInfo, refreshToken, getEnterpriseProfile } from '@api/login'
import { getToken, setToken, setExpiresIn, removeToken } from '@utils/auth'

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    enterpriseName: '',
    enterpriseSystemName: '',
    logo: '',
    roles: [],
    permissions: []
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
    SET_ENTERPRISENAME: (state, enterpriseName) => {
      state.enterpriseName = enterpriseName
    },
    SET_ENTERPRISESYSTEMNAME: (state, enterpriseSystemName) => {
      state.enterpriseSystemName = enterpriseSystemName
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_LOGO: (state, logo) => {
      state.logo = logo
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
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
          commit('SET_TOKEN', data.access_token)
          setExpiresIn(data.expires_in)
          commit('SET_EXPIRES_IN', data.expires_in)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          const user = res.data.user
          const avatar = user.avatar === "" ? require("@assets/images/profile.jpg") : user.avatar;
          if (res.data.roles && res.data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', res.data.roles)
            commit('SET_PERMISSIONS', res.data.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_NAME', user.userName)
          commit('SET_AVATAR', avatar)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
        getEnterpriseProfile().then(res => {
          const enterprise = res.data
          const systemName = enterprise.enterpriseSystemName === "" ? "雪忆管理系统" : enterprise.enterpriseSystemName;
          const logo = enterprise.logo === "" ? require("@assets/images/logo.jpg") : enterprise.logo;
          commit('SET_ENTERPRISENAME', enterprise.enterpriseName)
          commit('SET_ENTERPRISESYSTEMNAME', systemName)
          commit('SET_LOGO', logo)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 刷新token
    RefreshToken({commit, state}) {
      return new Promise((resolve, reject) => {
        refreshToken(state.token).then(res => {
          setExpiresIn(res.data)
          commit('SET_EXPIRES_IN', res.data)
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
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
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
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
