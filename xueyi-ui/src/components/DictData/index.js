import Vue from 'vue'
import DataDict from '@/utils/dict'
import { dicDict } from '@/api/sys/dict'

function install() {
  Vue.use(DataDict, {
    metas: {
      '*': {
        labelField: 'dictLabel',
        valueField: 'dictValue',
        request(dictMeta) {
          return dicDict(dictMeta.type).then(res => res.data)
        },
      },
    },
  })
}

export default {
  install,
}
