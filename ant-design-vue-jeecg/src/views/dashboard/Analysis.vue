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
      <div style="text-align: center" @click="changeImg">
        <img id="img_home_0" v-show="home_show[0]" alt="" src="../../../public/home/home1.jpg" height="500"
             width="auto"/>
        <img id="img_home_1" v-show="home_show[1]" alt="" src="../../../public/home/home2.jpg" height="500"
             width="auto"/>
      </div>
      <!--签字-->
    </section>
  </a-card>

</template>

<script>

export default {
  name: "Analysis",
  data() {
    return {
      home_show: [],
      home_show_index: 0
    }
  },
  created() {
    this.changeImg()
  },
  methods: {
    changeImg() {
      this.home_show = [false, false]
      // let items = ["img_home_1", "img_home_2"]
      let item = this.home_show_index;
      while (this.home_show_index === item) {
        item = Math.floor(Math.random() * this.home_show.length)
      }
      this.home_show_index = item
      this.home_show[item] = true;
      console.log(item)
      // document.getElementById(item).style.display = "none";
    }

  }
}
</script>

<!--<style scoped>-->
<!--@import '~@assets/less/common.less';-->
<!--</style>-->
