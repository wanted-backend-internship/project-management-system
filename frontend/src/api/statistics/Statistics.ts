import axiosInstance from "../handler/RequestInterceptor";

export const displayBasicInfo = async (boardId: string) => {
    const response = await axiosInstance.post(`/boards/${boardId}/statistics`);
    return response;
}

export const displayMemberResults = async (requestData:{
    taskBoxIds: number[],
    memberNames: string[],
    startDate?: string,
    endDate?: string
}) => {
    const response = await axiosInstance.post('/boards/statistics/report', requestData);
    return response;
}

export const displayTeamResults = async (requestData:{
    taskBoxIds: number[],
    memberNames: string[],
    startDate?: string,
    endDate?: string
}) => {
    const response = await axiosInstance.post('/boards/statistics/report', requestData);
    return response;
}