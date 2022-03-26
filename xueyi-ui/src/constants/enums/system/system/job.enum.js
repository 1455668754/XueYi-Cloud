/** 调度任务主页路由名称 */
export const JobIndexGo = '60a02b15ddcf45eab582a0c57af6ae62'

/** 调度任务详情页路由名称 */
export const JobDetailGo = '101aeea275ce4c61b0652491f75125c0'

/** 调度日志主页路由名称 */
export const JobLogIndexGo = 'c48c23456ed144719fe6d0e35d9e8ddf'

/** 任务状态 */
export const JobStatusEnum = {
  // 正常
  NORMAL: '0',
  // 暂停
  PAUSE: '1'
}

/** 任务并发执行 */
export const JobConcurrentEnum = {
  // 允许
  ALLOW: '0',
  // 禁止
  FORBID: '1'
}

/** 错误策略 */
export const JobMisfireEnum = {
  // 默认
  DEFAULT: '0',
  // 立即触发执行
  IGNORE_MISFIRES: '1',
  // 触发一次执行
  FIRE_AND_PROCEED: '2',
  // 不触发立即执行
  DO_NOTHING: '3'
}

