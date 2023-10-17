<template>
  <el-container style="width: 100%;height: 100%; top: 0; bottom: 0;">
    <el-header style="padding: 0">

      <el-menu  class="el-menu-mainBar" mode="horizontal" menu-align="end"
                background-color= "rgba(255, 255, 255, 0.5)" text-color="#000000" :router=true active-text-color="#5c76eb" border-color="rgba(255, 255, 255, 0.5)">
        <el-menu-item v-show="false">
          <el-button class="foldButton" :icon="myIcon" type="text"
                     style="background-color: rgba(0, 0, 0, 0);
                               border: 0px; height: 100%; width: 100%;
                                padding-top: auto; padding-bottom: auto;"
                     @click="clickFold"></el-button>
        </el-menu-item>



        <el-submenu class="main-submenu" index="6">
          <template slot="title">{{this.$store.state.userName}}</template>
          <el-menu-item index='login'>注销</el-menu-item>
        </el-submenu>

        <!-- <el-menu-item class="main-submenu" index="5">
            <template slot="title">增加客户</template>
        </el-menu-item> -->
        <el-menu-item class="main-submenu" index="addcustomer">
          <template slot="title">添加客户</template>
        </el-menu-item>
        <!-- <el-menu-item index="sign">添加客户</el-menu-item> -->
        <el-submenu class="main-submenu" index="4">
          <template slot="title">合同签订</template>
          <el-menu-item index="sign">待签订合同</el-menu-item>
        </el-submenu>

        <el-submenu class="main-submenu" index="3">
          <template slot="title">合同审批</template>
          <el-menu-item index="watchContract">待审批合同</el-menu-item>
        </el-submenu>

        <el-submenu class="main-submenu" index="2">
          <template slot="title">合同会签</template>
          <el-menu-item index="counterSign">待会签合同</el-menu-item>
        </el-submenu>

        <el-submenu class="main-submenu" index="/mainFrame">
          <template slot="title">合同起草</template>
          <el-menu-item index="draft" >起草合同</el-menu-item>
          <el-menu-item index="uncommitted">待定稿合同</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-header>


    <el-container direction="horizontal" style=" height: 100%;width: 100%; top: 0; bottom: 0;">
      <el-aside v-show="false" width="20%" style="height: 100%">
        <el-menu
            class="el-menu-vertical"
            style="height: 100%"
            :collapse="isFold">

          <el-menu-item index="1">
            <i class="el-icon-document-add"></i>
            <span slot="title">起草合同</span>
          </el-menu-item>
          <el-menu-item index="2">
            <i class="el-icon-document"></i>
            <span slot="title">待定稿合同</span>
          </el-menu-item>
          <el-menu-item index="3">
            <i class="el-icon-document-checked"></i>
            <span slot="title">已定稿合同</span>
          </el-menu-item>
          <el-menu-item index="4">
            <i class="el-icon-setting"></i>
            <span slot="title">流程查询</span>
          </el-menu-item>
          <!-- <el-menu-item index="5">
              <i class="el-icon-customer-add"></i>
              <span slot="title">添加客户</span>
          </el-menu-item> -->
        </el-menu>
      </el-aside>

      <el-main>

        <router-view></router-view>
      </el-main>

    </el-container>
  </el-container>
</template>

<script>


export default {
  name: "mainFrame",

  data() {
    return {
      fold: 'el-icon-s-unfold',
      unfold: 'el-icon-s-fold',
      myIcon: 'el-icon-s-fold',
      isFold: true,
    }
  },

  mounted: function () {
    this.load();
  },
  methods: {
    clickFold() {
      this.isFold = !this.isFold;
      this.myIcon = this.myIcon === this.unfold ? this.fold : this.unfold;
    },

    load() {
      if (this.$store.state.token === "") {
        this.$alert('请登录账号', '警告', {
          confirmButtonText: '确定',
          callback: {}
        });
        this.$router.push("/login");
      }
    },
  }
}
</script>

<style scoped>


.main-submenu {
  float: right;
}

.el-menu-mainBar {
  margin-left: 0;
  margin-top: 0;
}


</style>

<style>
/*.el-submenu__title i {*/
/*    color: #ffffff;*/
/*}*/

.tac {
  margin: 0;
  height: 100%;
}

html, body {
  margin: 0;
  top: 0;
  bottom: 0;
  height: 100%;
}

.el-menu-mainBar i {
  color: #ffffff;
}


</style>