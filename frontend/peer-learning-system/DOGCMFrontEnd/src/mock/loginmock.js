// 引入mockjs
import Mock from 'mockjs'
import Mock2 from 'mockjs'
//import MockSign from 'mockjs'
import draftMcok from 'mockjs'
//import AddMcok from 'mockjs'
import USMcok from 'mockjs'
//import MockR from 'registermock.js'
// 模拟登录成功的数据
const loginSuccess = {
    state: 0, // 状态码，0表示成功
    //group: Mock.Random.integer(0, 1), // 用户组，0表示普通用户，1表示管理员
    token: "123456", // 令牌
}
const DraftSuccess = {
    state:0,//草稿创建成功
}

const SIGNSuccess = {
    state : 0,//签订成功
    constructs : ["Construct 1", "Construct 2", "Construct 3"],
}

// 模拟账号不存在的数据
const loginFail1 = {
    state: 1, // 状态码，1表示账号不存在
}

// 模拟密码错误的数据
const loginFail2 = {
    state: -1, // 状态码，-1表示密码错误
}

// 模拟登录成功的数据
const RSuccess = {
    state: 0, // 状态码，0表示成功
    //group: Mock.Random.integer(0, 1), // 用户组，0表示普通用户，1表示管理员
    token: '123456', // 令牌
}

// 模拟账号不存在的数据
const RFail = {
    state: 1, // 状态码，1表示账号不存在
}

// 根据请求参数返回不同的数据
// 根据请求参数返回不同的数据 http://localhost:10087/user/login
Mock.mock('http://localhost:10087/user/login', 'post', (options) => {
    // 获取请求参数
    const params = new URLSearchParams(options.body)
    const user = params.get('user')
    const passwd = params.get('passwd')
    // 判断用户名和密码是否正确
    if (user === 'admin' && passwd === '123456') {
        // 返回登录成功的数据，并设置用户组为1（管理员）
        return {
            ...loginSuccess,
            group: 2,
            // 添加响应头
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Methods': 'POST',
                'Access-Control-Allow-Headers': 'Content-Type'
            }
        }
    } else if (user === 'user' && passwd === '654321') {
        // 返回登录成功的数据，并设置用户组为0（普通用户）
        return {
            ...loginSuccess,
            group: 1,
            // 添加响应头
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Methods': 'POST',
                'Access-Control-Allow-Headers': 'Content-Type'
            }
        }
    } else if (user !== 'admin' && user !== 'user') {
        // 返回账号不存在的数据
        return {
            ...loginFail1,
            // 添加响应头
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Methods': 'POST',
                'Access-Control-Allow-Headers': 'Content-Type'
            }
        }
    } else {
        // 返回密码错误的数据
        return {
            ...loginFail2,
            // 添加响应头
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Methods': 'POST',
                'Access-Control-Allow-Headers': 'Content-Type'
            }
        }
    }
})


Mock2.mock('http://localhost:10087/user/register', 'post', (options) => {
    // 获取请求参数
    //alert("进入后端！")
    const params = new URLSearchParams(options.body)
    const email = params.get('email')
    const user = params.get('user')
    const passwd = params.get('passwd')
    // 判断用户名和密码是否正确
    if (user === '11' && passwd != '' && email!='') {
        // 返回登录成功的数据，并设置用户组为1（管理员）
        return {
            ...RSuccess,
            group: 1,
            // 添加响应头
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Methods': 'POST',
                'Access-Control-Allow-Headers': 'Content-Type'
            }
        }
    } else {
        // 返回密码错误的数据
        return {
            ...RFail,
            // 添加响应头
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Methods': 'POST',
                'Access-Control-Allow-Headers': 'Content-Type'
            }
        }
    }
})

///http://localhost:10087/mainFrame/counterSign
// MockSign.mock('http://localhost:10087/contract/selectContractByType', 'post', (options) => {
//     // 获取请求参数
//     //alert("111进入已签订后端！")
//     const params = new URLSearchParams(options.body)
//     const user = params.get('id')
//     //const user = params.get('user')
//     //const passwd = params.get('passwd')
//     // 判断用户名和密码是否正确
//     if(user!="")
//     return {
//                 ...SIGNSuccess,
//                 group: 1,
//                 // 添加响应头
//                 headers: {
//                     'Access-Control-Allow-Origin': '*',
//                     'Access-Control-Allow-Methods': 'POST',
//                     'Access-Control-Allow-Headers': 'Content-Type'
//                 }
//             }
// })

draftMcok.mock('http://localhost:10087/contract/addContract', 'post', (options) => {
    // 获取请求参数
    alert("111进入已草稿后端！")
    const params = new URLSearchParams(options.body)

    const    contract_name= params.get('contract_name')
    //const    customer= params.get('customer')
    const  start_time= params.get('start_time')
    const    end_time= params.get('end_time')
   // const    content= params.get('content')
    const    user_name= params.get('user_name')
    const    file_name= params.get('file_name')
    alert(contract_name+start_time+end_time+" "+user_name+" "+file_name)
    // 判断用户名和密码是否正确
    if(contract_name!="")
    return {
        
                ...DraftSuccess,
                group: 1,
                // 添加响应头
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    'Access-Control-Allow-Methods': 'POST',
                    'Access-Control-Allow-Headers': 'Content-Type'
                }
            }
})
/*
data: {
            token: this.$store.state.token,
            contract_name: this.draftForm.name,
            customer_id: this.draftForm.userName,
            dateFrom: this.draftForm.date[0],
            dateTo: this.draftForm.date[1],
            file:this.draftForm.file,
            content: this.draftForm.info
                            },
                            */

//添加客户 http://localhost:10087/customer/addCustomer


// AddMcok.mock('http://localhost:10087/customer/addCustomer', 'post', (options) => {
//     // 获取请求参数
    
//     const params = new URLSearchParams(options.body)
//     const user = params.get('name')
//     //alert("111进入add C后端！"+user)
//     //const user = params.get('user')
//     //const passwd = params.get('passwd')
//     // 判断用户名和密码是否正确
//     if(user!="")
//     return {
//                 ...SIGNSuccess,
//                 group: 1,
//                 // 添加响应头
//                 headers: {
//                     'Access-Control-Allow-Origin': '*',
//                     'Access-Control-Allow-Methods': 'POST',
//                     'Access-Control-Allow-Headers': 'Content-Type'
//                 }
//             }
// })

//http://localhost:10087/mainFrame/counterSign 待会签

USMcok.mock('http://localhost:10087/mainFrame/counterSign', 'post', (options) => {
    // 获取请求参数
    //alert("会签后端！")
    const params = new URLSearchParams(options.body)
    const user = params.get('user_name')
    alert("待会签用户名："+user)
    // 判断用户名和密码是否正确

        // 返回登录成功的数据，并设置用户组为1（管理员）
        return {
            ...SIGNSuccess,
            group: 1,
            // 添加响应头
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Methods': 'POST',
                'Access-Control-Allow-Headers': 'Content-Type'
            }
        }

})