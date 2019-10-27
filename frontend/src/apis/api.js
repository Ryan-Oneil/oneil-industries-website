import axios from 'axios';

const apiConfig = {
    headers: {
        Authorization: "Bearer " + localStorage.getItem('token'),
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

export const apiDeleteCall = async (endpoint) => {
    return await baseApi.delete(endpoint, apiConfig);
};
