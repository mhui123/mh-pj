import axios from 'axios';
const instance = axios.create({
  baseURL: process.env.VUE_APP_API_URL,
});

function callApi(url, param) {
  if (url.includes('getWordListByKeyword')) {
    return instance.post(url, null, { params: { keyword: param } });
  } else {
    return instance.post(url, param);
  }
}
// function logoutUser() {
//   return instance.post('logout');
// }

// const forSecure = axios.create({
//   baseURL: process.env.VUE_APP_SEC_URL,
// });
// async function loginTest(userData) {
//   return forSecure.post('/loginProc', userData);
// }

function initPw(data) {
  return instance.post('initializePw', data);
}

function getAllWordList() {
  return instance.post('findAllWordList');
}

export { initPw, getAllWordList, callApi };
