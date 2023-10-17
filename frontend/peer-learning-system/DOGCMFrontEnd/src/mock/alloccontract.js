// mock.js
import Mock from 'mockjs'

// 模拟客户数据
const contracts = Mock.mock({
    'contracts|5': [{
        'id|+1': 1,
        'name': '@cname',
        'date': "2023.6.13"
    }]
})

// 拦截请求并返回模拟数据
Mock.mock("http://localhost:10087/contract/selectAllContract", 'post', () => {
    // 解析请求参数 查看user的全部（某一项)合同
    //alert("后端响应/contract/selectContractByType")
    //alert("hhhhhhhhhh")
    // 返回成功响应
    return {
        code: 200,
        message: 'success',
        contracts: contracts.contracts.map((item, index) => {
            // 根据customer属性封装对象
            //alert("sendtocontract:"+ item.name)
                //alert("sign ctrct")
                return {
                    id: item.id,
                    name: item.name,
                    beginTime:item.date,
                    index: index
                }
        })
    }
})
