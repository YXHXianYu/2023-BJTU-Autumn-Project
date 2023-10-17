// mock.js
import Mock from 'mockjs'

// 模拟客户数据
const manager_entities = Mock.mock({
    'manager_entities|5': [{
        'id|+1': 1,
        'name': '@cname',
        'date': "2023.6.13"
    }]
})

// 拦截请求并返回模拟数据
Mock.mock("http://localhost:10087/user/selectAllRight", 'post', () => {
    // 解析请求参数 查看user的全部（某一项)合同
    //alert("后端响应/contract/selectContractByType")
    //     alert("已签订")
    return {
        //code: token,
        message: 'success',
        manager_entities: manager_entities.manager_entities.map((item, index) => {
            // 根据customer属性封装对象
            //alert("sendtocontract:"+ item.name)

                //alert("sign ctrct")

                return {
                    id: item.id,
                    name: item.name,
                    date:item.date,
                    index: index,
                    rol_id: 1,
                }
        })
    }
})
