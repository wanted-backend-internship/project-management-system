import axiosInstance from "../handler/RequestInterceptor";

export const createTaskBox = async (taskBoxData:{
    taskBoxTitle: string,
    boardId: string
}) => {
    const response = await axiosInstance.post('/boards/taskBox', taskBoxData);
    return response;
}

export const updateTaskBox = async (taskBoxData: {
    taskBoxTitle: string,
    taskBoxId: string
}) => {
    const response = await axiosInstance.patch('/boards/taskBox', taskBoxData);
    return response;
}

export const deleteTaskBox = async (taskBoxId: string) => {
    await axiosInstance.delete(`/boards/taskBox/${taskBoxId}`);
}

export const moveTaskBox = async (taskBoxData: {
    boardId: string,
    prevTaskBoxOrder: string,
    currentTaskBoxOrder: string
}) => {
    await axiosInstance.post('/boards/taskBox/move', taskBoxData);
}