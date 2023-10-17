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
          label="用户名称"
          prop="user_name">
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
              @click="handleEdit(scope.$index, scope.row)">分配权限
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog @close="closeDlg" title="分配用户权限" :visible.sync="dialog2Visible" append-to-body>
      <el-container direction="vertical">
        <el-container>
          <el-main>
            <el-transfer
                filterable
                filter-placeholder="请输入用户名"
                v-model="permissionValue"
                :data="permissionData1"
                :titles="['权限列表', '授予权限']"
                class="main-transfer">
            </el-transfer>
          </el-main>
        </el-container>
        <el-row>
          <el-col :span="15">
            <el-button type="primary" @click="sendData">确定</el-button>
          </el-col>

          <el-col :span="9">
            <el-button @click="closeDlg">取消</el-button>
          </el-col>
        </el-row>
      </el-container>
    </el-dialog>
  </div>
</template>

<script>

import permission from "@/components/permission.vue";

export default {
  name: "allocContract",
  computed: {
    permission() {
      return permission
    }
  },

  data() {
    const generateData = () => {

      const data = [];

      this.$axios({
        url: this.$url + "/manager/operators",/*ByProcess*/
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
        res.data.operators.forEach((item, index) => {
          data.push({
            index: index + 1,
            //date: item.beginTime,
            user_name: item.name,
            id: item.id
          })
        })
      })
      return data;
    };

    const generatePermission = () =>{
      const pData = [];
      pData.push({
        label: '定稿及起草权限',
        key: 3,
      })
      pData.push({
        label: '会签权限',
        key: 4,
      })
      pData.push({
        label: '签订权限',
        key: 5,
      })
      pData.push({
        label: '审批权限',
        key: 6,
      })
      //window.console.log("pData:"+pData[0].label+pData[0].key);
      return pData;
    }

    return {
      search: '',
      tableData: generateData(),
      permissionData1: generatePermission(),
      dialogVisible: false,
      dialog2Visible: false,
      dialog3Visible: false,
      dialog4Visible: false,
      msg: '',
      index: -1,
      row: -1,
      permissionValue: [],
      resData: [],
      commitValue: [],
      watchValue: [],
      signValue: [],
      counterData: [],
      signData: [],
      approveData: [],

    }
  },


  methods: {
    sendData(){
      this.$axios({
        url: this.$url + "/manager/contribute",
        method: 'post',
        data: {
          types : 1,
          token: this.$store.state.token,
          con_id: this.row.id,
          permissions: this.permissionValue,
          user_name: this.row.user_name
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
      this.dialog2Visible = false;
      //this.permissionData1 = [];
      this.permissionValue = [];
      //this.permissionData1 = generatePermission();
      this.$notify({
          title: '成功',
          message: '分配权限成功！',
          type: "success"
      })
    },

    closeDlg() {
      //this.permissionData = [];
      this.commitValue = [];
      this.watchValue = [];
      this.signValue = [];
      //this.permissionData1 = [];
      this.permissionValue = [];
      //this.permissionData1 = generatePermission();
      this.dialog2Visible = false;
      // eslint-disable-next-line no-console
      //console.log(this.permissionData);
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
