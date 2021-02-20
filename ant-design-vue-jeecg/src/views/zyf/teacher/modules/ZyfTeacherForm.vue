<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="用户id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['userId']" placeholder="请输入用户id"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="专业id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['zyId']" placeholder="请输入专业id"  ></a-input>-->
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
            <a-select v-decorator="[ 'sexStr', {}]" placeholder="请选择性别" :getPopupContainer="(target) => target.parentNode">
              <a-select-option :value="1">男</a-select-option>
              <a-select-option :value="2">女</a-select-option>
            </a-select>
          </a-form-item>
          <a-col :span="24">
            <a-form-item label="专业" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <j-dict-select-tag type="list" v-decorator="['zyId']" :trigger-change="true" dictCode="" placeholder="请选择专业id" />-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择专业"
                v-model="zyId"
                v-decorator="[ 'zyName',{}]"
                optionFilterProp="children">
                <a-select-option v-for="(zy,index) in selectedZy " :key="index.toString()" :value="zy.id">
                  {{ zy.zyName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
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
import pick from 'lodash.pick'
import {validateDuplicateValue} from '@/utils/util'
import {addUser, editUser, queryUserRole, queryall, duplicateCheck} from '@/api/api'
import moment from 'moment'

export default {
  name: 'ZyfTeacherForm',
  components: {},
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
      zyId: "",
      selectedZy: [],
      dateFormat: "YYYY-MM-DD",
      moment,
      url: {
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
  },
  methods: {
    add() {
      this.edit({});
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'username', 'realname', 'workNo', 'sex','sexStr', 'zyName','orgCode'))
      })
      const getZyList = (params) => getAction("/zy/zyfZy/queryall", params);
      getZyList().then(res => {
        this.selectedZy = res.result
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
    validateWorkNo(rule, value, callback) {
      var params = {
        tableName: 'sys_user',
        fieldName: 'work_no',
        fieldVal: value,
        dataId: this.userId
      };
      duplicateCheck(params).then((res) => {
        if (res.success) {
          callback()
        } else {
          callback("学号已存在!")
        }
      })
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.userId = this.model.id;
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
          // 这里很重要, 用组织编号来存储 专业id
          if(that.zyId.length > 0){
            formData.orgCode = that.zyId
          }
          // 这个角色id是老师id, 很重要不要错
          formData.selectedroles = "1359088351200718850"
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
      this.form.setFieldsValue(pick(row, 'userId', 'zyId'))
    },
  }
}
</script>