import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import store from './store'
import router from './router'
import './theme/index.css';
import axios from 'axios';
import md5 from 'js-md5';
import './mock/loginmock.js'
import './mock/sendcontract.js'
import './mock/watchcontract.js'
import './mock/watchcontractdetail.js'
import './mock/UnCommitted.js'
import './mock/signmoc.js'
import './mock/contractwatch.js'
import './mock/contractsign.js'
import './mock/usermanagemock.js'
import './mock/alloccontract.js'
import './mock/addrootmock.js'
// import 'element-ui/lib/theme-chalk/index.css';
//

// 1.待定稿 http://localhost:10087/contract/selectContractByType
// 2.待会签 http://localhost:10087/contract/selectContractByType
// 3.已会签 http://localhost:10087/contract/selectContractByType

// 4.待审批
// 5.已审批 http://localhost:10087/contract/selectContractByType 

// 6.待签订 http://localhost:10087/contract/selectContractByType
// 7.已签订 http://localhost:10087/contract/selectContractByType

Vue.config.productionTip = false;

Vue.use(ElementUI);
Vue.prototype.$axios = axios;
Vue.prototype.$md5 = md5;
Vue.prototype.$server = "http://localhost";
Vue.prototype.$localServer = "http://localhost";
//Vue.prototype.$url = Vue.prototype.$server +  ":10087";
Vue.prototype.$url = Vue.prototype.$server +  ":8080";
Vue.prototype.$loginUrl = Vue.prototype.$url +  "/api/login";
Vue.prototype.$registerUrl = Vue.prototype.$url +  "/api/register";
Vue.prototype.$salt = "QYXTQL%%%%%11231LDLJHHAHSACOASJCPJASODPP:LNKJDAS@123123qwe123dfaf513234";

new Vue({
  store,
  router,
  render: h => h(App),

}).$mount('#app');
