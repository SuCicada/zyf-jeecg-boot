<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="课程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['kcName']" placeholder="请输入课程名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="专业" :labelCol="labelCol" :wrapperCol="wrapperCol">
<!--              <j-dict-select-tag type="list" v-decorator="['zyId']" :trigger-change="true" dictCode="" placeholder="请选择专业id" />-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择专业"
                v-model="zyId"
                v-decorator="['zyName']"
                optionFilterProp = "children">
                <a-select-option v-for="(zy,index) in selectedZy " :key="index.toString()" :value="zy.id">
                  {{ zy.zyName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="年级" :labelCol="labelCol" :wrapperCol="wrapperCol">
<!--              <j-dict-select-tag type="list" v-decorator="['njId']" :trigger-change="true" dictCode="" placeholder="请选择年级id" />-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择年级"
                v-model="njId"
                v-decorator="['njName']"
                optionFilterProp = "children"
              >
                <a-select-option v-for="(nj,index) in selectedNj " :key="index.toString()" :value="nj.id">
                  {{ nj.njName }}
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

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'ZyfKcForm',
    components: {
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
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
    data () {
      return {
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        selectedNj: [],
        njId: "",
        selectedZy: [],
        zyId:"",
        url: {
          add: "/kc/zyfKc/add",
          edit: "/kc/zyfKc/edit",
          queryById: "/kc/zyfKc/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'kcName','zyId','njId','zyName','njName'))
        })
        const getNjList = (params)=>getAction("/nj/zyfNj/queryall",params);
        getNjList().then(res=>{
          this.selectedNj = res.result
        })
        const getZyList = (params)=>getAction("/zy/zyfZy/queryall",params);
        getZyList().then(res=>{
          this.selectedZy = res.result
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            formData.zyId = that.zyId;
            formData.njId = that.njId;
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }

        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'kcName','zyId','njId'))
      },
    }
  }
</script>