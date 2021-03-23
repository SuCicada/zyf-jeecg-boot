<template>
  <div id="userLayout" :class="['user-layout-wrapper', device]">
    <div id="container" class="container">
      <div class="top">
        <div class="header">
          <a href="/">
            <img src="~@/assets/logo.png" class="logo" alt="logo">
            <span class="title">ZYF 奖助学金平台</span>
          </a>
        </div>
        <div class="desc">
          ZYF 奖助学金平台 是中国最具影响力的 奖助学金 平台
        </div>
      </div>

      <route-view></route-view>

      <div id="mask">
        <img src="~@/assets/background/bg0.jpg" alt="">
        <img src="~@/assets/background/bg1.jpg" alt="">
        <img src="~@/assets/background/bg2.jpg" alt="">
        <img src="~@/assets/background/bg3.jpg" alt="">
        <img src="~@/assets/background/bg4.jpg" alt="">
        <img src="~@/assets/background/bg5.jpg" alt="">
      </div>

      <div class="footer">
        <div class="links">
          <!--          <a href="http://doc.jeecg.com" target="_blank">帮助</a>-->
          <!--          <a href="https://github.com/zhangdaiscott/jeecg-boot" target="_blank">隐私</a>-->
          <!--          <a href="https://github.com/zhangdaiscott/jeecg-boot/blob/master/LICENSE" target="_blank">条款</a>-->
        </div>
        <div class="copyright">
          Copyright &copy; 2021 <a href="http://www.zyf.com" target="_blank">ZYF</a> 出品
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import RouteView from "@/components/layouts/RouteView"
import {mixinDevice} from '@/utils/mixin.js'

export default {
  name: "UserLayout",
  components: {RouteView},
  mixins: [mixinDevice],
  data() {
    return {}
  },
  mounted() {
    document.body.classList.add('userLayout')
    this.initBackground()
    Array.from(document.getElementById("mask").children)//获取的所有的子元素
      .forEach(imgObj => {
        console.log(imgObj)
        imgObj.onclick = function () {
          console.log(imgObj)
          document.getElementById("container")
            .style.backgroundImage = "url(" + this.src + ")";
          //循环遍历所有img,注册点击事件
        }
      })
  },
  methods: {
    initBackground() {
      let imgs = Array.from(document.getElementById("mask").children)
      let item = Math.floor(Math.random() * imgs.length)
      console.log(imgs[item].src)
      document.getElementById("container")
        .style.backgroundImage = "url(" + imgs[item].src + ")";
    },
  },
  beforeDestroy() {
    document.body.classList.remove('userLayout')
  }
  ,
}
</script>

<style lang="less" scoped>
.body {
  //background-image: url(~@/assets/background/bg1.jpg) ;
  //background-repeat: no-repeat;
  //background-attachment: fixed;
}

#mask {
  background-color: rgba(255, 255, 255, 0.3);
  height: 100px;
  width: 100%;
  text-align: left;
  //position: absolute; /*设定footer绝对位置在底部*/
  bottom: 0;
  margin: 0px;
  padding: 0px;
}

#mask img {
  width: 100px;
  margin-top: 35px;
  cursor: pointer;
}

#userLayout.user-layout-wrapper {
  height: 100%;

  &.mobile {
    .container {
      .main {
        max-width: 368px;
        width: 98%;
      }
    }
  }

  .container {
    width: 100%;
    min-height: 100%;
    background: #f0f2f5 url(~@/assets/background/bg1.jpg) no-repeat 50%;
    background-repeat: no-repeat;
    background-attachment: fixed;
    background-size: 100%;
    padding: 110px 0 144px;
    position: relative;

    a {
      text-decoration: none;
    }

    .top {
      text-align: center;

      .header {
        height: 44px;
        line-height: 44px;

        .badge {
          position: absolute;
          display: inline-block;
          line-height: 1;
          vertical-align: middle;
          margin-left: -12px;
          margin-top: -10px;
          opacity: 0.8;
        }

        .logo {
          height: 44px;
          vertical-align: top;
          margin-right: 16px;
          border-style: none;
        }

        .title {
          font-size: 33px;
          color: rgba(0, 0, 0, .85);
          font-family: "Chinese Quote", -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
          font-weight: 600;
          position: relative;
          top: 2px;
        }
      }

      .desc {
        font-size: 14px;
        color: rgba(0, 0, 0, 0.45);
        margin-top: 12px;
        margin-bottom: 40px;
      }
    }

    .main {
      min-width: 260px;
      width: 368px;
      margin: 0 auto;
    }

    .footer {
      position: absolute;
      width: 100%;
      bottom: 0;
      padding: 0 16px;
      margin: 48px 0 24px;
      text-align: center;

      .links {
        margin-bottom: 8px;
        font-size: 14px;

        a {
          color: rgba(0, 0, 0, 0.45);
          transition: all 0.3s;

          &:not(:last-child) {
            margin-right: 40px;
          }
        }
      }

      .copyright {
        color: rgba(0, 0, 0, 0.45);
        font-size: 14px;
      }
    }
  }
}
</style>
