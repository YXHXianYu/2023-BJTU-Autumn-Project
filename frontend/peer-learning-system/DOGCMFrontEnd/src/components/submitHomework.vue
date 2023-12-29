<template>
    <div class="problem">

        <h1 style="text-align: center">提交作业</h1>

        <el-table :data="tableData" stripe style="width: 100%" height="250" align="center" empty-text="无需要提交的作业">
            <!-- <el-table-column prop="uuid" label="编号" width="180"> </el-table-column> -->
            <el-table-column prop="groupHomeworkName" label="题目名称" width="250"> </el-table-column>
            <el-table-column prop="submitDeadline" label="提交截止时间" width="100"> </el-table-column>
            <el-table-column prop="ratingDeadline" label="互评截止时间" width="100"> </el-table-column>
            <el-table-column label="查看作业内容" width="100">
                <template slot-scope="scope">
                    <el-button plain type="primary" size="small" @click="query(scope.$index, scope.row)">查看</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="score" label="成绩" width="100"> </el-table-column>
            <el-table-column label="申请复查" width="100">
                <template slot-scope="scope">
                    <el-button plain type="primary" size="small" @click="requestRecheck(scope.$index, scope.row)">复查</el-button>
                </template>
            </el-table-column>
            <el-table-column label="查看优秀作业" width="100">
                <template slot-scope="scope">
                    <el-button plain type="primary" size="small" @click="excellent(scope.row)">查看</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="isPublic" label="是否为公开作业" width="100"> </el-table-column>
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

        <el-dialog title="申请复查" :visible.sync="recheckVisible" width="70%" append-to-body>
            <span>复查原因</span><br><br>
            <template>
            <el-radio v-model="recheckReason" label="互评得分错误">互评得分错误</el-radio>
            <el-radio v-model="recheckReason" label="未被互评">未被互评</el-radio>
            <el-radio v-model="recheckReason" label="标准答案错误">标准答案错误</el-radio>
            <el-radio v-model="recheckReason" label="其他">其他</el-radio>
            </template><br><br>

            <span>详细原因</span><br><br>
            <el-input type="textarea" :autosize="{minRows: 2, maxRows: 10}" v-model="recheckDetailedReason" placeholder="请输入详细原因（为什么提交复查申请；申请复查的题目编号；期望得到什么处理），若原因不足则不受理复查申请" class="scrollable-textarea"></el-input>
            <br><br>

            <el-button plain type="primary" size="small" @click="recheck(recheckData, recheckReason, recheckDetailedReason)">提交申请</el-button>

            <!-- <div v-html="dialogContent"></div><br> -->
        </el-dialog>

        <el-dialog title="优秀作业列表" :visible.sync="excellentVisible" width="70%" append-to-body>
            <div v-if="excellentData.length > 0">
                <div v-for="(item, index) in excellentData" :key="index">
                    <h2>优秀作业编号: {{ index + 1 }}</h2>
                    <div v-html="item.answer"></div>
                </div>
            </div>
            <div v-else>
                <h2>暂无优秀作业</h2>
            </div>
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
            tableData: [
                {
                    "groupHomeworkName": "二次元轻小说写作大赛",
                    "submitDeadline": "2024/2/2",
                    "ratingDeadline": "2024/2/29",
                    "score": "Unsubmit",
                    "isPublic": "是",
                }
            ],
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
            recheckVisible: false,
            recheckData: {},
            recheckReason: "其他",
            recheckDetailedReason: "",
            excellentVisible: false,
            excellentData: [],
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
        requestRecheck(index, row) {
            this.recheckVisible = true
            this.recheckData = row
        },
        recheck(recheckData, recheckReason, recheckDetailedReason) {
            // recheck
            const that = this
            this.$axios({
                url: 'http://localhost:8080/api/homework/request_recheck',
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get("token"),
                    homeworkUUID: recheckData.homework,
                    recheckReason: recheckReason,
                    recheckDetailedReason: recheckDetailedReason,
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
        excellent(row) {
            this.$axios({
                url: "http://localhost:8080/api/excellent/get",
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get("token"),
                    groupHomeworkName: row.groupHomeworkName,
                }),
            }).then(response => {
                if(response.data.code === 200) {
                    this.excellentVisible = true
                    this.excellentData = response.data.data
                } else {
                    window.alert(
                        'code: ' + response.data.code + '\n' +
                        'message: ' + response.data.message
                    )
                }
            }).catch(error => {
                window.console.log("error: ", error)
            })
        }
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
        width: 60rem;
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
