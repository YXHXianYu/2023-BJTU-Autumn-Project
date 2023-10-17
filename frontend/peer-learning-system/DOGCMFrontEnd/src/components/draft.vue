<template>
  <div>
    <el-card class="box-card" width="500px" style="transform: translateX(10%)">
      <div slot="header" class="clearfix">
        起草合同
      </div>

      <div>
        <el-form ref="draftForm" :model="draftForm" :rules="rule" label-position="left" label-width="85px" :disabled = 'isdisabled'>
          <el-form-item label="合同名称:" prop="name">
            <el-input v-model="draftForm.contract_name"></el-input>
          </el-form-item>
          <el-form-item label="客户:" prop="userName">
            <el-select v-model="draftForm.customer" clearable placeholder="请选择">
              <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
            <!--                    <el-input style="width: 70%" v-model="draftForm.userName"></el-input>-->
            <el-button @click="dialogVisible=true">添加客户</el-button>
          </el-form-item>
          <el-form-item label="活动时间" prop="date">
            <el-date-picker
                v-model="draftForm.date"
                type="daterange"
                align="right"
                unlink-panels
                range-separator="至"
                value-format="yyyy-MM-dd"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>

          <el-form-item label="合同内容" prop="info">
            <el-input type="textarea" :autosize="{minRows:3, maxRows:6}" v-model="draftForm.content"></el-input>
          </el-form-item>

          <el-form-item label="提交合同" prop="info">
            <input type="file" name="file" ref="fileInput" @change="handleFileChange" />
            <!-- :autosize="{minRows:3, maxRows:6}" ($event)v-model="draftForm.info" -->
          </el-form-item>


          <el-form-item>
            <el-row>
              <el-col :span="15">
                <el-button type="primary" @click="postContract">确定</el-button>
              </el-col>

              <el-col :span="9">
                <el-button type="danger" @click="resetContract">重置</el-button>
              </el-col>
            </el-row>
          </el-form-item>

        </el-form>
      </div>

      <el-dialog @close="closeDlg" center width="35%" title="添加客户" :visible.sync="dialogVisible" append-to-body>
        <add-customer></add-customer>
      </el-dialog>

    </el-card>
  </div>
</template>

<script>
    import AddCustomer from "@/components/addCustomer";
    export default {
        mounted() {
            //alert("draft "+this.$url + "/contract/addContract");
        },//钩子
        name: "draft",
        components: {AddCustomer},
        data() {

            const generateOpt = () => {
                const data = [];

                this.$axios({
                    // url: this.$url + "/customer/selectAllCustomer",
                    url: this.$url + "/operator/draft",
                    method: 'post',
                    data: {
                        types: 0,
                        user_name : this.$store.state.userName,
                        token: this.$store.state.token,
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
                    //alert("received"+ res.data.customers)
                    //window.console.log(res.data.customers)
                    res.data.customers.forEach((item, index) => {
                        //alert("已接收："+ item.id)
                        window.console.log("Reci Customer"+item)
                        data.push({
                            value: item,
                            label: item,
                            index: index
                        });
                        window.console.log("Right:"+ res.data.right);
                        if(res.data.right === 0){
                            this.isdisabled = true;
                          this.$notify({
                            title: '失败',
                            message: '您没有该权限！',
                            type: "failed"
                          })
                        }
                        window.console.log("Reci Customer2"+ data[0].value)
                    })
                });

                return data;
            };


            return {

                dialogVisible: false,

                draftForm: {
                    contract_name:'',
                    customer:'',
                    date:'',
                    start_time:'',
                    end_time:'',
                    content:'',
                    isdisabled : false,
                    user_name:this.$store.userName,
                    file_name:null,
                    filedename:'',
                    // name: '',
                    // userName: '',
                    // file:'',
                    // date: '',
                    // info: '',
                },

                nullContract: {
                    name: '',
                    userName: '',
                    date: '',
                    info: '',
                },

                options: generateOpt(),
                value: '',

                customerValue: '',

                rule: {
                    // name: {
                    //     required: true, message: '请输入合同名称', trigger: 'blur'
                    // },

                    // userName: {
                    //     required: true, message: '请输入客户姓名', trigger: 'blur'
                    // },
                    // date: {
                    //     required: true, message: '请选择时间', trigger: 'blur'
                    // },
                    content: {
                        required: true, message: '请输入合同内容', trigger: 'blur'
                    }
                }
            }
        },

        methods: {
            handleFileChange(event) {//event +
                // 获取用户选择的文件
                // window.console.log("handleFileChange="+this.file.$refs.files[0]);
                window.console.log("handleFileChange=");
                window.console.log("handleFileChange="+event.target.files[0]);
                this.draftForm.filedename = event.target.files[0].name;
                this.draftForm.file_name = event.target.files[0];
                new Blob([event.target.files[0]]);
                // const file = event.target.files[0];
                
                // 将文件保存在组件的 data 中，以便上传时使用
                // this.draftForm.file_name = file;
            },

            resetContract() {
                this.$confirm("此操作将清空该合同，是否继续？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.draftForm = this.cleanJson(this.draftForm);
                    this.$notify({
                        title: '成功',
                        message: '清空成功！',
                        type: "success"
                    })
                })
            },


            closeDlg() {
                const generateOpt = () => {
                    const data = [];

                    this.$axios({
                        url: this.$url + "/customer/selectAllCustomer",
                        method: 'post',
                        data: {
                            token: this.$store.state.token,
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

                        res.data.customers.forEach((item, index) => {
                            data.push({
                                value: item,
                                label: item,
                                index: index
                            });
                        })

                    });

                    return data;
                };

                this.options =  generateOpt();
            },

            postContract() {

                this.$refs["draftForm"].validate((valid) => {
                    if (valid) {
                        window.console.log("receive: "+this.draftForm.file_name )
                        // eslint-disable-next-line no-console
                        //console.log(this.draftForm+this.draftForm.customer);
                        this.$axios({
                            url: this.$url + "/operator/draft",
                            // url:this.$url + "/contract/addContract",
                            method: 'post',
                            data: {
                                types:1,
                                token: this.$store.state.token,
                                contract_name: this.draftForm.contract_name,
                                customer: this.draftForm.customer,
                                //customer_id: this.draftForm.userName,
                                start_time: this.draftForm.date[0],
                                end_time: this.draftForm.date[1],
                                content: this.draftForm.content,
                                user_name:this.$store.state.userName ,//userName this.draftForm.
                                file_name:this.draftForm.filedename,
                                
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

                            if (res.data.state === 0) {
                                this.$notify({
                                    title: '成功',
                                    message: '成功添加合同！',
                                    type: "success"
                                })

                                this.draftForm = this.cleanJson(this.draftForm)
                            }
                            if (res.data.state === -1) {
                                this.$notify({
                                    title: '失败',
                                    message: '添加合同失败！',
                                    type: "error"
                                })
                            }
                            // eslint-disable-next-line no-console
                            console.log(res);
                        })

                        //传文件
                        let formData = new FormData();
                        formData.append('file',this.draftForm.file_name);
                        formData.append('contract_name',this.draftForm.contract_name);
                        this.$axios({
                            url: this.$url + "/file/file_upload",
                            // url:this.$url + "/contract/addContract",
                            method: 'post',
                            data: 
                                formData
                                // types:1,
                                // token: this.$store.state.token,
                                // contract_name: this.draftForm.contract_name,
                                // user_name:this.$store.state.userName ,//userName this.draftForm.
                                // file_name:this.draftForm.filedename,
                                
                            ,
                            // transformRequest: [function (data) {
                            //     let ret = '';
                            //     for (let it in data) {
                            //         ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                            //     }
                            //     return ret
                            // }],
                            // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                            headers: {'Content-Type': 'multipart/form-data'}
                        }).then(res => {

                            // if (res.data.state === 0) {
                            //     this.$notify({
                            //         title: '成功',
                            //         message: '成功添加合同！',
                            //         type: "success"
                            //     })

                            //     this.draftForm = this.cleanJson(this.draftForm)
                            // }
                            // if (res.data.state === -1) {
                            //     this.$notify({
                            //         title: '失败',
                            //         message: '添加合同失败！',
                            //         type: "error"
                            //     })
                            // }
                            // eslint-disable-next-line no-console
                            console.log(res);
                        })
                    } else {
                        this.$notify({
                            title: '失败',
                            message: '文件提交失败！',
                            type: "error"
                        })
                    }
                })


            },

            cleanJson(json) {
                for (let draftFormKey in json) {
                    json[draftFormKey] = ''
                }
                return json;
            }
        }
    }
</script>

<style scoped>


    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }

    .clearfix:after {
        clear: both
    }

    .box-card {
        width: 50%;
        left: 20%;
        top: 15%;
        position: relative;
    }


</style>
<style>
    .el-menu--horizontal .el-menu-item:not(.is-disabled):focus {
        color: #ffffff;
    }

    .el-menu--horizontal>.el-submenu.is-active .el-submenu__title {
        color: #ffffff;
    }

    .el-menu--horizontal .el-menu .el-menu-item.is-active, .el-menu--horizontal .el-menu .el-submenu.is-active>.el-submenu__title {
        color: #FFFFFF;
    }
</style>
