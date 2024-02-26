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

export { setAccessToken, setRefreshToken, getAccessToken, getRefreshToken, clearStorage };
