import Vue from 'vue'
import App from './App.vue'

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import BootstrapVue from 'bootstrap-vue/dist/bootstrap-vue.esm';
import router from './router';
import axios from 'axios'

//https://www.bezkoder.com/axios-interceptors-refresh-token/
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('JWTToken');
    if (token) {
      config.headers.Authorization =  `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

Vue.use(BootstrapVue);

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
