<template>
    <div class="problem">

        <h1 style="text-align: center">作业管理</h1>

        <el-table :data="tableData" stripe style="width: 100%" height="250" align="center" empty-text="权限不足">
            <!-- <el-table-column prop="uuid" label="编号" width="180"> </el-table-column> -->
            <el-table-column prop="name" label="名称" width="250"> </el-table-column>
            <!-- <el-table-column prop="problemUUID" label="问题ID" width="250"> </el-table-column> -->
            <el-table-column label="发布互评任务" width="120">
                <template slot-scope="scope">
                    <el-button plain type="primary" size="small" @click="create(scope.row)">发布</el-button>
                </template>
            </el-table-column>
            <el-table-column label="截至互评任务" width="120">
                <template slot-scope="scope">
                    <el-button plain type="primary" size="small" @click="finish(scope.row)">截至</el-button>
                </template>
            </el-table-column>
            <el-table-column label="查看学生" width="120">
                <template slot-scope="scope">
                    <el-button plain type="primary" size="small" @click="showStudentsScore(scope.row)">查看</el-button>
                </template>
            </el-table-column>
        </el-table>

        <h1 style="text-align: center">添加作业</h1>

        <el-form ref="form" :model="form" :rules="rules" label-width="100px">

            <el-form-item label="作业名称: " prop="groupHomeworkName" style="margin-left: 30px; margin-right: 30px;">
                <el-input v-model="form.groupHomeworkName" placeholder="请输入作业名称"></el-input>
            </el-form-item>

            <el-form-item label="选择题目: " prop="problemName" style="margin-left: 30px; margin-right: 30px;">
                <el-select v-model="form.problemName" placeholder="请选择题目">
                    <el-option v-for="item in problems" :key="item.name" :label="item.name" :value="item.name"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="提交截止时间: " prop="submitDeadline" style="margin-left: 30px; margin-right: 30px;">
                <el-date-picker v-model="form.submitDeadline" type="date" placeholder=""></el-date-picker>
            </el-form-item>

            <el-form-item label="互评截止时间: " prop="ratingDeadline" style="margin-left: 30px; margin-right: 30px;">
                <el-date-picker v-model="form.ratingDeadline" type="date" placeholder=""></el-date-picker>
            </el-form-item>

            <!-- selectType selectedHomework-->

            <el-tooltip class="item" effect="dark" content="是否直接选择一次作业的所有学生" placement="left">
                <el-form-item label="快速选择: " prop="students" style="margin-left: 30px; margin-right: 30px;">
                    <el-switch v-model="selectType" active-text="是" inactive-text="否"></el-switch>
                </el-form-item>
            </el-tooltip>

            <el-form-item v-if="selectType" label="选择作业: " prop="selectHomework" style="margin-left: 30px; margin-right: 30px;">
                <el-select v-model="selectedHomework" placeholder="请选择作业">
                    <el-option v-for="item in tableData" :key="item.uuid" :label="item.name" :value="item.uuid"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item v-else label="选择学生: " prop="students" style="margin-left: 30px; margin-right: 30px;">
                <el-select v-model="selectedUsers" multiple placeholder="请选择学生">
                    <el-option v-for="item in users" :key="item.uuid" :label="item.username" :value="item.uuid"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="作业被评次数: " prop="students" style="margin-left: 30px; margin-right: 30px;">
                <el-tooltip class="item" effect="dark" content="只有在 “发布互评任务” 时，才需要填写此表格" placement="bottom">
                    <el-input-number v-model="ratingNumber" :min="1" :max="5" label=""></el-input-number>
                </el-tooltip>
            </el-form-item>

            <el-form-item label-width="0px">
                <el-button size="medium" style="border-color: #ffffff; margin-right: 60%;" @click="clickReturn">返回</el-button>
                <el-button type="primary" size="medium"  @click="clickAdd">添加</el-button>
            </el-form-item>
        </el-form>

        <el-dialog title="作业完成情况" :visible.sync="dialogFormVisible" width="70%" append-to-body>
            <h2>作业名称：{{dialogName}}</h2><br>
            <h2>完成情况：</h2><br>

            <el-table :data="studentsScore" style="width: 100%">
            <el-table-column prop="username" label="学生名" width="180"> </el-table-column>
            <el-table-column prop="averageScore" label="当前分数" width="180"> </el-table-column>
            <el-table-column prop="checkedScore" label="最终分数" width="180"> </el-table-column>
            <el-table-column prop="haveRatingRatio" label="已评分比例" width="180"> </el-table-column>
            <el-table-column prop="isNeedToCheck" label="学生是否请求复查" width="180"> </el-table-column>
            <el-table-column label="修改分数/复查" width="120">
                <template slot-scope="scope">
                    <el-button plain type="primary" size="small" @click="recheck(scope.row)">查看</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="isExcellent" label="是否为优秀作业" width="180"> </el-table-column>
            <el-table-column label="取反优秀作业状态" width="120">
                <template slot-scope="scope">
                    <el-button plain type="primary" size="small" @click="excellent(scope.row)">取反</el-button>
                </template>
            </el-table-column>
            </el-table>

            <!-- <div v-html="dialogContent"></div><br> -->
        </el-dialog>

        <el-dialog title="修改分数/复查" :visible.sync="recheckVisible" width="70%" append-to-body>
            <h2>作业名称</h2><br>
            <div v-html="recheckData.homeworkName"></div><br>
            <h2>题目名称</h2><br>
            <div v-html="recheckData.problemName"></div><br>
            <h2>题目内容</h2><br>
            <div v-html="recheckData.problemContent"></div><br>
            <h2>标准答案</h2><br>
            <div v-html="recheckData.standardAnswer"></div><br>
            <h2>学生答案</h2><br>
            <div v-html="recheckData.answer"></div><br>
            <h2>申请复查原因</h2><br>
            <div v-html="recheckData.recheckReason"></div><br>
            <h2>申请复查详细原因</h2><br>
            <div v-html="recheckData.recheckDetailedReason"></div><br>

            <div>
            <h2>新的成绩</h2>
            <el-slider v-model="recheckData.score"></el-slider>
            </div><br>

            <el-button plain type="primary" size="small" @click="recheck_update(recheckData)">更新分数</el-button>

            <!-- <div v-html="dialogContent"></div><br> -->
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
            selectType: false,
            selectedUsers: [],
            selectedHomework: "",
            form: {
                groupHomeworkName: "",
                problemName: "",
                submitDeadline: "",
                ratingDeadline: "",
            },
            dialogFormVisible: false,
            dialogName: "",
            dialogContent: "",
            dialogStandardAnswer: "",
            ratingNumber: 3,
            rules: {},
            studentsScore: [],
            recheckVisible: false,
            recheckData: {},
        }
    },

    created() {
        this.$axios({
            url: "http://localhost:8080/api/group_homework/get_all",
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

        this.$axios({
            url: "http://localhost:8080/api/problem/get_all",
            method: 'post',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            data: qs.stringify({
                token: SessionStorageService.get("token")
            })
        }).then(response => {
            this.problems = response.data.data
        }).catch(error => {
            window.console.log("error: ", error)
        })

        this.$axios({
            url: "http://localhost:8080/api/user/get_all",
            method: 'post',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            data: qs.stringify({
                token: SessionStorageService.get("token")
            })
        }).then(response => {
            this.users = response.data.data
        }).catch(error => {
            window.console.log("error: ", error)
        })
    },

    methods: {
        clickAdd() {
            this.$axios({
                url: "http://localhost:8080/api/create_group_homework",
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get("token"),
                    groupHomeworkName: this.form.groupHomeworkName,
                    problemName: this.form.problemName,
                    submitDeadline: this.form.submitDeadline,
                    ratingDeadline: this.form.ratingDeadline,
                    selectType: this.selectType,
                    students: JSON.stringify(this.selectedUsers),
                    selectedHomework: this.selectedHomework,
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
        create(row) {
            const that = this
            this.$axios({
                url: "http://localhost:8080/api/rating/create",
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get("token"),
                    groupHomeworkName: row.name,
                    ratingNumber: this.ratingNumber,
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
        finish(row) {
            const that = this
            this.$axios({
                url: "http://localhost:8080/api/rating/finish",
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get("token"),
                    groupHomeworkName: row.name,
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
        showStudentsScore(row) {
            this.$axios({
                url: 'http://localhost:8080/api/rating/get_all_students_and_score',
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get('token'),
                    groupHomeworkName: row.name,
                }),
            }).then(response => {
                this.dialogFormVisible = true
                this.dialogName = row.name
                this.studentsScore = response.data.data
            }).catch(error => {
                window.console.log("error: ", error)
            })
        },
        recheck(row) {
            this.$axios({
                url: 'http://localhost:8080/api/recheck/get',
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get('token'),
                    groupHomeworkName: row.groupHomeworkName,
                    studentUserUUID: row.userUUID,
                }),
            }).then(response => {
                this.recheckVisible = true
                this.recheckData = response.data.data
                this.recheckData.groupHomeworkName = row.groupHomeworkName
                this.recheckData.studentUserUUID = row.userUUID
            }).catch(error => {
                window.console.log("error: ", error)
            })
        },
        recheck_update(recheckData) {
            const that = this
            this.$axios({
                url: 'http://localhost:8080/api/recheck/set',
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get('token'),
                    groupHomeworkName: recheckData.groupHomeworkName,
                    studentUserUUID: recheckData.studentUserUUID,
                    score: recheckData.score,
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
            const that = this
            this.$axios({
                url: 'http://localhost:8080/api/excellent/negation',
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: qs.stringify({
                    token: SessionStorageService.get('token'),
                    groupHomeworkName: row.groupHomeworkName,
                    studentUUID: row.userUUID,
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
