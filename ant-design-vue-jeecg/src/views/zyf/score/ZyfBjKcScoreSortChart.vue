<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row>
          <a-col :span="6">
            <a-form-item label="年级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <j-dict-select-tag type="list" v-decorator="['njId']" :trigger-change="true" dictCode="" placeholder="请选择年级id" />-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择年级"
                v-model="queryParam.njId"
                optionFilterProp="children"
                v-decorator="['njName']"
                @change="changeZy">
                <a-select-option v-for="(nj,index) in selectedNj " :key="index.toString()" :value="nj.id">
                  {{ nj.njName }}
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
            <a-form-item label="课程" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <a-input v-decorator="['kcId']" placeholder="请输入课程id"  ></a-input>-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择课程"
                v-model="queryParam.kcId"
                v-decorator="[ 'kcName',{}]"
                optionFilterProp="children">
                <a-select-option v-for="(kc,index) in selectedKc " :key="index.toString()" :value="kc.id">
                  {{ kc.kcName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="6" v-show=" role !== 'STUDENT'">
            <a-form-item label="班级A:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <j-dict-select-tag type="list" v-decorator="['zyId']" :trigger-change="true" dictCode="" placeholder="请选择专业id" />-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择班级"
                v-model="queryParam.bjId1"
                v-decorator="[ 'bjName',{}]"
                optionFilterProp="children">
                <a-select-option v-for="(bj,index) in selectedBj " :key="index.toString()" :value="bj.id">
                  {{ bj.bjName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="6" v-show=" role !== 'STUDENT'">
            <a-form-item label="班级B" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <j-dict-select-tag type="list" v-decorator="['zyId']" :trigger-change="true" dictCode="" placeholder="请选择专业id" />-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择班级"
                v-model="queryParam.bjId2"
                v-decorator="[ 'bjName',{}]"
                optionFilterProp="children">
                <a-select-option v-for="(bj,index) in selectedBj " :key="index.toString()" :value="bj.id">
                  {{ bj.bjName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <!--          <a-col :span="10">-->
          <!--            <a-form-item label="成绩模式" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              &lt;!&ndash;              <j-dict-select-tag type="radio" v-decorator="['sortType']" :trigger-change="true" dictCode="" placeholder="请选择排名方式" />&ndash;&gt;-->
          <!--              <a-select v-decorator="[ 'scoreType', {}]" placeholder="请选择成绩模式"-->
          <!--                        :getPopupContainer="(target) => target.parentNode">-->
          <!--                <a-select-option :value="1">原始成绩</a-select-option>-->
          <!--                <a-select-option :value="2">综测成绩</a-select-option>-->
          <!--              </a-select>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="submitForm" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <a-tabs defaultActiveKey="1">
      <!-- 柱状图 -->
      <!--      <a-tab-pane tab="柱状图" key="1">-->
      <!--        <bar title="单专业年级班级成绩排名 柱状图" :dataSource="barData" :height="height"/>-->
      <!--      </a-tab-pane>-->
      <!-- 多列柱状图 -->
      <a-tab-pane tab="多列柱状图" key="2">
        <bar-multid title="班级课程成绩对比 多列柱状图" :dataSource="barData" :fields="fields" :height="height"/>
      </a-tab-pane>
      <!-- 迷你柱状图 -->
      <!--      <a-tab-pane tab="迷你柱状图" key="3">-->
      <!--        <mini-bar :dataSource="barData" :width="400" :height="200"/>-->
      <!--      </a-tab-pane>-->
      <!-- 面积图 -->
      <!--      <a-tab-pane tab="面积图" key="4">-->
      <!--        <area-chart-ty title="单专业年级班级成绩排名 面积图" :dataSource="areaData" x="月份" y="销售额" :height="height"/>-->
      <!--      </a-tab-pane>-->
      <!-- 迷你面积图 -->
      <!--      <a-tab-pane tab="迷你面积图" key="5">-->
      <!--        <div style="padding-top: 100px;width:600px;height:200px">-->
      <!--          <mini-area :dataSource="areaData" x="月份" y="销售额" :height="height"/>-->
      <!--        </div>-->
      <!--      </a-tab-pane>-->
      <!-- 多行折线图 -->
      <!--      <a-tab-pane tab="多行折线图" key="6">-->
      <!--        <line-chart-multid title="多行折线图" :height="height"/>-->
      <!--      </a-tab-pane>-->
      <!-- 饼图 -->
      <!--      <a-tab-pane tab="饼图" key="7">-->
      <!--        <pie title="饼图" :height="height"/>-->
      <!--      </a-tab-pane>-->
      <!-- 雷达图 -->
      <!--      <a-tab-pane tab="雷达图" key="8">-->
      <!--        <radar title="雷达图" :height="height"/>-->
      <!--      </a-tab-pane>-->
      <!-- 仪表盘 -->
      <!--      <a-tab-pane tab="仪表盘" key="9">-->
      <!--        <dash-chart-demo title="仪表盘" :value="9" :height="height"/>-->
      <!--      </a-tab-pane>-->
      <!-- 进度条 -->
      <!--      <a-tab-pane tab="进度条" key="10">-->
      <!--        <mini-progress :percentage="30" :target="40" :height="30"/>-->
      <!--        <mini-progress :percentage="51" :target="60" :height="30" color="#FFA500"/>-->
      <!--        <mini-progress :percentage="66" :target="80" :height="30" color="#1E90FF"/>-->
      <!--        <mini-progress :percentage="74" :target="70" :height="30" color="#FF4500"/>-->
      <!--        <mini-progress :percentage="92" :target="100" :height="30" color="#49CC49"/>-->
      <!--      </a-tab-pane>-->
      <!-- 排名列表 -->
      <!--      <a-tab-pane tab="排名列表" key="11">-->
      <!--        <rank-list title="单专业年级班级成绩排名" :list="rankList" style="width: 600px;margin: 0 auto;"/>-->
      <!--      </a-tab-pane>-->
      <!-- TransferBar -->
      <!--      <a-tab-pane tab="TransferBar" key="12">-->
      <!--        <transfer-bar title="年度消耗流量一览表" :data="barData" x="月份" y="流量(Mb)" :height="height"/>-->
      <!--      </a-tab-pane>-->
      <!-- Trend -->
      <!--      <a-tab-pane tab="Trend" key="13">-->
      <!--        <trend title="Trend" term="Trend：" :percentage="30"/>-->
      <!--      </a-tab-pane>-->
      <!-- Liquid -->
      <!--      <a-tab-pane tab="Liquid" key="14">-->
      <!--        <liquid :height="height"/>-->
      <!--      </a-tab-pane>-->
      <!-- BarAndLine -->
      <!--      <a-tab-pane tab="BarAndLine" key="15">-->
      <!--        <bar-and-line :height="height"/>-->
      <!--      </a-tab-pane>-->
    </a-tabs>
  </a-card>
</template>

<script>
import AreaChartTy from '@/components/chart/AreaChartTy'
import Bar from '@/components/chart/Bar'
import BarMultid from '@/components/chart/BarMultid'
import DashChartDemo from '@/components/chart/DashChartDemo'
import LineChartMultid from '@/components/chart/LineChartMultid'
import Liquid from '@/components/chart/Liquid'
import MiniBar from '@/components/chart/MiniBar'
import MiniArea from '@/components/chart/MiniArea'
import MiniProgress from '@/components/chart/MiniProgress'
import Pie from '@/components/chart/Pie'
import Radar from '@/components/chart/Radar'
import RankList from '@/components/chart/RankList'
import TransferBar from '@/components/chart/TransferBar'
import Trend from '@/components/chart/Trend'
import BarAndLine from '@/components/chart/BarAndLine'
import '@/assets/less/TableExpand.less'
import {mixinDevice} from '@/utils/mixin'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import ZyfScoreModal from './modules/ZyfScoreModal'
import {getAction} from "@api/manage";
import store from '@/store'

export default {
  name: 'ViserChartDemo',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    Bar, MiniBar, BarMultid, AreaChartTy, LineChartMultid,
    Pie, Radar, DashChartDemo, MiniProgress, RankList,
    TransferBar, Trend, Liquid, MiniArea, BarAndLine
  },
  data() {
    return {
      height: 420,
      rankList: [],
      barData: [],
      fields: [],
      areaData: []
      , labelCol: {
        xs: {span: 24},
        sm: {span: 5},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 16},
      },
      selectedNj: [],
      njId: "",
      selectedKc: [],
      kcId: "",
      selectedZy: [],
      zyId: "",
      selectedBj: [],
      bjId1: "",
      bjId2: "",
      isZyPage:false,
      role: "",
    }
  },
  created() {
    // setTimeout(() => {
    //   this.loadBarData()
    //   this.loadAreaData()
    //   this.loadRankListData()
    // }, 100)
    this.initSelected()
    this.getRole()
  },
  methods: {
    /**
     * 初始化 选择器
     */
    initSelected() {
      getAction("/nj/zyfNj/queryall")
        .then(res => this.selectedNj = res.result)
      getAction("/zy/zyfZy/queryall")
        .then(res => this.selectedZy = res.result)
      getAction("/bj/zyfBj/queryall")
        .then(res => this.selectedBj = res.result)
      getAction("/kc/zyfKc/queryall")
        .then(res => this.selectedKc = res.result)
    },
    getRole() {
      let userId = store.getters.userInfo.id
      let obj = {userId: userId}
      let that = this
      getAction("/zyf/util/queryUserRole", obj)
        .then(res => {
          let role = res.result
          console.log("role " + role)
          that.role = role
          this.role = role
        })
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
    loadData(x, y, max, min, before = '', after = '月') {
      let data = []
      for (let i = 0; i < 12; i += 1) {
        data.push({
          [x]: `${before}${i + 1}${after}`,
          [y]: Math.floor(Math.random() * max) + min
        })
      }
      return data
    },
    // 加载柱状图数据
    loadBarData() {
      this.barData = this.loadData('x', 'y', 1000, 200)
    },
    // 加载AreaChartTy的数据
    loadAreaData() {
      this.areaData = this.loadData('x', 'y', 500, 100)
    },
    loadRankListData() {
      this.rankList = this.loadData('name', 'total', 2000, 100, '北京朝阳 ', ' 号店')
    },
    submitForm() {
      let param = {
        bjId1: this.queryParam.bjId1,
        bjId2: this.queryParam.bjId2,
        kcId: this.queryParam.kcId,
      }
      getAction("/score/zyfScore/queryBjKcScoreSortChart", param)
        .then(res => {
          // this.selectedZy = res.result
          console.log(res['result'])
          if (res['result']) {
            // this.rankList =
            //   res['result'].map(a => {
            //     return {'name': a['bjName'], 'total': a['score']}
            //   })

            this.barData = res['result']['dataSource']
            this.fields = res['result']['fields']
            // this.areaData = this.barData =
            //   res['result'].map(a => {
            //     return {'x': a['bjName'], 'y': a['score']}
            //   })
          } else {
            this.rankList = this.areaData = this.barData = []
          }
          // console.log(this.loadData('x', 'y', 1000, 200))
        })
    }
  }
}
</script>

<style scoped>

</style>
