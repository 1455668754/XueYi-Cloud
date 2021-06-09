const baseURL = process.env.VUE_APP_BASE_API;
import defaultSettings from '@/settings';

/**
 * 通用js方法封装处理
 * Copyright (c) 2021 xueyi
 */

// 添加系统参数 | 系统Id systemId
export function addSystem(data) {
  const { systemId } = defaultSettings
  const state = {
    systemId: systemId
  }
  let search;
  if(data != null){
    search = data;
  }else {
    search={};
  }
  search["systemId"] = state.systemId;
  return search;
}

// 数组转对象
export function updateParamIds(Ids, params, propName) {
  let search;
  if (params != null) {
    search = params;
    if (!search.hasOwnProperty('params')) {
      search["params"] = {}
    }
  } else {
    search = {params: {}};
  }
  if (null != Ids && '' != Ids) {
    if (typeof (Ids) === 'string') {
      Ids = Ids.split(",");
    }
    if (typeof (propName) === "undefined") {
      search.params["Ids"] = Ids;
    } else {
      search.params[propName] = Ids;
    }
  }
  return search;
}

// 数组对象排序 | 需有sort参数 | 返回新排序数组 | 仅排序变化数据
export function sortOrderListOnlyDynamic(newList, oldList, idName) {
  let returnList = [];
  let id = idName != null ? idName : 'id';
  let listNew = JSON.parse(JSON.stringify(newList));
  for (let i = 0; i < listNew.length; i++) {
    listNew[i].sort = i;
    for (let j = 0; j < newList.length; j++) {
      if (listNew[i][id] === newList[j][id] && listNew[i].sort !== newList[j].sort) {
        returnList.push(listNew[i]);
      }
    }
  }
  return returnList;
}

// 数组对象排序 | 需有sort参数 | 返回新排序数组 全部数据
export function sortOrderList(newList) {
  let listNew = JSON.parse(JSON.stringify(newList));
  for (let i = 0; i < listNew.length; i++) {
    listNew[i].sort = i;
  }
  return listNew;
}

// Table合并行通用（不相邻的数据相互不受影响且行不交叉合并） 
export function mergeTableRow(config) {
  let data = config.data
  const { mergeColNames, firstMergeColNames, firstMerge } = config
  if (!mergeColNames || mergeColNames.length === 0) {
    return data
  }
  mergeColNames.forEach((m) => {
    const mList = {}
    data = data.map((v, index) => {
      const firstMergeVal = v[firstMerge]
      const rowVal = firstMergeVal + '-' + v[m]
      if (mList[rowVal] && mList[rowVal].newIndex === index) {
        const flag = firstMergeColNames.filter((f) => { return f === m }).length !== 0
        const mcFlag = mergeColNames.filter((mc) => { return mc === firstMerge }).length === 0
        const span = firstMerge + '-' + firstMergeVal + '-span'
        if ((mcFlag && flag) || (flag && data[index][span] && data[index][span].rowspan === 1)) {
          v[m + '-' + firstMergeVal + '-span'] = {
            rowspan: 1,
            colspan: 1
          }
        } else {
          data[mList[rowVal]['index']][m + '-' + firstMergeVal + '-span'].rowspan ++
          v[m + '-' + firstMergeVal + '-span'] = {
            rowspan: 0,
            colspan: 0
          }
          mList[rowVal]['num']++
          mList[rowVal]['newIndex']++
        }
      } else {
        mList[rowVal] = { num: 1, index: index, newIndex: index + 1 }
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