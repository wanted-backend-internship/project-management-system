import axiosInstance from "../handler/RequestInterceptor";

export const displayBasicInfo = async (boardId: string) => {
    const response = await axiosInstance.post(`/boards/${boardId}/statistics/member`);
    return response;
}

export const displayResults = async (requestData:{
    taskBoxIds: number[],
    memberNames: string[],
    startDate?: string,
    endDate?: string
}) => {
    const response = await axiosInstance.post('/boards/statistics/member/report', requestData);
    return response;
}