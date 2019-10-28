import axios from 'axios';

const apiConfig = {
    headers: {
        Authorization: localStorage.getItem('token') ? localStorage.getItem('token') : "none"
    }
};

const baseApi = axios.create({
        baseURL: "http://localhost:8080/api"
});

export const apiGetCall = async endpoint => {
    return await baseApi.get(endpoint, apiConfig);
};

export const apiPostCall = async (endpoint, data) => {
    return await baseApi.post(endpoint, data, apiConfig);
};

export const apiPutCall = async (endpoint, data) => {
    return await baseApi.put(endpoint, data, apiConfig);
};

export const apiDeleteCall = async (endpoint) => {
    return await baseApi.delete(endpoint, apiConfig);
};
