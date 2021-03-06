import Vue from 'vue';
import clippy from 'clippyjs';
import App from './App.vue';
import router from "./router";
import store from "./store";
import axios from 'axios';
import * as tfc from '@tensorflow/tfjs-core';
import '@tensorflow/tfjs-backend-webgl';
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
Vue.use(BootstrapVue)
Vue.use(IconsPlugin);
Vue.use(tfc);

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
  const { authenticated, authorities } = to.meta;
  if (store.getters.loggedUser.jwt) {
    next();
  } else {
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
      next({ name: "Login", query: { redirect: to.fullPath } });
    }
  } else {
    next();
  }
})


clippy.load('Bonzi', (agent) => {
  // do anything with the loaded agent
  Vue.prototype.$agent = agent;
  agent.show();
  agent.play('Wave');
  agent.speak("Hello my friend. Welcome to our two factor face verification demonstration");
});


Vue.config.productionTip = false
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
