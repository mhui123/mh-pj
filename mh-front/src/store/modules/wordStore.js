const state = {
  wordList: [],
};
const mutations = {
  pushToWordList(state, arr) {
    arr.forEach(e => {
      state.wordList.push(e);
    });
    // if (state.wordList.length > 0) {
    //   arr.forEach(e => {
    //     state.wordList.push(e);
    //   });
    // } else {
    //   state.wordList = arr;
    // }
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
