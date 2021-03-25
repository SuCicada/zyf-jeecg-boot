<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zxjName']" placeholder="请输入名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="排名方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
<!--              <j-dict-select-tag type="radio" v-decorator="['sortType']" :trigger-change="true" dictCode="" placeholder="请选择排名方式" />-->
              <a-select v-decorator="[ 'sortType', {}]" placeholder="请选择排名方式" :getPopupContainer= "(target) => target.parentNode">
                <a-select-option :value="1">全校排名</a-select-option>
                <a-select-option :value="2">专业排名</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="人数百分比" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['numRate']" placeholder="请输入人数百分比" style="width: 20%" />%
            </a-form-item>
          </a-col>
          <a-col :span="24">
<!--            <a-row :gutter="24" style="margin-top: 65px;margin-bottom:50px;">-->
<!--              <a-col :span="12">-->
            <a-form-item label="文件上传" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-model="fileList"></j-upload>
            </a-form-item>
<!--          </a-col>-->
<!--              <a-col :span="12">-->
<!--                选中的值(v-model)：-->
<!--                <j-ellipsis :value="fileList" :length="30" v-if="fileList.length>0"/>-->
<!--              </a-col>-->
<!--            </a-row>-->
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
    name: 'ZyfZxjForm',
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
        fileList: [],
        url: {
          add: "/zxj/zyfZxj/add",
          edit: "/zxj/zyfZxj/edit",
          queryById: "/zxj/zyfZxj/queryById"
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
          this.form.setFieldsValue(pick(this.model,'zxjName','sortType','numRate','fileList'))
        })
        this.fileList = this.model.fileList.split(",")
        // console.log(this.model)
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
            // 很重要, 文件路径记录提交, 并且清除fileList
            // 这里 fileList已经自动转为字串了, 不用再join了
            console.log(that.fileList)
            if(typeof that.fileList === "string"){
              formData.fileList= that.fileList //.join(",")
            }else{
              formData.fileList= that.fileList.join(",")
            }
            that.fileList = [];
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
        this.form.setFieldsValue(pick(row,'zxjName','sortType','numRate'))
      },
    }
  }
</script>