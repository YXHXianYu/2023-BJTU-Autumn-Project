<template>
  <el-container class="main">
    <el-header>
      <h1 style="text-align: center">主菜单</h1>
    </el-header>
    <el-main>
      <el-row :gutter="20">
        <el-col :span="8" :offset="8">
          <el-card align="center">
            <el-button class="button-margin" type="primary" size="medium" @click="clickProblem" plain>题目管理</el-button>
            <el-button class="button-margin" type="primary" size="medium" @click="clickRegisterProtectedUser" plain>注册受保护用户</el-button>
            <el-button class="button-margin" type="primary" size="medium" @click="clickLogout" plain>登出</el-button>
          </el-card>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import SessionStorageService from "../sessionStorageService.js"

export default {
    name: "main",
    data() {
        return {
        }
    },
    methods: {
        clickProblem() {
            this.$router.push("/problem")
        },
        clickRegisterProtectedUser() {
            this.$router.push("/registerProtectedUser")
        },
        clickLogout() {
            SessionStorageService.remove("token")
            this.$router.push("/login")
        }
    }
}
</script>

<style scoped>
    .button-margin {
        margin: 10px;
    }

    .card {
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .main {
        margin: auto;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 50rem;
        position: absolute;
        background-color: white;
        border-radius: 10px;
    }
</style>
