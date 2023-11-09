<template>
    <div class="problem">

        <h1 style="text-align: center">提交作业</h1>

        <el-table :data="tableData" stripe style="width: 100%" height="250" align="center" empty-text="权限不足">
            <!-- <el-table-column prop="uuid" label="编号" width="180"> </el-table-column> -->
            <el-table-column prop="groupHomeworkName" label="题目名称" width="250"> </el-table-column>
            <el-table-column prop="submitDeadline" label="提交截止时间" width="100"> </el-table-column>
            <el-table-column prop="ratingDeadline" label="互评截止时间" width="100"> </el-table-column>
            <el-table-column label="查看" width="100">
                <template slot-scope="scope">
                    <el-button plain type="primary" size="small" @click="query(scope.$index, scope.row)">查看</el-button>
                </template>
            </el-table-column>
        </el-table>

        <h1 style="text-align: center">添加作业</h1>

        <el-form ref="form" :model="form" :rules="rules" label-width="100px">

            <el-form-item label="选择题目: " prop="groupHomeworkName" style="margin-left: 30px; margin-right: 30px;">
                <el-select v-model="form.groupHomeworkName" placeholder="请选择题目">
                    <el-option v-for="item in tableData" :key="item.groupHomeworkName" :label="item.groupHomeworkName" :value="item.groupHomeworkName"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="你的作业: " prop="content" style="margin-left: 30px; margin-right: 30px;">
                <el-input type="textarea" :autosize="{minRows: 2, maxRows: 10}" v-model="form.answer" placeholder="请输入你的作业" class="scrollable-textarea"></el-input>
            </el-form-item>

            <el-form-item label-width="0px">
                <el-button size="medium" style="border-color: #ffffff; margin-right: 60%;" @click="clickReturn">返回</el-button>
                <el-button type="primary" size="medium"  @click="clickAdd">添加</el-button>
            </el-form-item>
        </el-form>

        <el-dialog title="详细信息" :visible.sync="dialogFormVisible" width="70%" append-to-body>
            <span>题目名称：{{dialogName}}</span><br><br>
            <span>题目内容：</span><br><br>
            <div v-html="dialogContent"></div><br>
            <!-- <span>题目答案：</span><br><br>
            <div v-html="dialogStandardAnswer"></div><br> -->
            <span>已提交的答案：</span><br><br>
            <div v-html="dialogAnswer"></div><br>
        </el-dialog>
    </div>
</template>


<script>
import qs from 'qs'
import SessionStorageService from "../sessionStorageService.js"

export default {
    name: "register",
    data() {
        return {
            isLoading: false,
            tableData: [],
            problems: [],
            users: [],
            selectedUsers: [],
            form: {
                groupHomeworkName: "",
                answer: "",
            },
            dialogFormVisible: false,
            dialogName: "",
            dialogContent: "",
            dialogStandardAnswer: "",
            dialogAnswer: "",
            rules: {},
        }
    },

    created() {
        this.$axios({
            url: "http://localhost:8080/api/homework/get_all_user_with_problem",
            method: 'post',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            data: qs.stringify({
                token: SessionStorageService.get("token")
            })
        }).then(response => {
            this.tableData = response.data.data
        }).catch(error => {
            window.console.log("error: ", error)
        })
    },

    methods: {
        clickAdd() {
            this.$axios({
                url: "http://localhost:8080/api/homework/set_answer",
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get("token"),
                    groupHomeworkName: this.form.groupHomeworkName,
                    answer: this.form.answer.replace(/\n/g, "<br>"),
                }),
            }).then(response => {
                window.console.log("state", response.data)
                window.alert(
                    'code: ' + response.data.code + '\n' +
                    'message: ' + response.data.message
                )
                if(response.data.code === 200) {
                    this.$router.go(0)
                }
            }).catch(error => {
                window.console.log("error: ", error)
            })
        },
        clickReturn() {
            this.$router.push("/main")
        },
        query(index, row) {
            this.dialogFormVisible = true
            this.dialogName = row.problemName
            this.dialogContent = row.problemContent
            this.dialogStandardAnswer = row.problemStandardAnswer
            this.dialogAnswer = row.answer
        },
        delete(index, row) {
            const that = this
            this.$axios({
                url: "http://localhost:8080/api/problem/delete",
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get("token"),
                    name: row.name,
                }),
            }).then(response => {
                window.alert(
                    'code: ' + response.data.code + '\n' +
                    'message: ' + response.data.message
                )

                if(response.data.code === 200) {
                    that.$router.go(0)
                }
            }).catch(error => {
                window.console.log("error: ", error)
            })
        },
        // addWheelListener(event) {
        //     event.target.addEventListener('wheel', this.handleWheel)
        // },
        // removeWheelListener(event) {
        //     event.target.removeWheelListener('wheel', this.handleWheel)
        // },
        // handleWheel(event) {

        // },
    }
}
</script>

<style scoped>

    .problem {
        margin: auto;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 50rem;
        position: absolute;
        background-color: rgba(255, 255, 255, 0.8);
        /* padding: 20px 20px 10px 20px; */
        border-radius: 10px;
        /* box-shadow: 0px 15px 25px 0px rgba(0, 0, 0, 0.11); */
    }
    .scrollable-textarea {
        overflow-y: auto;
    }

</style>
