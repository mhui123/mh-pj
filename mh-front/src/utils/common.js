function sleep(ms) {
  if (!Number.isNaN(ms)) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}

function getCurrentRoute() {
  return location.pathname;
}

function getCheckedIds() {
  let checkBoxes = document.getElementsByClassName('chkInput');
  checkBoxes = Array.from(checkBoxes);
  return checkBoxes.filter(x => x.checked).map(m => m.id);
}
function initCheckboxes() {
  let checkBoxes = document.getElementsByClassName('chkInput');
  let checkAllBtn = document.getElementById('checkAll');
  checkAllBtn.checked = false;
  checkBoxes = Array.from(checkBoxes);
  checkBoxes
    .filter(x => x.checked)
    .forEach(e => {
      e.checked = false;
    });
}
function checkAll() {
  let checkBoxes = document.getElementsByClassName('chkInput');
  let chkedCnt = 0;
  checkBoxes = Array.from(checkBoxes);
  checkBoxes.forEach(e => {
    if (e.checked === true) {
      chkedCnt = chkedCnt + 1;
    }
    e.checked = !e.checked;
  });
  if (chkedCnt > 0) {
    document.getElementById('checkAll').checked = false;
    checkBoxes.forEach(e => {
      e.checked = false;
    });
  }
}

export { sleep, getCurrentRoute, getCheckedIds, initCheckboxes, checkAll };
