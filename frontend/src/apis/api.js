import axios from 'axios';

const baseApi = axios.create({
        baseURL: "http://localhost:8080/api"
});

baseApi.interceptors.request.use((config) => {
    const token = localStorage.getItem('token') ? localStorage.getItem('token') : "none";

    if ( token != null ) {
        config.headers.Authorization = token;
    }

    return config;
}, (err) => {
    return Promise.reject(err);
});

export const apiGetCall = async endpoint => {
    return await baseApi.get(endpoint);
};

export const apiPostCall = async (endpoint, data, options) => {
    return await baseApi.post(endpoint, data, options);
};

export const apiPutCall = async (endpoint, data) => {
    return await baseApi.put(endpoint, data);
};

export const apiDeleteCall = async (endpoint) => {
    return await baseApi.delete(endpoint);
};
