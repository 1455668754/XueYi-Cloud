import defaultSettings from "~../../../src/settings"

/**
 * 通用js方法封装处理
 * Copyright (c) 2021 xueyi
 */
// 添加参数 data 原始数据 | params 要获取的参数名
export function acquisition(data, ...params){
  let search = data != null ? data : {}
  for (const paramsName of params) {
    if(paramsName === 'systemId'){
      const { systemId } = defaultSettings
      search['systemId'] = systemId
    }
    if(paramsName === 'siteId'){
      const { siteId } = defaultSettings
      const siteSetting = sessionStorage.getItem('site-setting') !== null ? JSON.parse(sessionStorage.getItem('site-setting').replace(/\"siteId\":(\d+)/, '"siteId": "$1"')) || '' : ''
      search['siteId'] = siteSetting.siteId === undefined ? siteId : siteSetting.siteId
    }
    if(paramsName === 'libraryId'){
      const { libraryId } = defaultSettings
      const librarySetting = sessionStorage.getItem('library-setting') !== null ? JSON.parse(sessionStorage.getItem('library-setting').replace(/\"libraryId\":(\d+)/, '"libraryId": "$1"')) || '' : ''
      search['libraryId'] = librarySetting.libraryId === undefined ? libraryId : librarySetting.libraryId
    }
  }
  return search
}

// 数组转对象
export function updateParamIds(Ids, params, propName) {
  let search
  if (params != null) {
    search = params
    if (!search.hasOwnProperty('params')) {
      search["params"] = {}
    }
  } else {
    search = {params: {}}
  }
  if (null != Ids && '' != Ids) {
    if (typeof (Ids) === 'string') {
      Ids = Ids.split(",")
    }
    if (typeof (propName) === "undefined") {
      search.params["Ids"] = Ids
    } else {
      search.params[propName] = Ids
    }
  }
  return search
}

// 数组对象排序 | params(需排序数组, 原始数组, 唯一参数别名（default 'id'）, 排序参数别名（default 'sort'）, 起始值（default 0）, 排序方法（asc递增 || desc递减）) | return 仅排序变化数据数组
export function sortOrderListOnlyDynamic(newList, oldList, idName, sortName, startNumber, sortOrder) {
  let returnList = []
  let listNew = JSON.parse(JSON.stringify(newList))
  let id = idName != null ? idName : 'id'
  let sort = sortName != null ? sortName : 'sort'
  let num = startNumber != null ? startNumber : 0
  let order = sortOrder != null && sortOrder === 'desc' || sortOrder === 'DESC' ? 'desc' : 'asc'
  if (order === 'desc') {
    for (let i = 0; i < listNew.length; i++) {
      listNew[i][sort] = num - i
      for (let j = 0; j < newList.length; j++) {
        if (listNew[i][id] === newList[j][id] && listNew[i][sort] !== newList[j][sort]) {
          returnList.push(listNew[i])
        }
      }
    }
  } else {
    for (let i = 0; i < listNew.length; i++) {
      listNew[i][sort] = num + i
      for (let j = 0; j < newList.length; j++) {
        if (listNew[i][id] === newList[j][id] && listNew[i][sort] !== newList[j][sort]) {
          returnList.push(listNew[i])
        }
      }
    }
  }
  return returnList
}

// 数组对象排序 | params(需排序数组, 排序参数别名（default 'sort'）, 起始值（default 0）, 排序方法（asc递增 || desc递减）) | return 新排序数组 全部数据
export function sortOrderList(newList, sortName, startNumber, sortOrder) {
  let listNew = newList
  let sort = sortName != null ? sortName : 'sort'
  let num = startNumber != null ? startNumber : 0
  let order = sortOrder != null && sortOrder === 'desc' || sortOrder === 'DESC' ? 'desc' : 'asc'
  if (order === 'desc') {
    for (let i = 0; i < listNew.length; i++) {
      listNew[i][sort] = num - i
    }
  } else {
    for (let i = 0; i < listNew.length; i++) {
      listNew[i][sort] = num + i
    }
  }
}

// 数组对象去空 | params(需去空数组, 操作类型（0删除 || 1改变对象操作变量（非空=false,空=true） | default 0） 操作变量（default 'params.visible' 仅操作类型为1时生效）, 检验去空值) | return 去空数组 全部数据
export function excludeEmptyList(newList, actionType, actionValue, ...excludeNames) {
  let change = false
  let listNew = newList
  let value = actionValue != null ? actionValue : 'visible'
  if (actionType === 1) {
    for (const excludeName of excludeNames) {
      let i = listNew.length
      while (i--) {
        if (value === 'visible') {
          if (!listNew[i].hasOwnProperty('params')) {
            listNew[i]["params"] = {}
          }
          if (change === true) {
            listNew[i]['params']['visible'] = listNew[i][excludeName] == null || listNew[i][excludeName].replace(/[ ]/g, "").length === 0
          } else {
            change = listNew[i]['params']['visible'] = listNew[i][excludeName] == null || listNew[i][excludeName].replace(/[ ]/g, "").length === 0
          }
        } else {
          if (change === true) {
            listNew[i][value] = listNew[i][excludeName] == null || listNew[i][excludeName].replace(/[ ]/g, "").length === 0
          } else {
            change = listNew[i][value] = listNew[i][excludeName] == null || listNew[i][excludeName].replace(/[ ]/g, "").length === 0
          }
        }
      }
    }
  } else {
    for (const excludeName of excludeNames) {
      let i = listNew.length
      while (i--) {
        if (listNew[i][excludeName] == null || listNew[i][excludeName].replace(/[ ]/g, "").length === 0) {
          listNew.splice(i, 1)
          change = true
        }
      }
    }
  }
  return change
}

// 数组对象去重 | params(需去重数组, 操作类型（0删除 || 1改变对象操作变量（非重=false,重=true） | default 0） 操作变量（default 'params.visible' 仅操作类型为1时生效）, 检验去重值) | return 去重数组 全部数据
export function excludeRepeatList(newList, actionType, actionValue, ...excludeNames) {
  let change = false
  let listNew = newList
  let value = actionValue != null ? actionValue : 'visible'
  if (actionType === 1) {
    for (let k = 0; k < listNew.length; k++) {
      if (value === 'visible') {
        if (!listNew[k].hasOwnProperty('params')) {
          listNew[k]["params"] = {}
        }
        listNew[k]['params']['visible'] = false
      } else {
        listNew[k][value] = false
      }
    }
    for (const excludeName of excludeNames) {
      let i = listNew.length
      while (i--) {
        let j = listNew.length
        while (j--) {
          if (value === 'visible') {
            if (change === true) {
              listNew[i]['params']['visible'] = listNew[i][excludeName] === listNew[j][excludeName] && i !== j
            } else {
              change = listNew[i]['params']['visible'] = listNew[i][excludeName] === listNew[j][excludeName] && i !== j
            }
          } else {
            if (change === true) {
              listNew[i][value] = listNew[i][excludeName] === listNew[j][excludeName] && i !== j
            } else {
              change = listNew[i][value] = listNew[i][excludeName] === listNew[j][excludeName] && i !== j
            }
          }
        }
      }
    }
  } else {
    for (const excludeName of excludeNames) {
      let i = listNew.length
      while (i--) {
        let j = listNew.length
        while (j--) {
          if (listNew[i][excludeName] === listNew[j][excludeName] && i !== j) {
            listNew.splice(j, 1)
            change = true
          }
        }
      }
    }
  }
  return change
}

// Table合并行通用（不相邻的数据相互不受影响且行不交叉合并）
export function mergeTableRow(config) {
  let data = config.data
  const {mergeColNames, firstMergeColNames, firstMerge} = config
  if (!mergeColNames || mergeColNames.length === 0) {
    return data
  }
  mergeColNames.forEach((m) => {
    const mList = {}
    data = data.map((v, index) => {
      const firstMergeVal = v[firstMerge]
      const rowVal = firstMergeVal + '-' + v[m]
      if (mList[rowVal] && mList[rowVal].newIndex === index) {
        const flag = firstMergeColNames.filter((f) => {
          return f === m
        }).length !== 0
        const mcFlag = mergeColNames.filter((mc) => {
          return mc === firstMerge
        }).length === 0
        const span = firstMerge + '-' + firstMergeVal + '-span'
        if ((mcFlag && flag) || (flag && data[index][span] && data[index][span].rowspan === 1)) {
          v[m + '-' + firstMergeVal + '-span'] = {
            rowspan: 1,
            colspan: 1
          }
        } else {
          data[mList[rowVal]['index']][m + '-' + firstMergeVal + '-span'].rowspan++
          v[m + '-' + firstMergeVal + '-span'] = {
            rowspan: 0,
            colspan: 0
          }
          mList[rowVal]['num']++
          mList[rowVal]['newIndex']++
        }
      } else {
        mList[rowVal] = {num: 1, index: index, newIndex: index + 1}
        v[m + '-' + firstMergeVal + '-span'] = {
          rowspan: 1,
          colspan: 1
        }
      }
      return v
    })
  })
  return data
}

// 检验是否为移动端设备
export function isMobile() {
  return navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
}
