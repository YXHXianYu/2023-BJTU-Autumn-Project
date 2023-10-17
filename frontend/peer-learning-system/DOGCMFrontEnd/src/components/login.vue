<template>
    <div class="login">

        <h1 style="text-align: center">登录</h1>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="用户名：" prop="name">
                <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
            </el-form-item>

            <el-form-item label="密码：" prop="password">
                <el-input v-model="form.password" placeholder="请输入密码" show-password></el-input>
            </el-form-item>

            <el-form-item label-width="0px">
                <el-button size="medium" style="border-color: #ded9d9; margin-right: 43%; left: 0px;" @click="clickRegister">创建账户</el-button>
                <el-button type="primary" size="medium" :loading = "isLoading" @click="ClickLogin" >{{text}}</el-button>
            </el-form-item>

        </el-form>
    </div>
</template>

<script>
import qs from "qs"

export default {
    name: "login",
    data() {
        return {
            state: -1,
            group: 0,
            text: "登录",
            isDisabled: false,
            isLoading: false,
            form: {
                username: 'Guest',
                password: '114514',
            },
            rules: {
                username: [
                    {
                        required: true, message: "请输入用户名", trigger: 'blur'
                    },
                ],
                password: [
                    {
                        required: true, message: "请输入密码", trigger: 'blur'
                    },
                    {
                        min: 6, message: "密码必须大于6", trigger: 'blur'
                    }
                ]
            },
        }
    },
    methods: {
        ClickLogin() {

            // this.isLoading = !this.isLoading;
            // this.text = "";
            // console.log('clicklog');
            
            if (this.isLoading) {
                this.text = ""
            }
            //this.$alert('clcik'+this.isLoading);
            this.$refs["form"].validate((valid) => {

                if (valid) {
                    this.$axios({//向指定资源提交数据
                        url: "http://localhost:8080/api/login",//请求路径
                        
                        method: 'post',
                        data: qs.stringify({
                            username: this.form.username,
                            password: this.form.password,
                        }),
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                    }).then(response => {
                        window.console.log("state", response.data)

                        if(response.data.code == 200) {
                            this.text = "";
                            this.isLoading = true;
                            this.state = response.data.state;
                            this.group = response.data.group;
                            this.$store.state.username = this.form.username;
                            this.$store.state.password = this.form.password;
                            this.$store.state.token = response.data.token;
                            this.$store.state.group = response.data.group;
                            this.loginProcess();
                        } else {
                            window.alert(
                                'code: ' + response.data.code + '\n' +
                                'message: ' + response.data.message
                            )
                        }
                    })
                } else {
                    this.text = "登录";
                    this.isLoading = false;
                    // console.log('error submit!!');
                    return false;
                }
            });
        },

        loginProcess() {
            if (this.state === 0) {
                this.$store.state.userName = this.form.name;
                if (this.group === 1) {
                    this.$router.push('/mainFrame')
                }

                if (this.group === 2) {
                    this.$router.push('/manager')
                }
            } else {
                this.text = "登录";
                this.isLoading = false;

                if (this.state === 1) {
                    this.$alert('账号不存在！', '警告', {
                        confirmButtonText: '确定',
                        callback: {
                        }
                    });
                }

                if (this.state === -1) {
                    this.$alert('密码错误！', '警告', {
                        confirmButtonText: '确定',
                        callback: {
                        }
                    });
                }
            }
        },
        clickRegister() {
            
            //实现路由跳转 注册页面
            this.$router.push("/register")
        }
    },




}
</script>

<style scoped>

    .login {
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
