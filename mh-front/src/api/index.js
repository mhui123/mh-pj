import axios from 'axios';
const instance = axios.create({
  baseURL: process.env.VUE_APP_API_URL,
});
const forSecure = axios.create({
  baseURL: process.env.VUE_APP_SEC_URL,
});
function registerUser(userData) {
  return instance.post('signup', userData);
}

function loginUser(userData) {
  return instance.post('login', userData);
}

async function testAPI() {
  console.log(process.env.VUE_APP_API_URL, process.env.SEC_URL, process.env.USER_URL);
  const res = await instance.post('/test');
  console.log(res.data);
}
async function loginTest(userData) {
  return forSecure.post('/loginProc', userData);
}
export { registerUser, loginUser, testAPI, loginTest };
