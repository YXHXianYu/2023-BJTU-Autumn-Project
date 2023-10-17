// mock.js
import Mock from 'mockjs'

// 模拟合同数据 列表!!!!!!!!
const contractData = {
    'id|+1': 1,
    'name': '这是详细的吗',
    //'customer': '@cname',
    // 'beginTime': '',
    //'endTime': '',
    'start_time': '2003-05-01',
    'end_time': '2023-11-11',
    // 'end_time': '',
    'content': '帅帅帅!!!',
    'customer':'王文'
}
// const SigncontractData = {
//     'id|+1': 1,
//     'name': '@ctitle',
//     'customer': '@cname',
//     // 'beginTime': '',
//     // 'endTime': '',
//     'start_time': '',
//     // 'end_time': '',
//     'content': 'contenting'
// }

// 拦截请求并返回模拟数据 http://localhost:10087/contract/selectContractByType
Mock.mock("http://localhost:10087/contract/selContract", 'post', (options) => {
    // 解析请求参数
    
    const params = new URLSearchParams(options.body)
    const token = params.get('token')
    const matter = params.get('matter')//对应各个事件！！
    const contract_name = params.get('contract_name')
    alert("这是查看合同信息的后端"+contract_name)
    //const id = params.get('id')
    //alert("successgetdetail")
    // 查找对应的合同数据
    //const contractData = contract.contract.find(item => item.id == id)
    // 判断是否存在合同数据
        // 返回成功响应
    if(matter === '6'|| matter === '7')
        return {
            code: token,
            message: 'success',
            state: 0,
            ...contractData
        }
    else
        return {
            code: token,
            message: 'success',
            state: 0,
            ...contractData
        }
})
