function sleep(ms) {
  if (!Number.isNaN(ms)) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}

export { sleep };
