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
function logoutUser() {
  return instance.post('logout');
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
function getWord(id) {
  return instance.post('getWordById', null, { params: { id: id } });
}
function searchWord(keyword) {
  return instance.post('getWordListByKeyword', null, { params: { keyword: keyword } });
}
function editWord(wordData) {
  return instance.put('edit', wordData);
}
export { registerUser, loginUser, addWord, getList, removeWord, editWord, getWord, searchWord, logoutUser };
