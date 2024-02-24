import axios from 'axios';
const instance = axios.create({
  baseURL: process.env.VUE_APP_API_URL,
});

function registerUser(userData) {
  return instance.post('signup', userData);
}

function loginUser(userData) {
  return instance.post('login', userData);
}
// const forSecure = axios.create({
//   baseURL: process.env.VUE_APP_SEC_URL,
// });
// async function loginTest(userData) {
//   return forSecure.post('/loginProc', userData);
// }

function addWord(wordData) {
  return instance.post('addWord', wordData);
}

function getList() {
  return instance.post('getList');
}

function removeWord(id) {
  return instance.delete(`delete/${id}`);
}
export { registerUser, loginUser, addWord, getList, removeWord };
