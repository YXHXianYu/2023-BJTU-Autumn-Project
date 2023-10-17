// mock.js
import Mock from 'mockjs'

// 模拟客户数据
const customers = Mock.mock({
    'customers|3': [{
        'id|+1': 1,
        'name': '@cname',
    }]
})

// 拦截请求并返回模拟数据
Mock.mock("http://localhost:10087/customer/selectAllCustomer", 'post', (options) => {
    // 解析请求参数 起草合同
    alert("customer/selectAllCustomer")
    const params = new URLSearchParams(options.body)
    const token = params.get('token')
    // 验证token是否有效
    if (token === '123456') {
        //  alert("hhhhhhhhhh")
        // 返回成功响应
        return {
            code: 200,
            message: 'success',

            customers: customers.customers.map((item, index) => {
                // 根据customer属性封装对象
                //alert("send:"+ item.name)
                return {
                    value: item.id,
                    label: item.name,
                    index: index
                }
            })
        }
    } else {
        // 返回失败响应
        return {
            code: token,
            message: "invalid"
        }
    }
})
