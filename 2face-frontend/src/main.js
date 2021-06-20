import Vue from 'vue';
import App from './App.vue';
import router from "./router";
import store from "./store";
import axios from 'axios';
import {BootstrapVue, IconsPlugin  } from "bootstrap-vue";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
Vue.use(BootstrapVue)
Vue.use(IconsPlugin);

// Configure axios to always include JWT when sending a request
axios.interceptors.request.use(config => {
  if (store.getters.loggedUser.jwt) {
    config.headers.Authorization = `Bearer ${store.getters.loggedUser.jwt}`
  }
  return config;
}, error => {
  return Promise.reject(error);
})

// Configure frontend authorization
router.beforeEach((to, _from, next) => {
  const {authenticated, authorities} = to.meta;
  if(store.getters.loggedUser.jwt){
      next();
  } else{
    next();
  }

  if (authenticated) {
    if (store.getters.loggedUser.jwt) {
      if (authorities && store.getters.loggedUser.authorities) {
        if (authorities.some(role => store.getters.loggedUser.authorities.includes(role))) {
          next();
        } else {
          next({ name: "Home" });
        }
      } else {
        next();
      }
    } else {
      next({name: "Login", query: { redirect: to.fullPath } });
    }
  } else {
    next();
  }
})

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
