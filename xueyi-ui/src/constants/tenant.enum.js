/** 数据源类型（0子数据源 1主数据源）*/
export const DATABASE_TYPE = {
  SLAVE_SOURCE: '0',
  MASTER_SOURCE: '1'
}

/** 读写类型（0读&写 1只读 2只写）*/
export const SOURCE_TYPE = {
  SOURCE_READ_WRITE: '0',
  SOURCE_READ: '1',
  SOURCE_WRITE: '2'
}

/** 是否为主数据源（Y是 N否）*/
export const IS_MAIN = {
  TRUE: 'Y',
  FALSE: 'N'
}
