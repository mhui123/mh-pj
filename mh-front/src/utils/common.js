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
  checkBoxes = Array.from(checkBoxes);
  checkBoxes
    .filter(x => x.checked)
    .forEach(e => {
      e.checked = false;
    });
}

export { sleep, getCurrentRoute, getCheckedIds, initCheckboxes };
