import { loginUser } from '@/api/index';
import { saveUserToCookie, getUserFromCookie, deleteCookie } from '@/utils/cookies';
import { setAccessToken, setRefreshToken, clearStorage, getAccessToken, getRefreshToken } from '@/utils/localstorageM';
const state = {
  username: getUserFromCookie() || '',
  pageGubun: '',
  accessToken: getAccessToken(),
  refreshToken: getRefreshToken(),
};
const mutations = {
  setUsername(state, username) {
    state.username = username;
  },
  setAccessToken(state, username) {
    state.username = username;
  },
  setRefreshToken(state, username) {
    state.username = username;
  },
  setGubun(state, gubun) {
    state.gubun = gubun;
  },
  clearUsername(state) {
    state.username = '';
  },
  clearGubun(state) {
    state.gubun = '';
  },
  clearToken(state) {
    state.accessToken = '';
    state.refreshToken = '';
  },
};
const getters = {
  isLogin(state) {
    return state.username !== '';
  },
  getUsername(state) {
    return state.username;
  },
  getGubun(state) {
    return state.gubun;
  },
  getAccessToken(state) {
    return state.accessToken;
  },
  getRefreshToken(state) {
    return state.refreshToken;
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
      setAccessToken(data.token.accessToken);
      setRefreshToken(data.token.refreshToken);
      saveUserToCookie(data.id);
    }
    return data;
  },
  async LOGOUT({ commit }) {
    commit('clearUsername');
    deleteCookie('mh_user');
    clearStorage();
  },
};

export default { state, mutations, getters, actions };
