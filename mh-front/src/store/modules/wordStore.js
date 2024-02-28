const state = {
  wordList: [],
  keyword: '',
  wordId: '',
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
  setWordId(state, value) {
    state.wordId = value;
  },
  clearWordId(state) {
    state.wordId = '';
  },
  setWordList(state, arr) {
    state.wordList = arr;
  },
};
const getters = {
  getWordList(state) {
    return state.wordList;
  },
  getKeyword(state) {
    return state.keyword;
  },
  getWordId(state) {
    return state.wordId;
  },
};
const actions = {};

export default { state, mutations, getters, actions };
