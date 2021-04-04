<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="账号">
              <!--<a-input placeholder="请输入账号查询" v-model="queryParam.username"></a-input>-->
              <j-input placeholder="输入账号模糊查询" v-model="queryParam.username"></j-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="真实名字">
              <a-input placeholder="请输入真实名字" v-model="queryParam.realname"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="学生学号">
              <a-input placeholder="请输入学生学号" v-model="queryParam.student"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="性别">
              <a-select v-model="queryParam.sex" placeholder="请选择性别">
                <a-select-option value="">请选择</a-select-option>
                <a-select-option value="1">男</a-select-option>
                <a-select-option value="2">女</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
          <!--            <a-form-item label="班级名称">-->
          <!--              <a-input placeholder="请输入班级名称" v-model="queryParam.bjName"></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
          <!--            <a-form-item label="专业名称">-->
          <!--              <a-input placeholder="请输入班级名称" v-model="queryParam.bjName"></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :span="6">
            <a-form-item label="学院" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <j-dict-select-tag type="list" v-decorator="['zyId']" :trigger-change="true" dictCode="" placeholder="请选择专业id" />-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择学院"
                v-model="queryParam.xyId"
                v-decorator="['xyName']"
                optionFilterProp="children"
                @change="changeXy">
                <a-select-option v-for="(xy,index) in selectedXy " :key="index.toString()" :value="xy.id">
                  {{ xy.xyName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="专业" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <j-dict-select-tag type="list" v-decorator="['zyId']" :trigger-change="true" dictCode="" placeholder="请选择专业id" />-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择专业"
                v-model="queryParam.zyId"
                v-decorator="['zyName']"
                optionFilterProp="children"
                @change="changeZy">
                <a-select-option v-for="(zy,index) in selectedZy " :key="index.toString()" :value="zy.id">
                  {{ zy.zyName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="年级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <j-dict-select-tag type="list" v-decorator="['zyId']" :trigger-change="true" dictCode="" placeholder="请选择专业id" />-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择年级"
                v-model="queryParam.njId"
                v-decorator="['njName']"
                optionFilterProp="children"
                @change="changeNj">
                <a-select-option v-for="(nj,index) in selectedNj " :key="index.toString()" :value="nj.id">
                  {{ nj.njName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="班级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <j-dict-select-tag type="list" v-decorator="['zyId']" :trigger-change="true" dictCode="" placeholder="请选择专业id" />-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择班级"
                v-model="queryParam.bjId"
                v-decorator="[ 'bjName',{}]"
                optionFilterProp="children">
                <a-select-option v-for="(bj,index) in selectedBj " :key="index.toString()" :value="bj.id">
                  {{ bj.bjName }}
                </a-select-option>
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('学生表')">导出</a-button>
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

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <zyf-student-modal ref="modalForm" @ok="modalFormOk"></zyf-student-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import {mixinDevice} from '@/utils/mixin'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import ZyfStudentModal from './modules/ZyfStudentModal'
import {getAction} from "@api/manage";

export default {
  name: 'ZyfStudentList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    ZyfStudentModal
  },
  data() {
    return {
      description: '学生表管理页面',
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },

        // {
        //   title:'用户id',
        //   align:"center",
        //   dataIndex: 'userId'
        // },
        // {
        //   title:'班级id',
        //   align:"center",
        //   dataIndex: 'bjId'
        // },
        {
          title: '学生账号',
          align: "center",
          dataIndex: 'username',
          // width: 60,
          sorter: true
        },
        {
          title: '学生姓名',
          align: "center",
          // width: 60,
          dataIndex: 'realname',
        },
        {
          title: '学生学号',
          align: "center",
          // width: 60,
          dataIndex: 'workNo',
        },
        {
          title: '所属班级',
          align: "center",
          dataIndex: 'bjName',
          // width: 80,
          sorter: true
        },
        {
          title: '性别',
          align: "center",
          // width: 40,
          dataIndex: 'sexStr',
          sorter: true
        },
        {
          title: '生日',
          align: "center",
          width: 60,
          dataIndex: 'birthday'
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
      labelCol: {
        xs: {span: 24},
        sm: {span: 5},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 16},
      },
      selectedKc: [],
      kcId: "",
      selectedBj: [],
      bjId: "",
      selectedZy: [],
      zyId: "",
      selectedXy: [],
      xyId: "",
      selectedNj: [],
      njId: "",
      role: "",
      url: {
        list: "/student/zyfStudent/list",
        delete: "/sys/user/delete",
        deleteBatch: "/sys/user/deleteBatch",
        exportXlsUrl: "/sys/user/exportXls",
        importExcelUrl: "sys/user/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList();
    this.initSelected()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({type: 'string', value: 'userId', text: '用户id', dictCode: ''})
      fieldList.push({type: 'string', value: 'bjId', text: '班级id', dictCode: ''})
      this.superFieldList = fieldList
    },
    initSelected() {
      getAction("/xy/zyfXy/queryall")
        .then(res => this.selectedXy = res.result)
      getAction("/zy/zyfZy/queryall")
        .then(res => this.selectedZy = res.result)
      getAction("/nj/zyfNj/queryall")
        .then(res => this.selectedNj = res.result)
      getAction("/bj/zyfBj/queryall")
        .then(res => this.selectedBj = res.result)
      getAction("/kc/zyfKc/queryall")
        .then(res => this.selectedKc = res.result)
    },
    changeXy() {
      let obj = {xyId: this.queryParam.xyId}
      getAction("/zy/zyfZy/queryall", obj)
        .then(res => this.selectedZy = res.result)
    },
    changeZy() {
      let obj = {
        zyId: this.queryParam.zyId
        , njId: this.queryParam.njId
      }
      getAction("/bj/zyfBj/queryall", obj)
        .then(res => this.selectedBj = res.result)
      getAction("/kc/zyfKc/queryall", obj)
        .then(res => this.selectedKc = res.result)
    },
    changeNj() {
      this.changeZy()
    },
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
