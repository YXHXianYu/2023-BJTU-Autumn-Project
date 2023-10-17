// mock.js
import Mock from 'mockjs'

// 模拟客户数据


// 拦截请求并返回模拟数据
Mock.mock("http://localhost:10087/user/addRootList", 'post', (options) => {
    // 解析请求参数 查看user的全部（某一项)合同
    //alert("后端响应/contract/selectContractByType")

    const params = new URLSearchParams(options.body)
    const keys = new params.get("list")
    //alert("hhhhhhhhhh")
    window.console.log("backend"+keys)
    // 返回成功响应
})
