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
  let search = data;
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
    listNew[i].sort = (i - 128) < 127 ? (i - 128) : 127;
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
    listNew[i].sort = (i - 128) < 127 ? (i - 128) : 127;
  }
  return listNew;
}
