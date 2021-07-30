<template>
  <el-popover
    placement="bottom"
    width="288"
    trigger="hover">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="通知" name="notice">
        <el-table
          :data="noticeList"
          :show-header="false"
          max-height="310"
          @row-click="noticeRowClick"
          style="width: 100%">
          <el-table-column>
            <template slot-scope="scope">
              <div :class="scope.row.isRead==='0'?'notice-packing':'notice-packing notice-packing-opacity'">
                <div class="notice-avatar-notice">
                  <img class="notice-image"
                       :src="scope.row.noticeType==='0'?notice.xy_notice:scope.row.noticeType==='1'?notice.xy_email:notice.xy_message">
                </div>
                <div class="notice-divide"/>
                <div class="notice-message">
                  <div class="notice-title">
                    {{
                      scope.row.noticeTitle.length <= maxTitleLength ? scope.row.noticeTitle : scope.row.noticeTitle.substring(0, maxTitleLength - 2) + '......'
                    }}
                  </div>
                  <div class="notice-interval">
                    {{ scope.row.timeInterval }}
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="公告" name="email">
        <el-table
          :data="announcementList"
          :show-header="false"
          max-height="308"
          @row-click="announcementRowClick"
          style="width: 100%">
          <el-table-column>
            <template slot-scope="scope">
              <div :class="scope.row.isRead==='0'?'notice-packing':'notice-packing notice-packing-opacity'">
                <div
                  :class="scope.row.noticeContent.length <= maxContentLength?'notice-avatar-announcement-one':'notice-avatar-announcement-two'">
                  <img class="notice-image"
                       :src="scope.row.noticeType==='0'?notice.xy_notice:scope.row.noticeType==='1'?notice.xy_email:notice.xy_message">
                </div>
                <div class="notice-divide"/>
                <div class="notice-message">
                  <div class="notice-title">
                    {{
                      scope.row.noticeTitle.length <= maxTitleLength ? scope.row.noticeTitle : scope.row.noticeTitle.substring(0, maxTitleLength - 2) + '......'
                    }}
                  </div>
                  <div class="notice-content">
                    {{
                      scope.row.noticeContent.length <= maxContentLength ? scope.row.noticeContent : scope.row.noticeContent.substring(0, maxContentLength - 2) + '......'
                    }}
                  </div>
                  <div class="notice-interval">
                    {{ scope.row.timeInterval }}
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
    <el-badge :value="unreadMessageNum" class="notice-badge" slot="reference" :hidden="unreadMessageNum === 0">
      <svg-icon icon-class="xy_notice" class="notice-badge-svg"/>
    </el-badge>
  </el-popover>
</template>

<script>
import xy_notice from 'common/src/assets/images/xy_notice.png'
import xy_email from 'common/src/assets/images/xy_email.png'
import xy_message from 'common/src/assets/images/xy_message.png'

export default {
  name: "Notice",
  data() {
    return {
      notice: {
        xy_notice: xy_notice,
        xy_email: xy_email,
        xy_message: xy_message
      },
      activeName: "notice",
      maxTitleLength: 12,     // 标题最大字符显示数
      minContentLength: 15,   // 内容一行最大字符显示数
      maxContentLength: 30,   // 内容最大字符显示数
      unreadMessageNum: 0,     // 未读消息数
      // noticeList:[]    // 通知 noticeTitle 消息标题 | noticeContent 消息内容 | noticeType 消息类型 | timeInterval 距今时间 | isRead 是否已读(0否 1是)
      // announcementList:[],  //公告  noticeTitle 消息标题 | noticeContent 消息内容 | noticeType 消息类型 | timeInterval 距今时间 | isRead 是否已读(0否 1是)
      announcementList: [{
        noticeTitle: '通知通知通知通知通知通知通知通知通知通通知通知通通知',
        noticeContent: '通知通知通知通知通知通知通通知通知通通知通知通知通知通知知通知通通知通知通知通知通知通知通通知通知通通知',
        noticeType: '1',
        timeInterval: '3 days ago',
        isRead: '0'
      }, {
        noticeTitle: '通知通知通知通知',
        noticeContent: '这是一条通知',
        noticeType: '0',
        timeInterval: '5 days ago',
        isRead: '1'
      }, {
        noticeTitle: '通知通知通知通知',
        noticeContent: '这是一条通知',
        noticeType: '2',
        timeInterval: '1 days ago',
        isRead: '0'
      }, {noticeTitle: '通知通知通知通知', noticeContent: '这是一条通知', noticeType: '0', timeInterval: '5 days ago', isRead: '1'}],
      noticeList: [{
        noticeTitle: '通知通知通知通知通知通知通知通知通知通通知通知通通知',
        noticeContent: '通知通知通知通知通知通知通知通知通通知通知通知通知通知通知通通知通知通通知',
        noticeType: '1',
        timeInterval: '3 days ago',
        isRead: '0'
      }, {
        noticeTitle: '通知通知通知通知',
        noticeContent: '这是一条通知',
        noticeType: '0',
        timeInterval: '5 days ago',
        isRead: '1'
      }, {
        noticeTitle: '通知通知通知通知',
        noticeContent: '这是一条通知',
        noticeType: '2',
        timeInterval: '1 days ago',
        isRead: '0'
      }, {noticeTitle: '通知通知通知通知', noticeContent: '这是一条通知', noticeType: '0', timeInterval: '5 days ago', isRead: '1'}]
    }
  },
  created() {
    this.unreadMessageNum = 0
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
    },
    noticeRowClick(row) {
      if (row.isRead === '0') {
        this.unreadMessageNum > 0 ? this.unreadMessageNum-- : this.unreadMessageNum = 0
        row.isRead = '1'
      }
    },
    announcementRowClick(row) {
      if (row.isRead === '0') {
        this.unreadMessageNum > 0 ? this.unreadMessageNum-- : this.unreadMessageNum = 0
        row.isRead = '1'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.notice-badge {
  margin-top: 12px;
  margin-right: 5px;

  .notice-badge-svg {
    margin-bottom: 12px;
    margin-left: 5px;
  }
}

.notice-packing {
  padding: 8px 0;
  display: table;

  .notice-avatar-notice {
    margin-right: 20px;
    display: table-cell;

    .notice-image {
      width: 32px;
      height: 32px;
      margin-bottom: -20px;
    }
  }

  .notice-avatar-announcement-one {
    margin-right: 20px;
    display: table-cell;

    .notice-image {
      width: 32px;
      height: 32px;
      margin-bottom: -30px;
    }
  }

  .notice-avatar-announcement-two {
    margin-right: 20px;
    display: table-cell;

    .notice-image {
      width: 32px;
      height: 32px;
      margin-bottom: -35px;
    }
  }

  .notice-divide {
    width: 10px;
    display: table-cell;
  }

  .notice-message {
    display: table-cell;
    font-size: 12px;
    line-height: 1.5715;
    font-family: -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol, Noto Color Emoji;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;

    .notice-title {
      font-size: 14px;
    }

    .notice-content {
      color: rgba(0, 0, 0, .45);
    }

    .notice-interval {
      color: rgba(0, 0, 0, .45);
    }
  }
}

.notice-packing-opacity {
  opacity: 0.5;
}
</style>
