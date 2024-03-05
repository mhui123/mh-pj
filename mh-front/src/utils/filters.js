function formatDate(value) {
  const date = new Date(value);
  const yyyy = date.getFullYear();
  let mm = date.getMonth() + 1;
  mm = mm > 9 ? mm : `0${mm}`;
  const dd = date.getDate() > 9 ? date.getDate() : `0${date.getDate()}`;
  let h = date.getHours();
  h = h > 9 ? h : `0${h}`;
  const minute = date.getMinutes() > 9 ? date.getMinutes() : `0${date.getMinutes()}`;

  return `${yyyy}-${mm}-${dd} ${h}:${minute}`;
}

export { formatDate };
