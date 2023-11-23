<template>
    <div class="problem">

        <h1 style="text-align: center">提交互评</h1>

        <el-table :data="tableData" stripe style="width: 100%" height="250" align="center" empty-text="无需要提交的作业">
            <!-- <el-table-column prop="uuid" label="编号" width="180"> </el-table-column> -->
            <el-table-column prop="groupHomeworkName" label="作业名称" width="200"> </el-table-column>
            <el-table-column prop="problemName" label="题目名称" width="200"> </el-table-column>
            <el-table-column prop="score" label="你的给分" width="200"> </el-table-column>
            <el-table-column label="查看详细信息" width="120">
                <template slot-scope="scope">
                    <el-button plain type="primary" size="small" @click="showDetails(scope.row)">查看</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-form>
            <el-form-item label-width="0px">
                <el-button size="medium" style="border-color: #ffffff; margin-right: 60%;" @click="clickReturn">返回</el-button>
            </el-form-item>
        </el-form>

        <el-dialog title="详细信息" :visible.sync="dialogFormVisible" width="70%" append-to-body>
            <span>题目名称：{{dialogName}}</span><br><br>
            <span>题目内容：</span><br><br>
            <div v-html="dialogContent"></div><br>
            <span>参考的标准答案：</span><br><br>
            <div v-html="dialogStandardAnswer"></div><br>
            <span>其他同学提交的答案：</span><br><br>
            <div v-html="dialogAnswer"></div><br><br>
            <el-slider v-model="dialogScore"></el-slider><br><br>
            <el-button type="primary" size="medium"  @click="addRating()">添加</el-button><br>
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
            dialogRatingUUID: "",
            dialogScore: 50,
            rules: {},
        }
    },

    created() {
        this.$axios({
            url: "http://localhost:8080/api/rating/get_all_rating_and_problem",
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
        addRating() {
            this.$axios({
                url: "http://localhost:8080/api/rating/add",
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get("token"),
                    ratingUUID: this.dialogRatingUUID,
                    score: this.dialogScore,
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
        showDetails(row) {
            this.dialogFormVisible = true
            this.dialogName = row.problemName
            this.dialogContent = row.problemContent
            this.dialogStandardAnswer = row.problemStandardAnswer
            this.dialogRatingUUID = row.uuid
            this.dialogAnswer = row.answer
        },
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
