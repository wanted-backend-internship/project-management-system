import axiosInstance from './handler/RequestInterceptor';
export const loginUserInfo = async () => {
    const response = await axiosInstance.post('/users/me');
    // console.log(response.data);
    return response.data;
};
