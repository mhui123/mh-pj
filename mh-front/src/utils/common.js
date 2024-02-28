function sleep(ms) {
  if (!Number.isNaN(ms)) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}

function getCurrentRoute() {
  return location.pathname;
}

export { sleep, getCurrentRoute };
