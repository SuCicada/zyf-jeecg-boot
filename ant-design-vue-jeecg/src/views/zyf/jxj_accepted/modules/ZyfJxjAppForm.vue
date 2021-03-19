<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="学生id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['studentId']" placeholder="请输入学生id"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="奖学金" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <a-input v-decorator="['jxjId']" placeholder="请输入奖学金id"  ></a-input>-->
              <a-select
                  mode="default"
                  style="width: 100%"
                  placeholder="请选择奖学金"
                  v-model="jxjId"
                  v-decorator="[ 'jxjName',{}]"
                  optionFilterProp="children"
                  v-bind:disabled="role !== 'STUDENT'"
              >
                <a-select-option v-for="(jxj,index) in selectedJxj " :key="index.toString()"
                                 :value="jxj.id">
                  {{ jxj.jxjName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="发放情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <a-input v-decorator="['acceptedStr']" placeholder="未发放" style="width: 100%"-->
              <!--                       :readOnly="true"/>-->
              <!--              等待教务处审核-->
              <a-select v-decorator="[ 'acceptedStr', {}]" placeholder="等待辅导员审核"
                        :getPopupContainer="(target) => target.parentNode"
                        v-model="accepted"
                        v-bind:disabled="role !== 'OFFICE'"
              >
                <a-select-option :value="1">审核通过</a-select-option>
                <a-select-option :value="2">申请驳回</a-select-option>

              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <p>
                1. 奖学金申请提交<br/>
                2. 辅导员审核通过<br/>
                3. 教务处审核通过<br/>
                4. 确认分发资格名单<br/>
                5. 奖学金下发<br/>
              </p>
            </a-form-item>
            <!--            <a-form-item label="是否发放" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input-number v-decorator="['accepted']" placeholder="请输入是否发放" style="width: 100%" />-->
            <!--            </a-form-item>-->
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
import store from '@/store'

export default {
  name: 'ZyfJxjAppForm',
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
      validatorRules: {},
      selectedJxj: [],
      jxjId: "",
      role: "",
      teacherCheck: 0,
      officeCheck: 0,
      accepted: 0,
      url: {
        add: "/jxj_app/zyfJxjApp/add",
        edit: "/jxj_app/zyfJxjApp/edit",
        queryById: "/jxj_app/zyfJxjApp/queryById"
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
      let that = this

      function getCheckStr(check, name = []) {
        let res = "";
        name.find((a, i) => {
          if (check === i) {
            res = a
            return true
          }
          return false
        })
        return res
      }

      record.acceptedStr = getCheckStr(record.accepted,
          ["奖学金未发放"
            , "奖学金已获得"
            , "奖学金未获得"])
      this.model = Object.assign({}, record);
      this.visible = true;
      console.log(this.model)
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'studentId', 'jxjId', 'jxjName',
            'teacherCheck', 'officeCheck', 'accepted',
            'teacherCheckStr', 'officeCheckStr', 'acceptedStr'))
      })
      const getJxjList = (params) => getAction("/jxj/zyfJxj/queryall", params);
      getJxjList().then(res => {
        this.selectedJxj = res.result
      })
      getAction("/zyf/util/queryUserRole")
          .then(res => {
            let role = res.result
            console.log("role " + role)
            that.role = role
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
              /*
              *   accepted: modify
              * */
              formData.accepted = that.accepted
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

          }
      )
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'studentId', 'jxjId', 'teacherCheck', 'officeCheck', 'accepted'))
    },
  }
}
</script>
