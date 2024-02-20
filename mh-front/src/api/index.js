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

async function testAPI(obj) {
  const res = await instance.post('api/test', obj);
  console.log(res.data);
}
export { registerUser, loginUser, testAPI };
