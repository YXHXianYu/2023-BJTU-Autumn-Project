//contract/sign 待签定
// mock.js
import Mock from 'mockjs'


// 拦截请求并返回模拟数据
Mock.mock("http://localhost:10087/contract/sign", 'post', (options) => {
    // 待会签!!!!!!!!!!正在签订
    //后端=[object Object] [undefined] 123
    const params = new URLSearchParams(options.body)
    const contract_name = params.get('contract_name')
    const user_name = params.get('user_name')
    const content = params.get('content')
    alert("后端签订成功="+contract_name+" ["+user_name+"] "+content)
    const token = params.get('token')
    // 验证token是否有效
    if (token === '123456') {
        //alert("hhhhhhhhhh")
        // 返回成功响应
        return {
            code: 200,
            message: 'success',
        }
    } else {
        // 返回失败响应
        return {
            code: token,
            message: "invalid"
        }
    }
})
