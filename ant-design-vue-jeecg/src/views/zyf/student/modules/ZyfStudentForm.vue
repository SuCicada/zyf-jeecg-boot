<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="用户id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['userId']" placeholder="请输入用户id"  ></a-input>-->
          <!--
            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="班级id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['bjId']" placeholder="请输入班级id"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-form-item label="用户账号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input placeholder="请输入用户账号" v-decorator.trim="[ 'username', validatorRules.username]"
                     :readOnly="!!model.id"/>
          </a-form-item>

          <template v-if="!model.id">
            <a-form-item label="登录密码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input type="password" placeholder="请输入登录密码" v-decorator="[ 'password',validatorRules.password]"/>
            </a-form-item>
            <!--            <a-form-item label="确认密码" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input type="password" @blur="handleConfirmBlur" placeholder="请重新输入登录密码"-->
            <!--                       v-decorator="[ 'confirmpassword', validatorRules.confirmpassword]"/>-->
            <!--            </a-form-item>-->
          </template>
          <a-form-item label="学号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input placeholder="请输入学号" v-decorator="[ 'workNo', validatorRules.workNo]"></a-input>
          </a-form-item>

          <a-form-item label="用户姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input placeholder="请输入用户姓名" v-decorator.trim="[ 'realname', validatorRules.realname]"></a-input>
          </a-form-item>


          <a-form-item label="生日" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-date-picker
              style="width: 100%"
              placeholder="请选择生日"
              v-decorator="['birthday', {initialValue:!model.birthday?null:moment(model.birthday,dateFormat)}]"
              :getCalendarContainer="node => node.parentNode"/>
          </a-form-item>
          <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="[ 'sexStr', {}]" placeholder="请选择性别"
                      :getPopupContainer="(target) => target.parentNode">
              <a-select-option :value="1">男</a-select-option>
              <a-select-option :value="2">女</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="学院" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <!--              <j-dict-select-tag type="list" v-decorator="['zyId']" :trigger-change="true" dictCode="" placeholder="请选择专业id" />-->
            <a-select
              mode="default"
              style="width: 100%"
              placeholder="请选择学院"
              v-model="xyId"
              v-decorator="['xyName']"
              optionFilterProp="children"
            >
              <a-select-option v-for="(xy,index) in selectedXy " :key="index.toString()" :value="xy.id">
                {{ xy.xyName }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="专业" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <!--              <j-dict-select-tag type="list" v-decorator="['zyId']" :trigger-change="true" dictCode="" placeholder="请选择专业id" />-->
            <a-select
              mode="default"
              style="width: 100%"
              placeholder="请选择专业"
              v-model="queryParam.zyId"
              v-decorator="['zyName']"
              optionFilterProp="children"
            >
              <a-select-option v-for="(zy,index) in selectedZy " :key="index.toString()" :value="zy.id">
                {{ zy.zyName }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="年级" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select
              mode="default"
              style="width: 100%"
              placeholder="请选择年级"
              v-model="queryParam.njId"
              v-decorator="['njName']"
              optionFilterProp="children"
            >
              <a-select-option v-for="(nj,index) in selectedNj " :key="index.toString()" :value="nj.id">
                {{ nj.njName }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="班级" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select
              mode="default"
              style="width: 100%"
              placeholder="请选择班级"
              v-model="bjId"
              v-decorator="[ 'bjName',{}]"
              optionFilterProp="children">
              <a-select-option v-for="(bj,index) in selectedBj " :key="index.toString()" :value="bj.id">
                {{ bj.bjName }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

import {httpAction, getAction} from '@/api/manage'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import pick from 'lodash.pick'
import {validateDuplicateValue} from '@/utils/util'
import moment from 'moment'
import {addUser, editUser, queryUserRole, queryall, duplicateCheck} from '@/api/api'
import {mixinDevice} from "@/utils/mixin";
import ZyfStudentModal from './ZyfStudentModal'

export default {
  name: 'ZyfStudentForm',
  mixins: [JeecgListMixin, mixinDevice],
  components: {ZyfStudentModal},
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {
      },
      required: false
    },
    //表单模式：true流程表单 false普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      form: this.$form.createForm(this),
      model: {},
      labelCol: {
        xs: {span: 24},
        sm: {span: 5},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 16},
      },
      confirmLoading: false,
      validatorRules: {
        username: {
          rules: [{
            required: true, message: '请输入用户账号!'
          }, {
            validator: this.validateUsername,
          }]
        },
        password: {
          rules: [{
            required: true,
            // pattern:/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,./]).{8,}$/,
            // message: '密码由8位数字、大小写字母和特殊符号组成!'
          }, {
            validator: this.validateToNextPassword,
          }],
        },
        // confirmpassword:{
        //   rules: [{
        //     required: true, message: '请重新输入登录密码!',
        //   }, {
        //     validator: this.compareToFirstPassword,
        //   }],
        realname: {rules: [{required: true, message: '请输入用户名称!'}]},
        phone: {rules: [{validator: this.validatePhone}]},
        workNo: {
          rules: [
            {required: true, message: '请输入学号'},
            {validator: this.validateWorkNo}
          ]
          // },
        },
      },
      bjId: "",
      selectedBj: [],
      selectedKc: [],
      kcId: "",
      selectedZy: [],
      zyId: "",
      selectedXy: [],
      xyId: "",
      selectedNj: [],
      njId: "",
      role: "",
      dateFormat: "YYYY-MM-DD",
      moment,
      url: {
        // add: "/student/zyfStudent/add",
        // edit: "/student/zyfStudent/edit",
        // queryById: "/student/zyfStudent/queryById"
        add: "/sys/user/add",
        edit: "/sys/user/edit",
        queryById: "/sys/user/queryById",
        userId: "/sys/user/generateUserId", // 引入生成添加用户情况下的url
      }
    }
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false
        }
        return true
      }
      return this.disabled
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true
        }
      }
      return false
    }
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData();
    this.initSelected()
    let that = this
    // setInterval(function () {
    //   that.changeXy2()
    //   that.changeZy()
    // }, 1000)
  },
  methods: {
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
    changeXy2() {
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
    add() {
      this.edit({});
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'username', 'realname', 'workNo', 'sex', 'sexStr', 'bjName', 'orgCode'))
      })
      const getZyList = (params) => getAction("/bj/zyfBj/queryall", params);
      getZyList().then(res => {
        this.selectedBj = res.result
      })
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = {id: this.formData.dataId};
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result);
          }
        });
      }
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          let httpurl = '';
          let method = '';
          if (!this.model.id) {
            httpurl += this.url.add;
            method = 'post';
          } else {
            httpurl += this.url.edit;
            method = 'put';
          }
          let formData = Object.assign(this.model, values);
          // 这里很重要, 用组织编号来存储 班级id
          if (that.bjId.length > 0) {
            formData.orgCode = that.bjId
          }
          // 这个角色id是学生id, 很重要不要错
          formData.selectedroles = "1361357450112102402"
          // 这里是为了兼容 sex的自动转换汉字
          if (!formData.sex) {
            formData.sex = formData.sexStr
          }
          // 很重要, 对生日的时间进行格式转换
          formData.birthday = formData.birthday.format(this.dateFormat);
          console.log("表单提交数据", formData)
          httpAction(httpurl, formData, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message);
              that.$emit('ok');
            } else {
              that.$message.warning(res.message);
            }
          }).finally(() => {
            that.confirmLoading = false;
          })
        }

      })
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'userId', 'bjId'))
    },
  }
}
</script>