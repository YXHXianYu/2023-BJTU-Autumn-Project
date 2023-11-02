<template>
    <div class="register">

        <h1 style="text-align: center">注册受限用户</h1>
        <el-form ref="registerForm" :model="registerForm" :rules="rules" label-width="100px">
            <el-form-item label="邮箱: " prop="email">
                <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>

            <el-form-item label="用户名: " prop="name">
                <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
            </el-form-item>

            <el-form-item label="密码: " prop="passwd">
                <el-input v-model="registerForm.password" placeholder="请输入密码" show-password clearable></el-input>
            </el-form-item>

            <el-tooltip class="item" effect="dark" content="老师只能布置作业等，管理员才拥有注册受限用户的权限" placement="bottom">
                <el-form-item label="是否为管理员: " prop="passwdagain">
                    <el-checkbox v-model="registerForm.isAdministrator"></el-checkbox>
                </el-form-item>
            </el-tooltip>

            <el-form-item label-width="0px">
                <el-button size="medium" style="border-color: #ffffff; margin-right: 43%; left: 0px;" @click="clickReturn">返回</el-button>
                <el-button type="primary" size="medium" :loading = "isLoading"  @click="clickRegister">{{text}}</el-button>
            </el-form-item>

        </el-form>
    </div>
</template>

<script>
import qs from 'qs'
import SessionStorageService from "../sessionStorageService.js"

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
                password: '20021012',
                isAdministrator: false
            },
            rules: {
                username: [
                    {
                        required: true, message: "请输入用户名", trigger: 'blur'
                    },
                ],
                email: [//如果为空就显示message
                    {
                        required: true, message: "请输入email", trigger: 'blur'
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
        clickRegister() {
            if (this.isLoading) {
                this.text = ""
            }
            //获取到的是添加了ref="registerForm"属性的这个组件 前端判断
            this.$refs["registerForm"].validate((valid) => {
                if(valid) {
                    //alert("valid"+this.$registerUrl);//http://localhost:10087/user/register

                    window.console.log("token: ", SessionStorageService.get('token'))

                    const data = qs.stringify({
                        token: SessionStorageService.get('token'),
                        email: this.registerForm.email,
                        username: this.registerForm.username,
                        password: this.registerForm.password,
                        isAdministrator: this.registerForm.isAdministrator,
                    })

                    this.$axios({
                        url: (this.registerForm.isAdministrator === false ? "http://localhost:8080/api/register_teacher" : "http://localhost:8080/api/register_administrator"),
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
                    this.text = "创建账户";
                    this.isLoading = false;
                    this.$alert('账号不成立！', '警告', {
                        confirmButtonText: '确定',
                        callback: {
                        }
                    });
                    return false;
                }
            })
        },
        clickReturn() {
            this.$router.push("/main")
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
        width: 25rem;
        position: absolute;
        background-color: white;
        padding: 20px 20px 10px 20px;
        border-radius: 10px;
        box-shadow: 0px 15px 25px 0px rgba(0, 0, 0, 0.11);
    }

</style>
