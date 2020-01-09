import axios from "axios";

export const BASE_URL = "https://localhost";

const baseApi = axios.create({
  baseURL: BASE_URL,
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

    //Prevents requests from getting stuck in a loop
    if (
      error.response.status === 401 &&
      originalRequest.url === `${BASE_URL}/token/refresh`
    ) {
      //Insert dispatch to display error
      return Promise.reject(new Error("Unable to get refresh Token"));
    }

    if (error.response.status === 401 && !originalRequest._retry) {
      error.config._retry = true;
      originalRequest._retry = true;
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
        .catch(error => {
          return Promise.reject(error);
        });
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
