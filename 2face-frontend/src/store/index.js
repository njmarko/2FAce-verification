import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    loggedUser: {},
  },
  mutations: {
    setUser(state, loggedUser) {
      state.loggedUser = loggedUser;
    },
    removeUser(state) {
      state.loggedUser = {};
    },
    setLoggedIn(state, loggedIn) {
      state.loggedUser.loggedIn = loggedIn;
    },
  },
  getters: {
    loggedUser(state) {
      return state.loggedUser;
    },
  },
  actions: {
    setUser(store, payload) {
      store.commit('setUser', payload);
    },
    removeUser(store) {
      store.commit('removeUser');
    },
    setLoggedIn(store, loggedIn) {
      store.commit('setLoggedIn', loggedIn);
    },
  },
  modules: {},
  plugins: [createPersistedState()]
});