import axios from "axios";

export const BASE_URL = "https://localhost:8443";

const baseApi = axios.create({
  baseURL: BASE_URL + "/api",
  headers: {
    Authorization: localStorage.getItem("authToken")
      ? localStorage.getItem("authToken")
      : "none"
  }
});

baseApi.interceptors.response.use(
  response => response,
  error => {
    const originalRequest = error.config;

    if (!error.response) {
      return Promise.reject(error);
    }

    if (error.response.status === 401 && !originalRequest._retry) {
      const refreshToken = localStorage.getItem("refreshToken");

      if (!refreshToken) {
        return Promise.reject(error);
      }

      return baseApi
        .post("/token/refresh", refreshToken, {
          headers: {
            "Content-Type": "application/json"
          }
        })
        .then(({ data }) => {
          localStorage.setItem("authToken", data);

          baseApi.defaults.headers["Authorization"] = data;
          originalRequest.headers["Authorization"] = data;

          return axios(originalRequest);
        })
        .catch(err => {});
    }

    return Promise.reject(error);
  }
);

export const apiGetCall = async endpoint => {
  return await baseApi.get(endpoint);
};

export const apiPostCall = async (endpoint, data, options) => {
  return await baseApi.post(endpoint, data, options);
};

export const apiPutCall = async (endpoint, data) => {
  return await baseApi.put(endpoint, data);
};

export const apiDeleteCall = async endpoint => {
  return await baseApi.delete(endpoint);
};
