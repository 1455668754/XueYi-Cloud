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
  if(params != null){
    search = params;
    if(!search.hasOwnProperty('params')){
      search["params"] = {}
    }
  }else{
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