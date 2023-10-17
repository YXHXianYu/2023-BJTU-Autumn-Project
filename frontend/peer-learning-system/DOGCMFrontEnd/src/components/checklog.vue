<template>
  <div>
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
          prop="name">
      </el-table-column>
      <el-table-column
          label="修改时间"
          prop="time">
      </el-table-column>

    <el-table-column
          label="操作"
          prop="behaviour">
      </el-table-column>

    </el-table>

   
  </div>
</template>

<script>

    export default {
        name: "allocContract",

        data() {
            const generateData = () => {

                const data = [];

                this.$axios({
                    url: this.$url + "/manager/checkLog",/*ByProcess*/
                    method: 'post',
                    data: {
                        types : 0,
                        token: this.$store.state.token,
                        process: 1
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
                    res.data.logs.forEach((item, index) => {
                        data.push({
                            name:item.name,
                            time:item.time,
                            behaviour:item.behaviour,
                            index: index + 1,
                            // date: item.beginTime,
                            // contract_name: item.name,
                            // id: item.id
                        })
                    })
                })

                return data;
            };


            return {
                search: '',
                tableData: generateData(),
                dialogVisible: false,
                dialog2Visible: false,
                dialog3Visible: false,
                dialog4Visible: false,
                msg: '',
                index: -1,
                row: -1,

                resData: [],
                commitValue: [],
                watchValue: [],
                signValue: [],
                permissionData: [],
                counterData: [],
                signData: [],
                approveData: [],

            }
        },


        methods: {
            sendData(){
              window.console.log("hhh");
              let commituser;
              let signuser;
              let watchuser;
              commituser = [];
              signuser = [];
              watchuser = [];
              commituser = this.commitValue;
              signuser = this.watchValue;
              watchuser = this.signValue;
              window.console.log("commit: "+commituser);
              window.console.log("signuser: "+signuser);
              window.console.log("watchuser: "+ watchuser)
              if(commituser.length === 0 || signuser.length === 0 || watchuser.length === 0){
                this.$notify({
                  title: '失败',
                  message: '分配失败！',
                  type: "failure"
                })
              }
              else {
                this.$axios({
                  url: this.$url + "/manager/display/distribute",
                  method: 'post',
                  data: {
                    types : 1,
                    token: this.$store.state.token,
                    con_id: this.row.id,
                    counter_names: commituser,
                    approve_names: watchuser,
                    sign_names: signuser,
                    contract_name: this.row.contract_name,
                  },
                  transformRequest: [function (data) {
                    let ret = '';
                    for (let it in data) {
                      ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                    }
                    return ret
                  }],
                  headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                });
                this.tableData.splice(this.index, 1);
                this.$notify({
                  title: '成功',
                  message: '分配成功！',
                  type: "success"
                })
              }
              this.dialog2Visible = false;
            },

            closeDlg() {
                this.permissionData = [];
                this.commitValue = [];
                this.watchValue = [];
                this.signValue = [];
                this.dialog2Visible = false;
                // eslint-disable-next-line no-console
                console.log(this.permissionData);
            },

            handleEdit(index, row) {
                this.index = index;
                this.row = row;

                this.dialog2Visible = true;

                const generatePermissionData = () => {
                    const perData = [];
                    const signData = [];
                    const approveData = [];
                    const counterData = [];
                    this.$axios({
                        url: this.$url + "/manager/display/distribute",
                        method: 'post',
                        data: {
                            types : 0,
                            token: this.$store.state.token,
                            con_id: this.row.id
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

                        // eslint-disable-next-line no-unused-vars
                        // res.data.users.forEach((item) => {
                        //     perData.push({
                        //         label: item.name,
                        //         key: item.id,
                        //     });
                            res.data.counters.forEach((item) => {
                                counterData.push({
                                    label: item.name,
                                    key: item.id,
                                })
                            })
                            res.data.approves.forEach((item) => {
                                approveData.push({
                                    label: item.name,
                                    key: item.id,
                                })
                            })
                            res.data.signs.forEach((item) => {
                                signData.push({
                                    label: item.name,
                                    key: item.id,
                                })
                            })
                            //  if (item.permissions === 1 ||
                            //     item.permissions === 3 ||
                            //     item.permissions === 5 ||
                            //     item.permissions === 7) {
                            //     this.commitValue.push(item.id)
                            // }

                            // if (item.permissions === 2 ||
                            //     item.permissions === 3 ||
                            //     item.permissions === 6 ||
                            //     item.permissions === 7) {
                            //     this.watchValue.push(item.id)
                            // }

                            // if (item.permissions === 4 ||
                            //     item.permissions === 5 ||
                            //     item.permissions === 6 ||
                            //     item.permissions === 7) {
                            //     this.signValue.push(item.id)
                            // }
                        //});
                    });
                    // eslint-disable-next-line no-console
                    console.log("0",signData);
                    // eslint-disable-next-line no-console
                    console.log("1",counterData);
                    // eslint-disable-next-line no-console
                    console.log("2",approveData);
                    this.signData = signData;
                    this.approveData = approveData;
                    this.counterData = counterData;
                    return perData;
                };
                this.permissionData = generatePermissionData();
            },

            changeCommit(value, direction, movedKeys) {
                if (direction === "right") {
                    this.$axios({
                        url: this.$url + "/contract/updatePermission",
                        method: 'post',
                        data: {
                            token: this.$store.state.token,
                            con_id: this.row.id,
                            type: 1,
                            list: movedKeys
                        },
                        transformRequest: [function (data) {
                            let ret = '';
                            for (let it in data) {
                                ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                            }
                            return ret
                        }],
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                    })
                }
                if (direction === "left") {
                    this.$axios({
                        url: this.$url + "/contract/updatePermission",
                        method: 'post',
                        data: {
                            token: this.$store.state.token,
                            con_id: this.row.id,
                            type: -1,
                            list: movedKeys
                        },
                        transformRequest: [function (data) {
                            let ret = '';
                            for (let it in data) {
                                ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                            }
                            return ret
                        }],
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                    })
                }
            },

            changeWatch(value, direction, movedKeys) {
                if (direction === "right") {
                    this.$axios({
                        url: this.$url + "/contract/updatePermission",
                        method: 'post',
                        data: {
                            token: this.$store.state.token,
                            con_id: this.row.id,
                            type: 2,
                            list: movedKeys
                        },
                        transformRequest: [function (data) {
                            let ret = '';
                            for (let it in data) {
                                ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                            }
                            return ret
                        }],
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                    })
                }
                if (direction === "left") {
                    this.$axios({
                        url: this.$url + "/contract/updatePermission",
                        method: 'post',
                        data: {
                            token: this.$store.state.token,
                            con_id: this.row.id,
                            type: -2,
                            list: movedKeys
                        },
                        transformRequest: [function (data) {
                            let ret = '';
                            for (let it in data) {
                                ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                            }
                            return ret
                        }],
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                    })
                }
            },

            changeSign(value, direction, movedKeys) {
                if (direction === "right") {
                    this.$axios({
                        url: this.$url + "/contract/updatePermission",
                        method: 'post',
                        data: {
                            token: this.$store.state.token,
                            con_id: this.row.id,
                            type: 4,
                            list: movedKeys
                        },
                        transformRequest: [function (data) {
                            let ret = '';
                            for (let it in data) {
                                ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                            }
                            return ret
                        }],
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                    })
                }
                if (direction === "left") {
                    this.$axios({
                        url: this.$url + "/contract/updatePermission",
                        method: 'post',
                        data: {
                            token: this.$store.state.token,
                            con_id: this.row.id,
                            type: -4,
                            list: movedKeys
                        },
                        transformRequest: [function (data) {
                            let ret = '';
                            for (let it in data) {
                                ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                            }
                            return ret
                        }],
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                    })
                }
            }
        }

    }
</script>

<style scoped>

</style>
