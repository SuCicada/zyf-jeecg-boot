<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="学生学号">
              <a-input placeholder="学生学号" v-model="queryParam.workNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="学生姓名">
              <a-input placeholder="学生姓名" v-model="queryParam.studentName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="班级名称">
              <a-input placeholder="班级名称" v-model="queryParam.bjName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="辅导员审核情况">
              <!--                <a-input placeholder="班级名称" v-model="queryParam.bjName"></a-input>-->
              <a-select v-decorator="[ 'teacherCheck', {}]"
                        v-model="queryParam.teacherCheck"
                        placeholder="请选择审核情况查询" :getPopupContainer="(target) => target.parentNode">
                <a-select-option :value="0">未审核</a-select-option>
                <a-select-option :value="1">审核通过</a-select-option>
                <a-select-option :value="2">审核拒绝</a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="教务处审核情况">
              <a-select v-decorator="[ 'officeCheck', {}]"
                        v-model="queryParam.officeCheck"
                        placeholder="请选择审核情况查询" :getPopupContainer="(target) => target.parentNode">
                <a-select-option :value="0">未审核</a-select-option>
                <a-select-option :value="1">审核通过</a-select-option>
                <a-select-option :value="2">审核拒绝</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">

      <a-button @click="handleAdd" type="primary" icon="plus"
                v-show=" role === 'STUDENT'">
        开始申请
      </a-button>
      <!--      <a-button type="primary" icon="download" @click="handleExportXls('奖学金申请')">导出</a-button>-->
      <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"-->
      <!--                @change="handleImportExcel">-->
      <!--        <a-button type="primary" icon="import">导入</a-button>-->
      <!--      </a-upload>-->
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal"-->
      <!--                     @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
          style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
          ref="table"
          size="middle"
          :scroll="{x:true}"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          class="j-table-force-nowrap"
          @change="handleTableChange">
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
              v-else
              :ghost="true"
              type="primary"
              icon="download"
              size="small"
              @click="downloadFile(text)">
            下载
          </a-button>
        </template>
        <span slot="action" slot-scope="text, record"
        >
          <a @click="handleDetail(record)">详情</a>
        </span>


        <span slot="action" slot-scope="text, record">
          <div v-show=" role === 'TEACHER'||  role === 'OFFICE'">
            <a @click="handleEdit(record)">审核</a>
          </div>
          <!--          <a @click="handleEdit(record)">编辑</a>-->
          <div v-if=" role === 'STUDENT' ">
            <a @click="handleDetail(record)">详情</a>
            <a-divider type="vertical"/>
            <a-popconfirm title="确定撤回吗?" @confirm="() => handleDelete(record.id)">
              <a>撤回申请</a>
            </a-popconfirm>
          </div>
          <!--          <a-dropdown>-->
          <!--            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
          <!--            <a-menu slot="overlay">-->
          <!--              <a-menu-item>-->
          <!--                <a @click="handleDetail(record)">详情</a>-->
          <!--              </a-menu-item>-->
          <!--              <a-menu-item>-->
          <!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
          <!--                  <a>删除</a>-->
          <!--                </a-popconfirm>-->
          <!--              </a-menu-item>-->
          <!--            </a-menu>-->
          <!--          </a-dropdown>-->
        </span>

      </a-table>
    </div>

    <zyf-jxj-app-modal ref="modalForm" @ok="modalFormOk"></zyf-jxj-app-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import {mixinDevice} from '@/utils/mixin'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import ZyfJxjAppModal from './modules/ZyfJxjAppModal'
import {getAction} from "@api/manage";
import store from '@/store'

export default {
  name: 'ZyfJxjAppList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    ZyfJxjAppModal
  },
  data() {
    return {
      description: '奖学金申请管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '班级',
          align: "center",
          dataIndex: 'bjName'
        },
        {
          title: '学生',
          align: "center",
          dataIndex: 'studentName'
        },
        {
          title: '申请奖学金',
          align: "center",
          dataIndex: 'jxjName'
        },
        {
          title: '辅导员审核',
          align: "center",
          dataIndex: 'teacherCheck'
          , customRender: function (t, r, index) {
            return t === 0 ? "等待辅导员审核"
                : t === 1 ? "辅导员审核通过"
                    : t === 2 ? "辅导员审核未通过"
                        : t;
          }
        },
        {
          title: '教务处审核',
          align: "center",
          dataIndex: 'officeCheck'
          , customRender: function (t, r, index) {
            return t === 0 ? "等待教务处员审核"
                : t === 1 ? "教务处审核通过"
                    : t === 2 ? "教务处审核未通过"
                        : t;
          }
        },
        {
          title: '是否发放',
          align: "center",
          dataIndex: 'accepted'
          , customRender: function (t, r, index) {
            console.log(t)
            return t === 0 ? "奖学金未发放"
                : t === 1 ? "奖学金已获得"
                    : t === 2 ? "奖学金未获得"
                        : t;
          }
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: {customRender: 'action'}
        }
      ],
      role: "",
      url: {
        list: "/jxj_app/zyfJxjApp/list?nihao",
        delete: "/jxj_app/zyfJxjApp/delete",
        deleteBatch: "/jxj_app/zyfJxjApp/deleteBatch",
        exportXlsUrl: "/jxj_app/zyfJxjApp/exportXls",
        importExcelUrl: "jxj_app/zyfJxjApp/importExcel",

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    initDictConfig() {
      let that = this
      console.log("---------")
      let userId = store.getters.userInfo.id
      let obj = {userId: userId}
      getAction("/zyf/util/queryUserRole", obj)
          .then(res => {
            let role = res.result
            console.log("role " + role)
            that.role = role
            this.role = role
          })
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({type: 'string', value: 'studentId', text: '学生id', dictCode: ''})
      fieldList.push({type: 'string', value: 'jxjId', text: '奖学金id', dictCode: ''})
      fieldList.push({type: 'int', value: 'teacherCheck', text: '辅导员审核', dictCode: ''})
      fieldList.push({type: 'int', value: 'officeCheck', text: '教务处审核', dictCode: ''})
      fieldList.push({type: 'int', value: 'accepted', text: '是否发放', dictCode: ''})
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
