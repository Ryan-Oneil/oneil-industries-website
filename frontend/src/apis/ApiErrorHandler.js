export const getApiError = error => {
  if (error.response) {
    return error.response.data;
  } else if (error.request) {
    return error.request;
  } else {
    return error.message;
  }
};

export const getApiFormError = error => {
  if (error.response) {
    return error.response.data.errors;
  } else if (error.request) {
    return error.request;
  } else {
    return error.message;
  }
};

export const handleFormError = (error, setFieldError, setStatus) => {
  const apiError = getApiFormError(error);

  if (Array.isArray(apiError)) {
    apiError.forEach(fieldError =>
      setFieldError(fieldError.property, fieldError.message)
    );
  } else {
    setStatus({ msg: apiError, type: "error" });
  }
};
