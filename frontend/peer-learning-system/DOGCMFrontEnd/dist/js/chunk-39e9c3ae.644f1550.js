(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-39e9c3ae"],{"4c1c":function(t,e,s){},"57ad":function(t,e,s){"use strict";s.r(e);var i=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("el-container",{staticStyle:{width:"100%",height:"100%",top:"0",bottom:"0"}},[s("el-header",{staticStyle:{padding:"0"}},[s("el-menu",{staticClass:"el-menu-mainBar",attrs:{mode:"horizontal","menu-align":"end","background-color":"rgba(255, 255, 255, 0.5)","text-color":"#000000",router:!0,"active-text-color":"#5c76eb","border-color":"rgba(255, 255, 255, 0.5)"}},[s("el-menu-item",{directives:[{name:"show",rawName:"v-show",value:!1,expression:"false"}]},[s("el-button",{staticClass:"foldButton",staticStyle:{"background-color":"rgba(0, 0, 0, 0)",border:"0px",height:"100%",width:"100%","padding-top":"auto","padding-bottom":"auto"},attrs:{icon:t.myIcon,type:"text"},on:{click:t.clickFold}})],1),s("el-submenu",{staticClass:"main-submenu",attrs:{index:"6"}},[s("template",{slot:"title"},[t._v(t._s(this.$store.state.userName))]),s("el-menu-item",{attrs:{index:"login"}},[t._v("注销")])],2),s("el-menu-item",{staticClass:"main-submenu",attrs:{index:"addcustomer"}},[s("template",{slot:"title"},[t._v("添加客户")])],2),s("el-submenu",{staticClass:"main-submenu",attrs:{index:"4"}},[s("template",{slot:"title"},[t._v("合同签订")]),s("el-menu-item",{attrs:{index:"sign"}},[t._v("待签订合同")])],2),s("el-submenu",{staticClass:"main-submenu",attrs:{index:"3"}},[s("template",{slot:"title"},[t._v("合同审批")]),s("el-menu-item",{attrs:{index:"watchContract"}},[t._v("待审批合同")])],2),s("el-submenu",{staticClass:"main-submenu",attrs:{index:"2"}},[s("template",{slot:"title"},[t._v("合同会签")]),s("el-menu-item",{attrs:{index:"counterSign"}},[t._v("待会签合同")])],2),s("el-submenu",{staticClass:"main-submenu",attrs:{index:"/mainFrame"}},[s("template",{slot:"title"},[t._v("合同起草")]),s("el-menu-item",{attrs:{index:"draft"}},[t._v("起草合同")]),s("el-menu-item",{attrs:{index:"uncommitted"}},[t._v("待定稿合同")])],2)],1)],1),s("el-container",{staticStyle:{height:"100%",width:"100%",top:"0",bottom:"0"},attrs:{direction:"horizontal"}},[s("el-aside",{directives:[{name:"show",rawName:"v-show",value:!1,expression:"false"}],staticStyle:{height:"100%"},attrs:{width:"20%"}},[s("el-menu",{staticClass:"el-menu-vertical",staticStyle:{height:"100%"},attrs:{collapse:t.isFold}},[s("el-menu-item",{attrs:{index:"1"}},[s("i",{staticClass:"el-icon-document-add"}),s("span",{attrs:{slot:"title"},slot:"title"},[t._v("起草合同")])]),s("el-menu-item",{attrs:{index:"2"}},[s("i",{staticClass:"el-icon-document"}),s("span",{attrs:{slot:"title"},slot:"title"},[t._v("待定稿合同")])]),s("el-menu-item",{attrs:{index:"3"}},[s("i",{staticClass:"el-icon-document-checked"}),s("span",{attrs:{slot:"title"},slot:"title"},[t._v("已定稿合同")])]),s("el-menu-item",{attrs:{index:"4"}},[s("i",{staticClass:"el-icon-setting"}),s("span",{attrs:{slot:"title"},slot:"title"},[t._v("流程查询")])])],1)],1),s("el-main",[s("router-view")],1)],1)],1)},n=[],a={name:"mainFrame",data:function(){return{fold:"el-icon-s-unfold",unfold:"el-icon-s-fold",myIcon:"el-icon-s-fold",isFold:!0}},mounted:function(){this.load()},methods:{clickFold:function(){this.isFold=!this.isFold,this.myIcon=this.myIcon===this.unfold?this.fold:this.unfold},load:function(){""===this.$store.state.token&&(this.$alert("请登录账号","警告",{confirmButtonText:"确定",callback:{}}),this.$router.push("/login"))}}},l=a,o=(s("f948"),s("9f9a"),s("2877")),c=Object(o["a"])(l,i,n,!1,null,"5744cf26",null);e["default"]=c.exports},"9f9a":function(t,e,s){"use strict";var i=s("d489"),n=s.n(i);n.a},d489:function(t,e,s){},f948:function(t,e,s){"use strict";var i=s("4c1c"),n=s.n(i);n.a}}]);
//# sourceMappingURL=chunk-39e9c3ae.644f1550.js.map