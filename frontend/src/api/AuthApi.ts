import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api/auth';

const apiClient = axios.create({
    baseURL: API_BASE_URL,
});

export const registerUser = async (userData: {
    email: string;
    password: string;
    username: string;
}) => {
    const response = await apiClient.post('/register', userData);
    return response.data;
};

export const localLogin = async (loginData: {
    email: string;
    password: string;
}) => {
    const response = await apiClient.post('/login', loginData);
    const accessToken = response.headers.authorization;

    if (accessToken) {
        localStorage.setItem('accessToken', accessToken);
    }

    return response;
};

export const logout = async () => {
    const response = await apiClient.post('/logout');
    return response;
};
