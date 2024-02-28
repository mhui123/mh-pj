import { callApi } from '@/api/index';
import { saveUserToCookie, getUserFromCookie, deleteCookie } from '@/utils/cookies';
import { setAccessToken, setRefreshToken, clearStorage, getAccessToken, getRefreshToken, setItem, getItem } from '@/utils/localstorageM';
const state = {
  username: getUserFromCookie() || '',
  role: getItem('role') || '',
  pageGubun: '',
  accessToken: getAccessToken(),
  refreshToken: getRefreshToken(),
  userList: [],
};
const mutations = {
  setUsername(state, username) {
    state.username = username;
  },
  setAccessToken(state, accTkn) {
    state.accessToken = accTkn;
  },
  setRefreshToken(state, refreshToken) {
    state.refreshToken = refreshToken;
  },
  setGubun(state, gubun) {
    state.gubun = gubun;
  },
  setRole(state, role) {
    state.role = role;
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
    state.role = '';
  },
  setUserList(state, arr) {
    state.userList = arr;
  },
};
const getters = {
  isLogin(state) {
    return state.accessToken;
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
  getRole(state) {
    return state.role;
  },
  getUserList(state) {
    return state.userList;
  },
};
const actions = {
  async LOGIN({ commit }, userData) {
    const { data } = await callApi('login', userData);
    const { userInfo, result } = data;
    console.log(data);
    if (data === null) {
      return null;
    } else if (result === 200) {
      commit('setUsername', userInfo.id);
      commit('setRole', userInfo.role);
      commit('setAccessToken', userInfo.token.accessToken);
      commit('setRefreshToken', userInfo.token.refreshToken);
      setAccessToken(userInfo.token.accessToken);
      setRefreshToken(userInfo.token.refreshToken);

      setItem('role', userInfo.role);
      saveUserToCookie(userInfo.id);
    }
    return data;
  },
  async LOGOUT({ commit }) {
    commit('clearUsername');
    commit('clearToken');
    deleteCookie('mh_user');
    clearStorage();
  },
};

export default { state, mutations, getters, actions };
