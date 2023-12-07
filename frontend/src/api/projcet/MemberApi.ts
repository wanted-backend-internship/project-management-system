import axiosInstance from "../handler/RequestInterceptor";

export const searchUser = async (memberData:{
    email: string,
}) => {
    const response = await axiosInstance.post('/boards/members/search', memberData);
    return response;
}

export const addMember = async (memberData:{
    boardId: string,
    userId: string
}) => {
    const response = await axiosInstance.post('/boards/members', memberData);
    return response;
}

export const fetchMember = async (boardId: string) => {
    const response = await axiosInstance.get(`/boards/${boardId}/members`)
    return response;
}

export const deleteMember = async (memberId: string) => {
    const response = await axiosInstance.delete(`/boards/members/${memberId}`)
    return response;
}

export const checkMember = async (boardId: string) => {
    const response = await axiosInstance.post(`/boards/${boardId}/members/check`);
    return response;
}
