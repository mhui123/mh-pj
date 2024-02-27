const state = {
  wordList: [],
  keyword: '',
};
const mutations = {
  pushToWordList(state, arr) {
    if (Array.isArray(arr)) {
      arr.forEach(e => {
        state.wordList.push(e);
      });
    }
  },
  spliceWordList(state, idx) {
    state.wordList.splice(idx, 1);
  },
  clearWordList(state) {
    state.wordList = [];
  },
  setKeyword(state, value) {
    state.keyword = value;
  },
};
const getters = {
  getWordList(state) {
    return state.wordList;
  },
  getKeyword(state) {
    return state.keyword;
  },
};
const actions = {};

export default { state, mutations, getters, actions };
