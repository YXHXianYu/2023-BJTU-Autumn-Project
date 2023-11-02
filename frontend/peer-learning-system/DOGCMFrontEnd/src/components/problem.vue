<template>
    <div class="problem">

        <h1 style="text-align: center">题目管理</h1>

        <el-table :data="tableData" stripe style="width: 100%" height="250" align="center" empty-text="权限不足">
            <!-- <el-table-column prop="uuid" label="编号" width="180"> </el-table-column> -->
            <el-table-column prop="name" label="名称" width="250"> </el-table-column>
            <el-table-column label="查看" width="100">
                <template slot-scope="scope">
                    <el-button plain type="primary" size="small" @click="queryProblem(scope.$index, scope.row)">查看</el-button>
                </template>
            </el-table-column>
            <el-table-column label="删除" width="100">
                <template slot-scope="scope">
                    <el-button plain type="primary" size="small" @click="deleteProblem(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <h1 style="text-align: center">添加题目</h1>

        <el-form ref="problemForm" :model="problemForm" :rules="rules" label-width="100px">

            <el-form-item label="题目名称: " prop="name" style="margin-right: 30px;">
                <el-input v-model="problemForm.name" placeholder="请输入题目名称"></el-input>
            </el-form-item>

            <el-form-item label="题目内容: " prop="content" style="margin-right: 30px;">
                <el-input type="textarea" :autosize="{minRows: 2, maxRows: 10}" v-model="problemForm.content" placeholder="请输入题目内容" class="scrollable-textarea"></el-input>
            </el-form-item>

            <el-form-item label="题目答案: " prop="standardAnswer" style="margin-right: 30px;">
                <el-input type="textarea" :autosize="{minRows: 2, maxRows: 4}" v-model="problemForm.standardAnswer" placeholder="请输入题目内容"></el-input>
            </el-form-item>

            <el-form-item label-width="0px">
                <el-button size="medium" style="border-color: #ffffff; margin-right: 60%;" @click="clickReturn">返回</el-button>
                <el-button type="primary" size="medium"  @click="clickAddProblem">添加题目</el-button>
            </el-form-item>
        </el-form>

        <el-dialog title="详细信息" :visible.sync="dialogFormVisible" width="70%" append-to-body>
            <span>题目名称：{{dialogName}}</span><br><br>
            <span>题目内容：</span><br><br>
            <div v-html="dialogContent"></div><br>
            <span>题目答案：</span><br><br>
            <div v-html="dialogStandardAnswer"></div><br>
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
            problemForm: {
                name: "",
                content: "",
                standardAnswer: "",
            },
            dialogFormVisible: false,
            dialogName: "",
            dialogContent: "",
            dialogStandardAnswer: "",
            rules: {},
        }
    },

    created() {
        this.$axios({
            url: "http://localhost:8080/api/problem/get_all",
            method: 'post',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            data: qs.stringify({
                token: SessionStorageService.get("token")
            })
        }).then(response => {
            // window.console.log("state", response.data)
            this.tableData = response.data.data
            // window.console.log("tableData: ", this.$store.tableData)
        }).catch(error => {
            window.console.log("error: ", error)
        })
    },

    methods: {
        clickAddProblem() {
            this.$axios({
                url: "http://localhost:8080/api/problem/add",
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get("token"),
                    name: this.problemForm.name,
                    // content: this.problemForm.content,
                    content: this.problemForm.content.replace(/\n/g, "<br>"),
                    standardAnswer: this.problemForm.standardAnswer.replace(/\n/g, "<br>"),
                }),
            }).then(response => {
                window.console.log("state", response.data)
                window.alert(
                    'code: ' + response.data.code + '\n' +
                    'message: ' + response.data.message
                )
            }).catch(error => {
                window.console.log("error: ", error)
            })
            this.$router.go(0)
        },
        clickReturn() {
            this.$router.push("/main")
        },
        queryProblem(index, row) {
            this.dialogFormVisible = true
            this.dialogName = row.name
            this.dialogContent = row.content
            this.dialogStandardAnswer = row.standardAnswer
        },
        deleteProblem(index, row) {
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
