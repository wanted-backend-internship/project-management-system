import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8081/api',
});

axiosInstance.interceptors.request.use(
    config => {
      const token = localStorage.getItem('accessToken');
      if (token) {
        config.headers.authorization = token;
        // console.log(token);
      }
      return config;
    },
    error => {
      return Promise.reject(error);
    },
);

export default axiosInstance;
