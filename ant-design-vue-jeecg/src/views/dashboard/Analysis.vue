<template>
  <a-card :bordered="false" :class="{'abcdefg':true}">
<!--    <div class="no-print" style="text-align: right">-->
<!--      <a-button v-print="'#printContent'" ghost type="primary">打印</a-button>-->
<!--    </div>-->
    <section ref="print" id="printContent" class="abcdefg">
      <div style="text-align: center">
        <p style="font-size: 24px;font-weight: 800">
          欢迎使用 ZYF奖助学金管理系统
        </p>
      </div>
      <div style="text-align: center">
      <img alt="" src="../../../public/home.jpg" height="914" width="1166"/>

      </div>
      <!--签字-->
    </section>
  </a-card>
</template>

<script>
export default {
  name: "Analysis"
}
</script>

<style scoped>

</style>