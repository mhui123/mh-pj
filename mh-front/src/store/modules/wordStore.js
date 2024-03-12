const state = {
  wordList: [],
  keyword: '',
  wordId: '',
  pageFrom: '',
  pageIdx: 0,
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
  setPageFrom(state, from) {
    state.pageFrom = from;
  },
  setPageIdx(state, value) {
    state.pageIdx = value;
  },
  initPageIdx(state) {
    state.pageIdx = 0;
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
  getPageFrom(state) {
    return state.pageFrom;
  },
  getPageIdx(state) {
    return state.pageIdx;
  },
};
const actions = {};

export default { state, mutations, getters, actions };
