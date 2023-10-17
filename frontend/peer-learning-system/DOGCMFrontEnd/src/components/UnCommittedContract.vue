<template>
    <div>
        <el-table
                :data="tableData.filter(data => !search ||
                data.contract_name.toLowerCase().includes(search.toLowerCase()))"
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
                    @click="handleEdit(scope.$index, scope.row)">定稿
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


        <el-dialog title="修改合同" :visible.sync="dialogVisible" append-to-body>
            <edit-contract></edit-contract>
        </el-dialog>

        <el-dialog title="定稿意见" :visible.sync="dialog2Visible" append-to-body>

            <el-form >
                <el-form-item label="修改内容:">
                    <el-input type="textarea" :rows="3" v-model="msg"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialog2Visible = false">取 消</el-button>
                <!-- 修改了参数scope.$index, scope.row -->
                <el-button type="primary" @click="postMsg()">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="查看合同" :visible.sync="dialog3Visible" append-to-body>

            <el-form ref="draftForm" :model="draftForm" :rules="rule" label-position="left" label-width="85px">
                <el-form-item label="合同名称:" prop="name">
                    <el-input v-model="draftForm.name"></el-input>
                </el-form-item>
                <el-form-item label="客户:" prop="userName">
                    <el-input v-model="draftForm.customer"></el-input>
                </el-form-item>
                <el-form-item label="活动时间" prop="date">
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
                    <el-input type="textarea" :autosize="{minRows:3, maxRows:6}" v-model="draftForm.info"></el-input>
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
    /* eslint-disable no-console */

    import EditContract from "@/components/EditContract";
//import { it } from "node:test";
//import { addListener } from "process";

    export default {
        name: 'UnCommittedContract',
        components: {EditContract},
        data() {
            //alert("待定稿 "+this.$url + "/contract/selectContractByType")
            const generateData = () => {
                const data = [];
                
                this.$axios({
                    // url: this.$url + "/contract/selectContractByType",
                    url: this.$url + "/operator/finalize",
                    method: 'post',
                    data: {
                        //contract_name:roww.contract_name,
                        types : 0,
                        matter :1,
                        user_name:this.$store.state.userName,
                        // token: this.$store.state.token,
                        token: this.$store.state.token,
                        type: 'uncommitted'
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
                    //查看所有数据
                    res.data.finalizations.forEach((item, index) => {
                        data.push({
                            index: index + 1,
                            //content:item.content,
                            //start_time: item.start_time,
                            //end_time: item.end_time,
                            //content:item.content,
                            // date: item.beginTime,
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
                dialogVisible: false,
                dialog2Visible: false,
                dialog3Visible: false,
                msg: '',
                index: -1,
                row: -1,
                draftForm: {
                    start_time:'2003-5-9',
                    end_time:'2099-5-7',
                    name: '',
                    userName: '',
                    date: '',
                    info: '',
                },
            }
        },
        methods: {
            handleEdit(index, row) {
                this.dialog2Visible = true;
                this.index = index;
                this.row = row;
                console.log(index, row);
                //alert("定稿完成");


            },

            handleWatch(index, row) {
                this.index = index;
                this.row = row;
                //const roww = this.tableData.find((item) => item.index === this.row.id);
                //roww.contract_name
                //alert("前端要看"+this.$store.state.token+" **"+roww.contract_name)//+this.$refs.contracts.value)//+this.contracts[this.row.id]this.draftForm.name

                this.$axios({
                    // url:this.$url + "/contract/selContract",
                    url:this.$url + "/Check",
                    method: 'post',
                    data: {
                        contract_name : this.row.contract_name,//roww.contract_name,
                        matter :1,//待定稿
                        user_name:this.$store.userName,
                        token: this.$store.state.token,//123456
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
                    this.draftForm.name = this.row.contract_name;//res.data.name;
                    this.draftForm.userName = res.data.customer;
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
                //alert("UCC"+this.draftForm)
                //console.log("test",this.draftForm)
            },

            postMsg(){
                //alert("post="+this.$url + "/contract/commit")
                //前端[object Object] undefined 111index, row
                //前端undefined undefined 111
                //前端[object Object]  222
                //const roww = this.tableData.find((item) => item.index === this.row.id);//contract_name : roww.contract_name,
                //alert("前端"+this.$store.state.token+"* "+this.row.id+"["+this.$store.state.userName+"]"+" ["+roww.contract_name+"] "+this.msg)
                this.$axios({
                    
                    url:this.$url + "/operator/finalize/fill",
                    method: 'post',
                    data: {
                        types : 1,
                        user_name : this.$store.state.userName,
                        contract_name : this.row.contract_name,//roww.contract_name,
                        content : this.msg,
                        token: this.$store.state.token,
                        id: this.row.id,//第一行第二行
                        msg: this.msg,
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

                        // var data = [];
                        // res.data.data.forEach((item, index) => {
                        //     data.push({
                        //         index: index + 1,
                        //         date: item.beginTime,
                        //         contract_name: item.name,
                        //         id: item.id
                        //     })
                        // });

                        //待调试
                        this.tableData.splice(this.index, this.row.index)

                        this.$notify({
                            title: '成功',
                            message: '定稿成功！',
                            type: "success"
                        })
                    }

                    this.msg = ''
                    this.dialog2Visible = false;
                })
            },

            handleDelete(index, row) {
                console.log(index, row);
            },

            comment() {
                // this.$router.push('/CommentBox');
                this.$prompt('定稿意见', '提示', {
                    confirmButtonText: '确定11',
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
