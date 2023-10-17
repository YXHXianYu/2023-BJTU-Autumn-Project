<template>
<!-- 待会签 -->
    <div>
        <!-- <div v-for="(contract, index) in contracts" :key="index">
      {{ contract }}
        </div> -->
        <el-table
                :data="tableData.filter(data => !search ||
                data.contract_name.toLowerCase().includes(search.toLowerCase()))"
                height="500"
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
                    <!--                    <el-button-->
                    <!--                            size="mini"-->
                    <!--                            @click="handleEdit(scope.$index, scope.row)">修改-->
                    <!--                    </el-button>-->

                    <el-button
                            size="mini"
                            @click="handleWatch(scope.$index, scope.row)">查看
                    </el-button>
                    <el-button
                            size="mini"
                            @click="handleEdit(scope.$index, scope.row)">会签
                    </el-button>
                    <!--                    <el-button-->
                    <!--                            size="mini"-->
                    <!--                            type="danger"-->
                    <!--                    >删除-->
                    <!--                    </el-button>-->
                    <!--                            @click="handleDelete(scope.$index, scope.row)"-->
                </template>
            </el-table-column>
        </el-table>


        <el-dialog title="修改合同" :visible.sync="dialogVisible">
            <edit-contract></edit-contract>
        </el-dialog>

        <el-dialog title="确认会签" :visible.sync="dialog2Visible" append-to-body>

           <el-form >
               <el-form-item label="会签意见">
                   <el-input type="textarea" :rows="3" v-model="msg"></el-input>
               </el-form-item>
           </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialog2Visible = false">取 消</el-button>
                <el-button type="primary" @click="postMsg()">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="查看合同" :visible.sync="dialog3Visible" append-to-body>

            <el-form ref="draftForm" :model="draftForm" label-position="left" label-width="85px">
                <el-form-item label="合同名称:" prop="name">
                    <el-input v-model="draftForm.name"></el-input>
                    <!-- draftForm.name -->
                </el-form-item>
                <el-form-item label="客户:" prop="userName">
                    <el-input v-model="draftForm.customer"></el-input>
                </el-form-item>
                <el-form-item label="活动时间" prop="name">
                    <el-col :span="12">
                    <el-input v-model="draftForm.start_time"></el-input>
                    </el-col>
                    <el-col :span="12">
                    <el-input v-model="draftForm.end_time"></el-input>
                    </el-col>
                    <!-- <el-input v-model="draftForm.start_time"></el-input> -->
                </el-form-item>
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
                

                <el-form-item label="合同内容" prop="info">
                    <el-input type="textarea" :autosize="{minRows:3, maxRows:6}" v-model="draftForm.content"></el-input>
                </el-form-item>

                <el-form-item label="下载合同" prop="info">
                    <el-button @click="DownFile">下载附件</el-button>
                    <!-- <el-input type="textarea" :autosize="{minRows:3, maxRows:6}" v-model="draftForm.content"></el-input> -->
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">

                <el-button type="primary" @click="dialog3Visible = false">确 定</el-button>
            </div>
        </el-dialog>
        <!--        <el-pagination-->
        <!--                background-->
        <!--                layout="prev, pager, next"-->
        <!--                :total="1000"-->
        <!--                align="center">-->
        <!--        </el-pagination>-->
    </div>
</template>

<script>
    export default {
        // mounted() {//一进入页面就发送user_name数据，获取待会签的信息
        //     this.sendDataToBackend();
        // },

        name: "counterSign",

        data() {
            // contracts:[];
            const generateData = () => {
                const data = [];
                //alert("前端：待会签"+this.$url + "/contract/selectContractByType")
                window.console.log("fasong"+this.$store.state.userName)
                this.$axios({

                    // url: this.$url + "/contract/selectContractByType",
                    url: this.$url + "/operator/counter",
                    method: 'post',
                    
                    data: {
                        types : 0,
                        matter :2,
                        user_name:this.$store.state.userName,
                        // token: this.$store.state.token,
                        token: this.$store.state.token,
                        type: 'counterSign'
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

                    res.data.contracts.forEach((item, index) => {
                        data.push({
                            index: index + 1,
                            date: item.beginTime,
                            contract_name: item,//name
                            id: item.id
                        })
                    })
                });

                return data;
            };
            return {

                search: '',
                tableData: generateData(),
                dialogVisible: false,
                dialog2Visible: false,
                dialog3Visible: false,
                msg: '',
                index: -1,
                row: -1,
                draftForm: {
                    name: '',
                    start_time: '',
                    customer:'',
                    end_time: '1010-10-10',
                    userName: this.$store.userName,//'',
                    date: '',
                    info: '',
                },
            }
        },
        methods: {
            DownFile(){
                window.open(this.$url + "/file/file_download" + "?contract_name=" + this.row.contract_name, '_self');
            },
            sendDataToBackend() {
                //alert("user="+this.$store.state.userName)
                // 构造请求的数据
                // const requestData = {
                //     user_name:this.$store.state.userName, // 从状态管理中获取用户名
                // };
                this.$axios({
                            url: "http://localhost:10087/mainFrame/counterSign",
                            method: 'post',
                            data: {
                                //contract_name:
                                user_name: this.$store.state.userName
                            },
                            //这个不能删！！
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
                                    message: '待会签！',
                                    type: "error"
                                })
                            }

                            if (res.data.state === 0) {
                                this.$notify({
                                    title: '成功',
                                    message: '待会签成功',
                                    type: "success"
                                })
                                ////////应该回收合同
                                this.constructs = res.data.constructs
                                // this.cusForm = this.cleanJson(this.cusForm)
                            }
                        })
                // this.$axios({//向指定资源提交数据
                //             url: this.$loginUrl,//请求路径
                            
                //             method: 'post',
                //             data: {//提交id 密码
                //                 user: this.form.name,
                //                 //passwd: this.$md5(this.form.passwd + this.$salt),
                //                 passwd: this.form.passwd,
                //             },

                //             transformRequest: [function (data) {
                //                 let ret = '';
                //                 for (let it in data) {
                //                     ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                //                 }
                //                 return ret
                //             }],
                //             headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                //             // headers: {
                //             //     'Content-Type': 'application/x-www-form-urlencoded'
                //             // }
                //         }).then(res => {
                //             //alert('okk!');
                //             this.text = "";
                //             this.isLoading = true;
                //             this.state = res.data.state;
                //             this.group = res.data.group;
                //             this.$store.state.userName = this.form.name;
                //             this.$store.state.passwd = this.form.passwd;
                //             this.$store.state.token = res.data.token;
                //             this.$store.state.group = res.data.group;
                //             this.loginProcess();
                //         })
                // 发送请求
                // this.$axios.post('http://localhost:10087/mainFrame/counterSign', requestData)
                // .then(response => {
                //     // 从响应中获取 contracts 数组
                //     const contracts = response.data.contracts;

                //     // 在前端进行后续操作，比如将 contracts 数组保存到组件的数据中
                //     this.contracts = contracts;
                // });
            },
            handleEdit(index, row) {
                this.dialog2Visible = true;
                this.index = index;
                this.row = row;
                // eslint-disable-next-line no-console
                console.log(index, row);


            },

            handleWatch(index, row) {
                window.console.log("watching: "+row.contract_name);
                this.index = index;
                this.row = row;

                //点击查看
                this.$axios({
                    // url:this.$url + "/contract/selContract",
                    url:this.$url + "/Check",
                    method: 'post',
                    data: {
                        contract_name:this.row.contract_name,
                        matter :2,
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
                if (res.data.state === -1) {
                    this.$notify({
                        title: '失败',
                        message: '获取失败！',
                        type: "error"
                    });

                    this.dialog3Visible = false;
                }

                if (res.data.state === 0) {
                    window.console.log(res.data.name+res.data.customer+res.data.start_time);
                    this.draftForm.name = this.row.contract_name;//res.data.name;
                    this.draftForm.customer = res.data.customer;
                    const date = [];
                    date[0] = res.data.start_time;
                    date[1] = res.data.end_time;
                    this.draftForm.start_time = res.data.start_time;
                    //this.draftForm.date = date;
                    this.draftForm.content = res.data.content;
                    this.draftForm.end_time = res.data.end_time;
                    // this.draftForm.name = res.data.data[0].name;
                    // this.draftForm.userName = res.data.data[0].customer;
                    // const date = [];
                    // date[0] = res.data.data[0].beginTime;
                    // date[1] = res.data.data[0].endTime;
                    // this.draftForm.date = date;
                    // this.draftForm.info = res.data.data[0].content;
                }
                this.dialogVisible = false;

            },

            postMsg(){
                //alert("待会签="+this.$url + "/contract/counter")
                //this.row = row;
                window.console.log("huiqianiiiiiiiiiii");
                window.console.log("post:"+this.row.contract_name);
                //const roww = this.tableData.find((item) => item.index === this.row.id);
                this.$axios({
                    // url:this.$url + "/contract/counter",
                    url:this.$url + "/operator/counter",
                    method: 'post',
                    data: {
                        types : 1, //1
                        user_name : this.$store.state.userName,
                        contract_name : this.row.contract_name,
                        content:this.msg,//" xxx",
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
                    
                    if (res.data.state === -1) {
                        this.$notify({
                            title: '失败',
                            message: '定稿失败！',
                            type: "error"
                        })
                    }

                    if (res.data.state === 0) {
                        // generateData();
                          
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
                            message: '会签成功！',
                            type: "success"
                        })
                      
                        this.dialog2Visible = false;
                    }

                    this.msg = ''
                    this.dialog2Visible = false;
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
