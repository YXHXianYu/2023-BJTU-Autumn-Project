<template>
<!-- 待审批 -->
    <div>
        <el-table
                :data="tableData.filter(data => !search ||
                data.contract_name.toLowerCase().includes(search.toLowerCase()))"
                height='500'
                style="width: 100%">
            <el-table-column
                    label="序号"
                    prop="index">
            </el-table-column>

            <el-table-column
                    label="合同名称"
                    prop="contract_name">
            </el-table-column>
            <el-table-column
                    label="起草时间"
                    prop="date">
            </el-table-column>
            <el-table-column
                    align="right">
                <template slot="header" slot-scope="
/* eslint-disable vue/no-unused-vars */
scope">
                    <el-input
                            v-model="search"
                            size="mini"
                            placeholder="输入关键字搜索"/>
                </template>
                <template slot-scope="scope">

                    <el-button
                            size="mini"
                            @click="handleWatch(scope.$index, scope.row)">查看
                    </el-button>
                    <el-button
                            size="mini"
                            @click="handleEdit(scope.$index, scope.row)">审批
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-dialog title="确认审批" :visible.sync="dialog2Visible" append-to-body>
            
                <el-form >
                    <el-form-item label="审批意见详情:">
                        <el-input type="textarea" :rows="3" v-model="msg"></el-input>
                </el-form-item>
                </el-form >
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialog2Visible = false">取 消</el-button>
                 <el-button type="danger" @click="postMsg(0)" >拒 绝</el-button>
                <el-button type="primary" @click="postMsg(1)" >确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="查看" :visible.sync="dialog3Visible" append-to-body>

            <el-form ref="draftForm" :model="draftForm" label-position="left" label-width="85px">
                <el-form-item label="合同名称:" prop="name">
                    <el-input v-model="draftForm.name"></el-input>
                </el-form-item>
                <el-form-item label="客户:" prop="userName">
                    <el-input v-model="draftForm.customer"></el-input>
                </el-form-item>
                <el-form-item label="起草时间" prop="date">
                    <el-col :span="12">
                    <el-input v-model="draftForm.start_time"></el-input>
                    </el-col>
                    <el-col :span="12">
                    <el-input v-model="draftForm.end_time"></el-input>
                    </el-col>
                    <!-- <el-input v-model="draftForm.start_time"></el-input> -->
                    <!-- <el-date-picker
                            v-model="draftForm.date"
                            type="daterange"
                            align="right"
                            unlink-panels
                            range-separator="至"
                            value-format="yyyy-MM-dd"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
                    </el-date-picker> -->
                </el-form-item>

                <el-form-item label="合同内容" prop="info">
                    <!-- <el-button @click="DownFile">下载附件</el-button> -->
                    <el-input type="textarea" :autosize="{minRows:3, maxRows:6}" v-model="draftForm.info"></el-input>
                </el-form-item>
                <el-form-item label="下载合同" prop="info">
                    <el-button @click="DownFile">下载附件</el-button>
                    <!-- <el-input type="textarea" :autosize="{minRows:3, maxRows:6}" v-model="draftForm.info"></el-input> -->
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="dialog3Visible = false">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "watchContract",

        data() {
            const generateData = () => {
                //alert("待审批"+this.$url + "/contract/selectContractByType")
                const data = [];
                this.$axios({
                    // url: this.$url + "/contract/selectContractByType",
                    url: this.$url + '/operator/approve',
                    method: 'post',
                    data: {
                        types : 0,
                        matter :4,
                        user_name:this.$store.state.userName,
                        // token: this.$store.state.token,
                        token: this.$store.state.token,
                        type: 'watchContract'
                        // page:this.$store.state
                    },
                    transformRequest: [function (data) {
                        let ret = '';
                        for (let it in data) {
                            ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                        }
                        return ret
                    }],
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}

                }).then(res => {
                    //window.console.log(res.data)
                    res.data.approves.forEach((item, index) => {
                        data.push({
                            index: index + 1,
                            //date: item.beginTime,
                            contract_name: item,
                            id: item.id
                        })
                    })
                });
                return data;
            };
            return {
                search: '',
                tableData: generateData(),
                dialog2Visible: false,
                dialog3Visible: false,
                msg: '',
                index: -1,
                row: -1,
                draftForm: {
                    start_time: '',
                    end_time: '',
                    customer:'',
                    name: '',
                    userName: '',
                    date: '',
                    info: '',
                },
            }
        },
        methods: {
            DownFile(){
                window.open(this.$url + "/file/file_download" + "?contract_name=" + this.row.contract_name, '_self');
            },
            handleEdit(index, row) {
                this.dialog2Visible = true;
                this.index = index;
                this.row = row;
                // eslint-disable-next-line no-console
                console.log(index, row);
            },

            handleWatch(index, row) {
                //window.console.log(row.contract_name)
                this.index = index;
                this.row = row;


                this.$axios({
                    // url:this.$url + "/contract/selContract",
                    url:this.$url + '/Check',
                    method: 'post',
                    data: {
                        matter :4,
                        contract_name:this.row.contract_name,
                        user_name:this.$store.state.userName,
                        token: this.$store.state.token,
                        id: this.row.id,
                    },
                    transformRequest: [function (data) {
                        let ret = '';
                        for (let it in data) {
                            ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                        }
                        return ret
                    }],
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).then(res => {
                    this.handleData(res);
                });
                this.dialog3Visible = true;
            },
            handleData(res) {
                window.console.log(res.data)
                if (res.data.state === -1) {
                    this.$notify({
                        title: '失败',
                        message: '获取失败！',
                        type: "error"
                    });
                    this.dialog3Visible = false;
                    //this.dialog2Visible = true
                }

                if (res.data.state === 0) {
                    this.draftForm.name = this.row.contract_name;
                    this.draftForm.customer = res.data.customer;
                    this.draftForm.start_time = res.data.start_time;
                    this.draftForm.end_time = res.data.end_time;
                    const date = [];
                    date[0] = res.data.beginTime;
                    date[1] = res.data.endTime;
                    this.draftForm.date = date;
                    this.draftForm.info = res.data.content;
                }
                this.dialogVisible = false;
                // console.log("test",this.draftForm)
            },

            postMsg(para){
                this.dialog2Visible = false;
                // const roww = this.tableData.find((item) => item.index === this.row.id);
                //alert("待审批="+this.$url + "/contract/watch")
                this.$axios({
                    // url:this.$url + "/contract/watch",
                    url:this.$url + '/operator/approve',
                    method: 'post',
                    data: {
                        types : 1,
                        user_name : this.$store.state.userName,
                        contract_name : this.row.contract_name,
                        token: this.$store.state.token,
                        content:this.msg,
                        id: this.row.id,
                        // 人为同意
                        accept : para
                    },
                    transformRequest: [function (data) {
                        let ret = '';
                        for (let it in data) {
                            ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                        }
                        return ret
                    }],
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).then(res => {

                    if (res.data.state === -1) {
                        this.$notify({
                            title: '失败',
                            message: '审批失败！',
                            type: "error"
                        })
                    }

                    if (res.data.state === 0) {
                        // var data = [];
                        // res.data.data.forEach((item, index) => {
                        //     data.push({
                        //         index: index + 1,
                        //         date: item.beginTime,
                        //         contract_name: item.name,
                        //         id: item.id
                        //     })
                        // });

                        // this.tableData = data;
                        this.tableData.splice(this.index, 1);
                        this.$notify({
                            title: '成功',
                            message: '审批成功！',
                            type: "success"
                        })
                    }

                    this.msg = '';
                    //this.dialog2Visible = false;
                })
            },

            handleDelete(index, row) {
                // eslint-disable-next-line no-console
                console.log(index, row);
            },

            comment() {
                // this.$router.push('/CommentBox');
                this.$prompt('定稿意见', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消'
                }).then(({value}) => {
                    this.$message({
                        type: 'success',
                        message: '提交成功！定稿意见为：' + value
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '取消输入'
                    });
                });
                // this.$alert('<strong>这是 <i>HTML</i> 片段</strong>', 'HTML1 片段', {
                //     dangerouslyUseHTMLString: true
                // });
            }

        },
    }
</script>

<style scoped>

</style>
