function setAccessToken(accTkn) {
  localStorage.setItem('accTkn', accTkn);
}
function setRefreshToken(rfTkn) {
  localStorage.setItem('rfTkn', rfTkn);
}
function getAccessToken() {
  return localStorage.getItem('accTkn');
}
function getRefreshToken() {
  return localStorage.getItem('rfTkn');
}

function clearStorage() {
  localStorage.clear();
}

function setItem(key, item) {
  localStorage.setItem(key, item);
}
function getItem(key) {
  return localStorage.getItem(key);
}

export { setAccessToken, setRefreshToken, getAccessToken, getRefreshToken, clearStorage, setItem, getItem };
