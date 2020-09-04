export const getApiError = error => {
  if (error.response) {
    return error.response.data;
  } else if (error.request) {
    return error.request;
  } else {
    return error.message;
  }
};

export const displayBytesInReadableForm = bytes => {
  if (bytes === 0 || bytes < 0) return "0 Bytes";

  const k = 1024;
  const dm = 2 < 0 ? 0 : 2;
  const sizes = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];

  const i = Math.floor(Math.log(bytes) / Math.log(k));

  return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + " " + sizes[i];
};

export const getDateWithAddedDays = days => {
  const date = new Date();

  date.setDate(date.getDate() + days);

  return date;
};

export const getFilterSort = sorter => {
  let order = "";

  if (sorter.order === "ascend") {
    order = "asc";
  } else if (sorter.order === "descend") {
    order = "desc";
  }
  return `${sorter.field},${order}`;
};
