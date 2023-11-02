<template>
    <div class="register">

        <h1 style="text-align: center">注册</h1>
        <el-form ref="registerForm" :model="registerForm" :rules="rules" label-width="100px">
            <!-- <el-form-item label="邮箱: " prop="email">
                <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item> -->

            <el-form-item label="用户名: " prop="username">
                <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
            </el-form-item>

            <el-form-item label="密码: " prop="password">
                <el-input v-model="registerForm.password" placeholder="请输入密码" show-password clearable></el-input>
            </el-form-item>

            <!-- <el-form-item label="Sensei: " prop="passwdagain">
                <el-checkbox v-model="registerForm.isTeacher"></el-checkbox>
            </el-form-item> -->

            <el-form-item label-width="0px">
                <el-button size="medium" style="border-color: #ffffff; margin-right: 43%; left: 0px;" @click="clickLogin">登录</el-button>
                <el-button type="primary" size="medium" :loading = "isLoading"  @click="clickRegister">{{text}}</el-button>
            </el-form-item>

        </el-form>
    </div>
</template>

<script>
import qs from 'qs'

export default {
    name: "register",
    data() {
        return {
            state: -1,
            text: "创建账号",
            isDisabled: false,
            isLoading: false,
            registerForm: {
                email:'2943003@qq.com',
                username: 'Guest',
                password: '114514',
                isTeacher: 0
            },
            rules: {
                username: [
                    { required: true, message: "请输入用户名", trigger: 'blur' },
                ],
                email: [//如果为空就显示message
                    { required: true, message: "请输入email", trigger: 'blur' },
                ],
                password: [
                    { required: true, message: "请输入密码", trigger: 'blur' },
                    { min: 6, message: "密码必须大于6", trigger: 'blur' }
                ]
            },
        }
    },

    methods: {
        clickRegister() {
            //获取到的是添加了ref="registerForm"属性的这个组件 前端判断
            this.$refs["registerForm"].validate((valid) => {
                if(valid) {
                    //alert("valid"+this.$registerUrl);//http://localhost:10087/user/register
                    const data = qs.stringify({
                        email: this.registerForm.email,//邮箱
                        username: this.registerForm.username,
                        password: this.registerForm.password,
                        isTeacher: this.registerForm.isTeacher,
                    })
                    this.$axios({
                        url: "http://localhost:8080/api/register",
                        method: 'post',
                        data: data,
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                    }).then(response => {
                        window.console.log("state", response.data)
                        window.alert(
                            'code: ' + response.data.code + '\n' +
                            'message: ' + response.data.message
                        )
                    }).catch(error => {
                        window.console.log("error: ", error)
                    })
                    // console.log('success')
                } else {
                    return false;
                }
            })
        },

        clickLogin() {
            this.$router.push("/login")
        }
    }
}
</script>

<style scoped>

    .register {
        margin: auto;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 20rem;
        position: absolute;
        background-color: rgba(255, 255, 255, 0.8);
        padding: 20px 20px 10px 20px;
        border-radius: 10px;
        box-shadow: 0px 15px 25px 0px rgba(0, 0, 0, 0.11);
    }

</style>
