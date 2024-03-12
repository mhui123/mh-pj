import axios from 'axios';
const instance = axios.create({
  baseURL: process.env.VUE_APP_API_URL,
});

function callApi(url, param) {
  if (url.startsWith('delete/')) {
    return instance.delete(url);
  } else if ('edit' === url) {
    return instance.put(url, param);
  } else {
    return instance.post(url, param);
  }
}

export { callApi };
