import { loginUser } from '@/api/index';
import { saveUserToCookie, getUserFromCookie, deleteCookie } from '@/utils/cookies';
const state = {
  username: getUserFromCookie() || '',
  token: '',
};
const mutations = {
  setUsername(state, username) {
    state.username = username;
  },
  setToken(state, token) {
    state.token = token;
  },
  clearUsername(state) {
    state.username = '';
  },
  clearToken(state) {
    state.token = '';
  },
};
const getters = {
  isLogin(state) {
    return state.username !== '';
  },
  getUsername(state) {
    return state.username;
  },
};
const actions = {
  async LOGIN({ commit }, userData) {
    const { data } = await loginUser(userData);
    console.log(data);
    if (data === null) {
      return null;
    } else if (data.state === 200) {
      commit('setUsername', data.id);
      saveUserToCookie(data.id);
    }
    return data;
  },
  async LOGOUT({ commit }) {
    commit('clearUsername');
    deleteCookie('mh_user');
  },
};

export default { state, mutations, getters, actions };
