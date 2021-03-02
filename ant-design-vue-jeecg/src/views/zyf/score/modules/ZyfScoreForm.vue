<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="学生" :labelCol="labelCol" :wrapperCol="wrapperCol">
<!--              <a-input v-decorator="['studentId']" placeholder="请输入学生"  ></a-input>-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择学生"
                v-model="studentId"
                v-decorator="[ 'realname',{}]"
                optionFilterProp="children">
                <a-select-option v-for="(student,index) in selectedStudent " :key="index.toString()" :value="student.id">
                  {{ student.bjName }} - {{ student.realname }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="课程" :labelCol="labelCol" :wrapperCol="wrapperCol">
<!--              <a-input v-decorator="['kcId']" placeholder="请输入课程id"  ></a-input>-->
              <a-select
                mode="default"
                style="width: 100%"
                placeholder="请选择课程"
                v-model="kcId"
                v-decorator="[ 'kcName',{}]"
                optionFilterProp="children">
                <a-select-option v-for="(kc,index) in selectedKc " :key="index.toString()" :value="kc.id">
                  {{ kc.kcName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="成绩值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['score']" placeholder="请输入成绩值" style="width: 100%" />
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
    name: 'ZyfScoreForm',
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
        selectedKc: [],
        kcId: "",
        selectedStudent: [],
        studentId: "",
        url: {
          add: "/score/zyfScore/add",
          edit: "/score/zyfScore/edit",
          queryById: "/score/zyfScore/queryById"
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
          this.form.setFieldsValue(pick(this.model,'studentId','kcId','score','realname','kcName'))
        })
        const getKcList = (params) => getAction("/kc/zyfKc/queryall", params);
        getKcList().then(res => {
          this.selectedKc = res.result
        })
        const getStudentList = (params) => getAction("/student/zyfStudent/queryall", params);
        getStudentList().then(res => {
          this.selectedStudent = res.result
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
            formData.studentId = that.studentId
            formData.kcId = that.kcId
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
        this.form.setFieldsValue(pick(row,'studentId','kcId','score'))
      },
    }
  }
</script>