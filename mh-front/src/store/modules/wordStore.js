const state = {
  wordList: [],
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
};
const getters = {
  getWordList(state) {
    return state.wordList;
  },
};
const actions = {};

export default { state, mutations, getters, actions };
