<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="专业名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zyName']" placeholder="请输入专业名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="所属学院" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <j-dict-select-tag type="list" v-decorator="['xyId']" :trigger-change="true" dictCode="" placeholder="请选择学院" />-->
              <!--                :disabled="departDisabled"-->
              <!--                :getPopupContainer= "(target) => target.parentNode"-->
              <!--                v-model="selectedXy"-->
              <!--                :getPopupContainer = "(target) => target.parentNode"-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择学院"
                v-model="xyId"
                v-decorator="['xyName',{}]"
                optionFilterProp="children"
              >
                <a-select-option v-for="(xy,index) in selectedXy" :key="index.toString()" :value="xy.id">
                  {{ xy.xyName }}
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
import JSearchSelectTag from '@/components/dict/JSearchSelectTag'

export default {
  name: 'ZyfZyForm',
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
      selectedXy: [],
      xyId: "",
      url: {
        add: "/zy/zyfZy/add",
        edit: "/zy/zyfZy/edit",
        queryById: "/zy/zyfZy/queryById"
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
      console.log(this.model)
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'zyName', 'xyId', 'xyName'))
      })

      const getXyList = (params) => getAction("/xy/zyfXy/queryall", params);
      getXyList().then(res => {
        this.selectedXy = res.result
        console.log(this.selectedXy)
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
          console.log(that.xyId)
          console.log(formData)
          if(that.xyId){
            formData.xyId = that.xyId
          }
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
      this.form.setFieldsValue(pick(row, 'zyName', 'xyId'))
    },
  }
}
</script>