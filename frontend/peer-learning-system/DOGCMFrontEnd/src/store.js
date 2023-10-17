import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);


export default new Vuex.Store({
    state: {
        userName: '',
       // email:'',
        token: '123456',
        group: -1,
        passwd:'',
        pswMD5: '',
        pushAddr: ''
    },
    mutations: {

    },
    actions: {

    }
})
